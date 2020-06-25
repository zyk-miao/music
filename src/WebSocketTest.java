import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value="/websocket/{id}")
public class WebSocketTest {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //存放每个连接
    private static ConcurrentHashMap<String,WebSocketTest> webSocketMap=new ConcurrentHashMap<String, WebSocketTest>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    String id;
    //存放每个连接的唯一标志 Servlet返回的那个字符串
    public static CopyOnWriteArrayList<String> id_list=new CopyOnWriteArrayList();
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "id") String param, Session session) throws IOException {
        id =param;
        this.session = session;
        addOnlineCount();           //在线数加1
        System.out.println("新"+id+"加入！当前在线" + getOnlineCount());
        //利用唯一标志 作键名 存放连接
        webSocketMap.put(id,this);
        id_list.add(id);
        //测试用
        if(getOnlineCount()==1){
            System.out.println("1触发");
        }
        else {
            //当有第二个及以后的连接 就向第一个连接请求第一个的播放状态
            webSocketMap.get(id_list.get(0)).sendMessage("get_now_info");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        //移除掉连接
       webSocketMap.remove(id);
       id_list.remove(id);
        subOnlineCount();           //在线数减1
        System.out.println("客户端" +id+"断开连接 当前在线人数"+ getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        System.out.println("来自客户端" + id + "的消息:" + message);
        //如果第一个发送的消息就转发给第二个 反之
        if (getOnlineCount() > 1) {
            if (id.equals(id_list.get(0))) {
                webSocketMap.get(id_list.get(1)).sendMessage(message);
            } else if (id.equals(id_list.get(1))) {
                webSocketMap.get(id_list.get(0)).sendMessage(message);
            } else return;
        }
        else return;
    }


    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
//        this.session.getBasicRemote().sendText(message);
        this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }

}