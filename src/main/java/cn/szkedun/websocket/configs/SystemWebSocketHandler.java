package cn.szkedun.websocket.configs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;

import cn.szkedun.websocket.utils.UrlParamUtils;

public class SystemWebSocketHandler implements WebSocketHandler {

	Logger logger = LoggerFactory.getLogger(SystemWebSocketHandler.class);

	private static final Map<String, WebSocketSession> connections = new ConcurrentHashMap<String, WebSocketSession>();

	private String nickName;

	Map<String, String> paramMap;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("connect to the websocket success......");
		paramMap = UrlParamUtils.getParamsMap(session.getUri().toString());
		this.nickName = paramMap.get("nickName");
		session.getAttributes().putAll(paramMap);
		connections.put(nickName, session);

		String msg = getOnlineList();
		JSONObject json = new JSONObject();
		json.put("type", 1);
		json.put("sender", "system");
		json.put("msg", msg);
		TextMessage message = new TextMessage(json.toJSONString());
		broadCast(message);

		json = new JSONObject();
		json.put("type", 2);
		json.put("sender", "system");
		json.put("msg", nickName + " 上线了");
		json.put("date", getDateTimeStr());
		message = new TextMessage(json.toJSONString());
		broadCast(message);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("message =>> " + message.getPayload());
		if (message.getPayload() == null || StringUtils.isBlank(message.getPayload().toString())) {
			return;
		}

		String sender = session.getAttributes().get("nickName") + "";
		JSONObject json = new JSONObject();
		json.put("sender", sender);
		json.put("msg", message.getPayload());
		json.put("type", 2);
		json.put("date", getDateTimeStr());
		TextMessage text = new TextMessage(json.toJSONString());
		if (message.getPayload().toString().matches(".*@([^:^：^\\s]+)[:：\\s]+.*")) {
			String to = message.getPayload().toString().replace(".*@([^:^：^\\s]+)[:：\\s]+.*", "$1");
			sendTo(text, to);
		} else {
			broadCast(text);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub
		connections.remove(session);
		String msg = getOnlineList();
		JSONObject json = new JSONObject();
		json.put("type", 1);
		json.put("sender", "system");
		json.put("msg", msg);
		json.put("date", getDateTimeStr());
		TextMessage message = new TextMessage(json.toJSONString());
		broadCast(message);
		logger.info(nickName + ":has disconnection.");

		json = new JSONObject();
		json.put("type", 2);
		json.put("sender", "system");
		json.put("msg", nickName + " 下线了");
		json.put("date", getDateTimeStr());
		message = new TextMessage(json.toJSONString());
		broadCast(message);

		msg = getOnlineList();
		json = new JSONObject();
		json.put("type", 1);
		json.put("sender", "system");
		json.put("msg", msg);
		message = new TextMessage(json.toJSONString());
		broadCast(message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		// TODO Auto-generated method stub
		handleTransportError(session, new Throwable("connectin closed "));
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 发送或广播信息
	 * 
	 * @param message
	 */
	private void broadCast(WebSocketMessage<?> message) {
		for (Map.Entry<String, WebSocketSession> entry : connections.entrySet()) {
			WebSocketSession chat = entry.getValue();
			try {
				chat.sendMessage(message);
				logger.info(message.getPayload().toString());
			} catch (Exception e) {
				connections.remove(chat);
				try {
					chat.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	private void sendTo(WebSocketMessage<?> message, String to) {
		WebSocketSession chat = connections.get(to);
		try {
			chat.sendMessage(message);
			logger.info(message.getPayload().toString());
		} catch (Exception e) {
			connections.remove(chat);
			try {
				chat.close();
			} catch (IOException e1) {
			}
		}
	}

	public String getOnlineList() {
		String result = "";
		for (Map.Entry<String, WebSocketSession> entry : connections.entrySet()) {
			WebSocketSession chat = entry.getValue();
			result += chat.getAttributes().get("nickName") + ",";
		}
		return result;
	}

	public static String getDateTimeStr() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getDateStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

}
