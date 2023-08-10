package com.ruoyi.web.controller.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.system.domain.Equipment;
import com.ruoyi.system.domain.TempAlarmConfig;
import com.ruoyi.system.domain.TempData;
import com.ruoyi.system.sdk.SdkManager;
import com.ruoyi.system.service.IEquipmentService;
import com.ruoyi.system.service.ITempAlarmConfigService;
import com.ruoyi.system.service.ITempDataService;
import com.ruoyi.system.util.LoginJudge;
import com.ruoyi.web.controller.websocket.WebSocketServer;
import com.ruoyi.web.controller.websocket.service.WebSocketServerService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
// @EnableScheduling //1.开启定时任务
// @EnableAsync //2.开启多线程
public class WebSocketServerServiceImpl implements WebSocketServerService {

    private final static Logger log = LoggerFactory.getLogger(WebSocketServerServiceImpl.class);

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 用来存储每一台设备的状态定时任务
     */
    public static final ConcurrentHashMap<Long, ScheduledExecutorService> equipmentNatTask = new ConcurrentHashMap<>();

    /**
     * 用来存储当前开启定时任务的设备id,使用的是并发集合类，是线程安全的
     */
    private static final CopyOnWriteArrayList<Long> hashEquipment = new CopyOnWriteArrayList<>();

    /**
     * 核心线程数
     */
    private static final int corePoolSize = 10;
    /**
     * 最大线程数
     */
    private static final int maximumPoolSize = 20;
    /**
     * 核心线程数存活时间
     */
    private static final long keepAliveTime = 10L; // 存活时间为5分钟


    @Resource
    private IEquipmentService equipmentService;
    @Resource
    private ITempAlarmConfigService tempAlarmConfigService;
    @Resource
    private SdkManager sdkManager;
    @Resource
    private ITempDataService tempDataService;


    /**
     * 异步定时每隔30秒检测设备是否断开连接
     * <p>
     * fixedDelay：每次延时2秒执行一次任务
     * 注意，这里是等待上次任务执行结束后，再延时固定时间后开始下次任务
     *
     * @throws InterruptedException
     */
    @Override
    // @Async("asyncPoolTaskExecutor")
    // @Scheduled(fixedDelay = 1000 * 3)
    public void testConnection() throws InterruptedException, ExecutionException, TimeoutException {
    }

