package cn.szkedun.websocket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomepageController {

	@RequestMapping(value={"/","/index","/home"})
	public ModelAndView home(){
		return new ModelAndView("index.jsp");
	}
}
