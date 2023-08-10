package com.ruoyi.web.controller.websocket;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.websocket.SemaphoreUtils;
import com.ruoyi.framework.websocket.WebSocketUsers;
import com.ruoyi.system.domain.Equipment;
import com.ruoyi.system.service.IEquipmentService;
import constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * websocket 消息处理
 *
 * @author ruoyi
 */

//交给Spring来管理
@Component
//建立连接的端点
@ServerEndpoint("/websocket/{equipmentId}")
public class WebSocketServer {
    /**
     * WebSocketServer 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 默认最多允许同时在线人数100
     */
    public static int socketMaxOnlineCount = 100;
    /**
     * 信号量控制在线人数
     */
    private static Semaphore socketSemaphore = new Semaphore(socketMaxOnlineCount);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    public static ConcurrentHashMap<Long, WebSocketServer> webSocketSet = new ConcurrentHashMap<Long, WebSocketServer>();
    /**
     * concurrent包的线程安全hashMap,用来存放每一个session里的参数信息
     */
    private static ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 传过来的id 不同设备不同id
     */
    private Long id = 0l;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam(value = "equipmentId") Long param, Session session) throws Exception {
        boolean semaphoreFlag = false;
        // 尝试获取信号量
        semaphoreFlag = SemaphoreUtils.tryAcquire(socketSemaphore);
        if (!semaphoreFlag) {
            // 未获取到信号量
            LOGGER.error("\n 当前在线人数超过限制数- {}", socketMaxOnlineCount);
            WebSocketUsers.sendMessageToUserByText(session, "忙碌中,请稍后再试...");
            session.close();
        } else {
            // 添加用户
            WebSocketUsers.put(session.getId(), session);
            //接收客户端编号并且加入到set集合当中
            id = param;
            this.session = session;
            webSocketSet.put(param, this);
            //获取session当中的参数
            // Map<String, List<String>> map = session.getRequestParameterMap();
            // if (!map.isEmpty()){
            //     //把取出来的参数存入hashmap当中
            //     hashMap.put(session.getId(),map.get("equipmentIds").get(0));
            // }
            LOGGER.info("\n 建立连接 - {}", session);
            LOGGER.info("\n 当前人数 - {}", WebSocketUsers.getUsers().size());
        }
    }

    /**
     * 连接关闭时处理
     */
    @OnClose
    public void onClose(Session session) {
        LOGGER.info("\n 关闭连接 - {}", session);
        // 移除用户
        WebSocketUsers.remove(session.getId());
        webSocketSet.remove(id);
        // 获取到信号量则需释放
        SemaphoreUtils.release(socketSemaphore);
    }

    /**
     * 抛出异常时处理
     */
    @OnError
    public void onError(Session session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            // 关闭连接
            session.close();
        }
        String sessionId = session.getId();
        LOGGER.info("\n 连接异常 - {}", sessionId);
        LOGGER.info("\n 异常信息 - {}", exception);
        // 移出用户
        WebSocketUsers.remove(sessionId);
        // 获取到信号量则需释放
        SemaphoreUtils.release(socketSemaphore);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
//       WebSocketUsers.sendMessageToUserByText(session, JSON.toJSONString(AjaxResult.success("搜索中")));
//        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
//        IEquipmentService equipmentService = SpringUtils.getBean(IEquipmentService.class);
//        // 先从缓存中获取设备信息
//        List<Equipment> list = redisCache.getCacheList(Constants.EQUIPMENT_CACHE_LIST);
//        // 缓存中获取设备信息没有设备信息,然后调用sdk方法搜索设备，只有设备信息使用到了websocket
//        if (!StringUtils.isNotNull(list) || list.size() == 0) {
//            list = equipmentService.searchEquipment();
//        }
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(list);
//        rspData.setTotal(new PageInfo(list).getTotal());
//        WebSocketUsers.sendMessageToUserByText(session, JSON.toJSONString(rspData));
    }


    /**
     * 给指定的人发送消息
     *
     * @param message
     */
    public static void sendToMessageById(Long id, String message) {
        try {
            if (webSocketSet.get(id) != null) {
                webSocketSet.get(id).sendMessage(message);
            } else {
                System.out.println("webSocketSet中没有此key，不推送消息");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        synchronized (session) {
            getSession().getBasicRemote().sendText(message);
        }
    }

    public Session getSession() {
        return session;
    }

    public static ConcurrentHashMap<String, String> getHashMap() {
        return hashMap;
    }
}