    @Override
    public void start(Equipment equipment) {
        //判断当前设备是否在线
        if (equipment.getNetStatus().equals("1")){
            // 判断条件是否满足
            if (conditionSatisfied(equipment)) {
                // 创建线程池
                scheduledExecutorService = Executors.newScheduledThreadPool(10);
                //把该线程池添加进map集合中(标记定时任务)
                equipmentNatTask.put(equipment.getId(), scheduledExecutorService);
                //把该线程添加进list集合中(标记设备)
                hashEquipment.add(equipment.getId());
                // 启动定时任务
                scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        // 执行任务
                        doTask(equipment);
                    }
                }, 0, 5, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void startByEquipmentId(Long id) {
        // 创建线程池
        scheduledExecutorService = Executors.newScheduledThreadPool(10);
        //把该线程池添加进map集合中(标记定时任务)
        equipmentNatTask.put(id, scheduledExecutorService);
        //把该线程添加进list集合中(标记设备)
        hashEquipment.add(id);
        // 启动定时任务
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Equipment equipment = equipmentService.selectEquipmentById(id);
                if (equipment != null) {
                    // 执行任务
                    doTask(equipment);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop(Equipment equipment) {
        //获取对应的定时任务
        ScheduledExecutorService scheduledExecutorService = equipmentNatTask.get(equipment.getId());
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            //移除标记
            equipmentNatTask.remove(equipment.getId());
            hashEquipment.remove(equipment.getId());
            log.info("ID为{}的设备测温任务已关闭", equipment.getId());
        }
    }

    @Override
    public void stopByEquipmentId(Long id) {
        //获取对应的定时任务
        ScheduledExecutorService scheduledExecutorService = equipmentNatTask.get(id);
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            //移除标记
            equipmentNatTask.remove(id);
            hashEquipment.remove(id);
            log.info("ID为{}的设备测温任务已关闭", id);
        }
    }

    @Override
    public void JudgeEmptyAndStartFirst(List<Equipment> list1) {
        //过滤出所有在线的设备
        List<Equipment> list = list1.stream().filter(s -> s.getNetStatus().equals("1")).collect(Collectors.toList());
        // 获取所有的键
        Set<Long> keys = equipmentNatTask.keySet();
        if (keys.size() == 0) {
            //没有定时任务 遍历list开启设备的定时任务
            list.forEach(this::start);
        } else {
            //已存在的设备定时任务
            List<Long> hasEquipmentId = new ArrayList<>(hashEquipment);
            //传入的需要判断的设备是否开启定时任务
            List<Long> judgeEquipments = list.stream().map(Equipment::getId).collect(Collectors.toList());
            if (hasEquipmentId.size() > judgeEquipments.size()) {
                //已开启的定时任务多余实际的定时任务 关闭多余的定时任务
                hasEquipmentId.removeAll(judgeEquipments);
                hasEquipmentId.forEach(this::stopByEquipmentId);
            }
            if (hasEquipmentId.size() < judgeEquipments.size()) {
                //开启未开启设备的定时任务
                judgeEquipments.removeAll(hasEquipmentId);
                judgeEquipments.forEach(this::startByEquipmentId);
            }
            // //关闭所有已经开启的定时任务重新开启
            // list.forEach(this::stop);
            // //查询所有设备
            // List<Equipment> list1 = equipmentService.selectEquipmentList(new Equipment());
            // list1.forEach(this::start);
        }
    }

    private boolean conditionSatisfied(Equipment equipment) {
        // 判断条件是否满足
        return equipment.getIp() != null && equipment.getPort() != null;
    }

    private void doTask(Equipment equipment) {
        // 每 5 秒执行一次的任务 检测设备是否断开连接以及测温
        boolean ipIsOpen = LoginJudge.remoteEquipOpen(equipment);
        log.debug("检测id为{}的设备是否在线，检测结果为:{}",equipment.getId(),ipIsOpen);
        if (ipIsOpen) {
            if (!equipment.getNetStatus().equalsIgnoreCase("1")) {
                equipment.setNetStatus("1");
                equipmentService.updateEquipment(equipment);
            }
            log.info("ID为{}的设备在线且测温任务正在执行", equipment.getId());
            //当前设备在线 进行测温
            //1.获取当前帧温度
            TempData tempData = sdkManager.getFullFrameTempData(equipment);
            tempData.setNormal(1);
            tempData.setTempRegionName(equipment.getGroupName());
            //2.根据设备id获取报警配置
            TempAlarmConfig tempAlarmConfig = tempAlarmConfigService.selectTempAlarmConfigByEquipmentId(equipment.getId());
            if (tempAlarmConfig != null) {
                if (tempAlarmConfig.getHighTempThresholdLevel1() != null) {
                    //该设备存在报警配置 判断当前帧温度是否正常并存入数据库
                    if (tempData.getTempMax() > tempAlarmConfig.getHighTempThresholdLevel1()) {
                        tempData.setNormal(0);
                    }
                }
                if (tempAlarmConfig.getLowTempThresholdLevel1() != null) {
                    //该设备存在报警配置 判断当前帧温度是否正常并存入数据库
                    if (tempData.getTempMin() < tempAlarmConfig.getLowTempThresholdLevel1()) {
                        tempData.setNormal(0);
                    }
                }
            }
            //将当前帧温度存入数据库
            tempDataService.insertTempData(tempData);
        } else {
            if (!equipment.getNetStatus().equalsIgnoreCase("0")) {
                log.info("ID为{}的设备不在线", equipment.getId());
                equipment.setNetStatus("0");
                equipmentService.updateEquipment(equipment);
            }
        }
    }
}
