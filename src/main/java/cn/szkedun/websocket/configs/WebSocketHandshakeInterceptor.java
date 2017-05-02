package cn.szkedun.websocket.configs;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import cn.szkedun.websocket.utils.UrlParamUtils;


public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {


	 
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		System.out.println("request URI :"+request.getURI());
		Map<String,String>  paramMap= UrlParamUtils.getParamsMap(request.getURI().toString());
		if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            if (session != null) {
            	for(Map.Entry<String,String> entry:paramMap.entrySet()){
            		session.setAttribute(entry.getKey(), entry.getValue());
            	}
            }else{
            	return true;
            }
        }
		
		System.out.println("beforeHandshake attributes :"+attributes);
        return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub

	}

	
}
