package com.vieztech.controllers.site;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@RequestMapping(path={"/", ""})
	public ModelAndView index(Model model) {
		System.out.println("here");
		return new ModelAndView("index");
	}
}
