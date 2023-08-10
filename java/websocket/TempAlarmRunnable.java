package com.ruoyi.web.controller.websocket.MyRunnable;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.AlarmData;
import com.ruoyi.system.domain.Equipment;
import com.ruoyi.system.domain.TempAlarmConfig;
import com.ruoyi.system.domain.TempData;
import com.ruoyi.system.sdk.SdkManager;
import com.ruoyi.system.service.IAlarmDataService;
import com.ruoyi.system.service.IEquipmentService;
import com.ruoyi.web.controller.thermometry.AlarmDataController;
import com.ruoyi.web.controller.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * 监控温度报警线程
 */
public class TempAlarmRunnable implements Runnable {
    private Long equipmentId;
    private final static Logger log = LoggerFactory.getLogger(TempAlarmRunnable.class);


    public TempAlarmRunnable(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    //监测所有的报警设备，使用当前报警设备id，每5s查询出数据
    @Override
    public void run() {
        if (CollectionUtil.isNotEmpty(WebSocketServer.webSocketSet)) {
            //取出存入hashMap当中的值
            ConcurrentHashMap<String, String> hashMap = AlarmDataController.hashMap;
            if (!hashMap.isEmpty()) {
                String s = hashMap.get(Constants.Temp_Alarm_Config + equipmentId);
                //获取温度报警配置参数
                TempAlarmConfig tempAlarmConfig = JSON.parseObject(s, TempAlarmConfig.class);
                selectAlarmData(tempAlarmConfig, equipmentId);
            }
        }
    }

    public void selectAlarmData(TempAlarmConfig tempAlarmConfig, Long webSocketId) {

        //在非Spring环境下 获取bean
        IEquipmentService equipmentService = SpringUtils.getBean(IEquipmentService.class);
        IAlarmDataService alarmDataService = SpringUtils.getBean(IAlarmDataService.class);
        SdkManager sdkManager = SpringUtils.getBean(SdkManager.class);

        //取出高温一级阈值
        Double highTempThresholdLevel1 = tempAlarmConfig.getHighTempThresholdLevel1();
        //取出低温一级阈值
        Double lowTempThresholdLevel1 = tempAlarmConfig.getLowTempThresholdLevel1();
        //取出整帧的最高温最低温
        //根据ID查询对应的设备信息
        Equipment equipment = equipmentService.selectEquipmentById(tempAlarmConfig.getEquipmentId());
        if (equipment != null && equipment.getNetStatus().equalsIgnoreCase("1")
                && "1".equals(tempAlarmConfig.getTempAlarmSwitch())) {
            //当前帧温度参数
            TempData tempData = sdkManager.getFullFrameTempData(equipment);
            if (tempData != null) {
                //当前帧最高温，最低温，设置当前帧温度信息
                Double tempMax = tempData.getTempMax();
                Double tempMin = tempData.getTempMin();
                AlarmData alarmData = setAlarmData(tempAlarmConfig, tempData);
                //当温度报警开关打开 且高温报警开关也打开，当前温度高于一级高温阈值 触发高温报警条件
                if (tempAlarmConfig.getHighTempAlarmEnable() != null && tempAlarmConfig.getHighTempAlarmEnable() == 1
                        && highTempThresholdLevel1 != null && tempMax > highTempThresholdLevel1) {
                    //设置报警等级为1级 设置温度情况为报警0
                    alarmData.setNormal(0);
                    alarmData.setAlarmType(1);
                    alarmData.setAlarmLevel(1);
                    if (tempAlarmConfig.getHighTempThresholdLevel2() != null && tempMax > tempAlarmConfig.getHighTempThresholdLevel2()) {
                        //设置报警等级为2级
                        alarmData.setAlarmLevel(2);
                        if (tempAlarmConfig.getHighTempThresholdLevel3() != null && tempMax > tempAlarmConfig.getHighTempThresholdLevel3()) {
                            //设置报警等级为3级
                            alarmData.setAlarmLevel(3);
                        }
                    }
                    //满足温度报警,是否抓拍
                    if (tempAlarmConfig.getSnapEnable() != null && tempAlarmConfig.getSnapEnable() == 1) {
                        //开启抓拍
                        String snapPath = sdkManager.snapshotReturnPath(equipment) + ".jpg";
                        String filePath = equipment.getIp() + "F" + snapPath;
                        alarmData.setRelateSnap(filePath);
                    }
                    //报警信息存到数据库当中
                    alarmDataService.insertAlarmData(alarmData);
                    if (alarmData.getRelateSnap() != null && !alarmData.getRelateSnap().equals("")) {
                        //把snapPath转成Base64编码
                        String snapBase64 = downloadPic(alarmData.getRelateSnap(), 1);
                        alarmData.setRelateSnap(snapBase64);
                    }
                    //符合报警条件进行高温报警
                    //给前台返回消息
                    WebSocketServer.sendToMessageById(webSocketId, JSON.toJSONString(alarmData));
                }
                //当温度报警开关打开 且低温报警开关也打开的时候
                if (tempAlarmConfig.getLowTempAlarmEnable() != null && tempAlarmConfig.getLowTempAlarmEnable() == 1) {
                    //当前温度小于一级低温阈值 触发低温报警条件
                    if (lowTempThresholdLevel1 != null && tempMin < lowTempThresholdLevel1) {
                        //设置报警等级为1级 设置温度情况为报警0
                        alarmData.setNormal(0);
                        alarmData.setAlarmType(1);
                        alarmData.setAlarmLevel(1);
                        if (tempAlarmConfig.getLowTempThresholdLevel2() != null && tempMin < tempAlarmConfig.getLowTempThresholdLevel2()) {
                            //设置报警等级为2级
                            alarmData.setAlarmLevel(2);
                            if (tempAlarmConfig.getLowTempThresholdLevel3() != null && tempMin < tempAlarmConfig.getLowTempThresholdLevel3()) {
                                //设置报警等级为3级
                                alarmData.setAlarmLevel(3);
                            }
                        }
                        //满足温度报警,是否抓拍
                        if (tempAlarmConfig.getSnapEnable() != null && tempAlarmConfig.getSnapEnable() == 1) {
                            //开启抓拍
                            String snapPath = sdkManager.snapshotReturnPath(equipment) + ".jpg";
                            String filePath = equipment.getIp() + "F" + snapPath;
                            alarmData.setRelateSnap(filePath);
                        }
                        //报警信息存到数据库当中
                        alarmDataService.insertAlarmData(alarmData);
                        if (alarmData.getRelateSnap() != null && !alarmData.getRelateSnap().equals("")) {
                            //把snapPath转成Base64编码
                            String snapBase64 = downloadPic(alarmData.getRelateSnap(), 1);
                            alarmData.setRelateSnap(snapBase64);
                        }
                        //符合报警条件进行高温报警
                        //给前台返回消息
                        WebSocketServer.sendToMessageById(webSocketId, JSON.toJSONString(alarmData));
                    }
                }
            }
        }
    }

    private AlarmData setAlarmData(TempAlarmConfig tempAlarmConfig, TempData tempData) {
        AlarmData alarmData = new AlarmData();
        alarmData.setEquipmentId(tempAlarmConfig.getEquipmentId());
        alarmData.setEquipmentName(tempAlarmConfig.getEquipmentName());
        alarmData.setTempRegionName(tempAlarmConfig.getRegionName());
        alarmData.setNormal(1);
        alarmData.setTempMax(tempData.getTempMax());
        alarmData.setTempMin(tempData.getTempMin());
        alarmData.setTempAvg(tempData.getTempAvg());
        alarmData.setCreateTime(DateUtils.getNowDate());
        return alarmData;
    }

    public String downloadPic(String url, int flag) {
        String base64Image = "";
        try {
            //输入流，通过输入流读取文件内容 绝对路径无法直接传参
            log.info("读取到的数据库路径为:{}", url);
            //构建fileInputStream eg:192.168.1.30F20230625154508oneSnap.jpg =>192.168.1.30//20230625154508oneSnap.jpg
            String filePathSuffix = "";
            //指定基础路径路径
            String hotPicFilePath = "";
            //判断系统
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {// Windows 操作系统
                filePathSuffix = url.replaceAll("F", "\\\\");
                //3.判断在指定盘符中是否存在以该设备IP命名的设备抓拍文件夹
                hotPicFilePath = "D:\\snapPic\\" + filePathSuffix;
            } else if (os.contains("nux") || os.contains("nix")) {// Linux 或 UNIX 操作系统
                filePathSuffix = url.replaceAll("F", "/");
                //3.判断在指定盘符中是否存在以该设备IP命名的设备抓拍文件夹
                hotPicFilePath = "/home/snapPic/" + filePathSuffix;
            }
            log.info("存储到空间当中的绝对路径为:{}", hotPicFilePath);
            File file = new File(hotPicFilePath);
            if (file.exists()) {
                //转换成B64编码响应给前端
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                //将字节数据进行B64编码
                base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
            } else {
                if (flag <= 3) {
                    //可能是当前图片还没加载进文件夹，重新执行一遍该方法,尝试三次
                    downloadPic(url, ++flag);
                }
            }
        } catch (Exception e) {
            log.error("图片抓拍或下载失败");
        }
        return base64Image;
    }
}
