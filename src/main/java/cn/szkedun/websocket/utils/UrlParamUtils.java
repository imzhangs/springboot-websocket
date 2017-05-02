package cn.szkedun.websocket.utils;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.util.UriEncoder;

public class UrlParamUtils {


	public static Map<String,String> getParamsMap(String uri){
		Map<String,String> paramMap=new HashMap<String,String>();
		if(uri.contains("?") && uri.contains("=")){
			uri=uri.substring(uri.indexOf("?")+1);
			for(String params:uri.split("&")){
				paramMap.put(params.split("=")[0], UriEncoder.decode(params.split("=")[1]));
			}
		}
		return paramMap;
	}
	
	public static void main(String[] args) {
		System.out.println("@aaaaa:::：：  dfsss".replaceAll("@([^:^：^\\s]+)[:：\\s]+.*","$1"));
	}
}
