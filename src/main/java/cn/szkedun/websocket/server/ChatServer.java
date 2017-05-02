//package cn.szkedun.websocket.server;
//
//
//
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Set;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSONObject;
//
//import cn.szkedun.websocket.utils.Base64;
//
//@ServerEndpoint(value = "/ws/chat/{token}/{nickName}")
//@Component
//public class ChatServer {
//
//	private static Logger logger = LoggerFactory.getLogger(ChatServer.class);
//	/**
//	 * 连接对象集合
//	 */
//	private static final Set<ChatServer> connections = new CopyOnWriteArraySet<ChatServer>();
//
//	private String nickName;
//	
//	private String token;
//
//	private String unameKey;
//
//	/**
//	 * WebSocket Session
//	 */
//	private Session session;
//
//	public ChatServer() {
//	}
//
//	/**
//	 * 打开连接
//	 * 
//	 * @param session
//	 * @param nickName
//	 */
//	@OnOpen
//	public void onOpen(Session session, @PathParam(value = "token") String token, @PathParam(value = "nickName") String nickName) {
//
//		this.session = session;
//		this.token=token;
//		this.nickName = nickName;
//		this.unameKey = " 【" + nickName + "】@" + Base64.decodeBase64(this.token);
//		connections.add(this);
//		String message =getOnlineList();
//		ChatServer.broadCast("system", 1, message);
//	}
//
//	/**
//	 * 关闭连接
//	 */
//	@OnClose
//	public void onClose() {
//		connections.remove(this);
//		String message =getOnlineList();
//		ChatServer.broadCast("system", 1, message);
//		logger.info(nickName+":has disconnection.");
//	}
//
//	/**
//	 * 接收信息
//	 * 
//	 * @param message
//	 * @param nickName
//	 */
//	@OnMessage
//	public void onMessage(String message, @PathParam(value = "nickName") String nickName) {
//		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
//		ChatServer.broadCast(nickName + "  " + time, 2, message);
//	}
//
//	/**
//	 * 错误信息响应
//	 * 
//	 * @param throwable
//	 */
//	@OnError
//	public void onError(Throwable throwable) {
//		System.out.println(throwable.getMessage());
//	}
//
//	/**
//	 * 发送或广播信息
//	 * 
//	 * @param message
//	 */
//	private static void broadCast(String sender, int type, String message) {
//		for (ChatServer chat : connections) {
//			try {
//				synchronized (chat) {
//					JSONObject json = new JSONObject();
//					json.put("type", type);
//					json.put("sender", sender);
//					json.put("msg", message);
//					chat.session.getBasicRemote().sendText(json.toJSONString());
//					logger.info(message);
//				}
//			} catch (IOException e) {
//				connections.remove(chat);
//				try {
//					chat.session.close();
//				} catch (IOException e1) {
//				}
//				ChatServer.broadCast(sender, type,
//						String.format("System> %s %s", chat.nickName, " has bean disconnection."));
//			}
//		}
//	}
//	
//	public String getOnlineList(){
//		String result="";
//		for(ChatServer chat:connections){
//			result+=chat.unameKey+",";
//		}
//		return result;
//	}
//
//	public static String getDateTimeStr() {
//		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//	}
//
//	public static String getDateStr() {
//		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//	}
//}
