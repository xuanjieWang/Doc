package com.ruoyi.web.controller.websocket.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Equipment;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface WebSocketServerService {
    /**
     * 异步定时每隔30秒检测设备是否断开连接
     * <p>
     * fixedDelay：每次延时2秒执行一次任务
     * 注意，这里是等待上次任务执行结束后，再延时固定时间后开始下次任务
     *
     * @throws InterruptedException
     */
    public void testConnection() throws InterruptedException, ExecutionException, TimeoutException;

    //定时检测设备状态任务启动
    public void start(Equipment equipment);
    //根据设备ID开启定时任务
    public void startByEquipmentId(Long id);
    //定时检测设备状态任务关闭
    public void stop(Equipment equipment);
    //根据ID关闭定时任务
    public void stopByEquipmentId(Long id);
    //判断是否已经开启了所有已存在的设备的定时任务，没有则开启
    public void JudgeEmptyAndStartFirst(List<Equipment> list);

}
