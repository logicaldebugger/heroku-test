package com.vieztech.controllers.site;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {
	private static final String PATH = "/error";
	private final ErrorAttributes errorAttributes;
	@Value("true")
	private boolean debug;

	@Autowired
	public ErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping(path = { "", "/" })
	public String getPage(Model model, HttpServletRequest request, HttpServletResponse response) {
		String path;
		switch (response.getStatus()) {
		case 500:
			model.addAttribute("errors", getErrorAttributes(request, debug));
		case 403:
		case 404:
			path = "" + response.getStatus();
			break;
		default:
			path = "/error";
		}
		return path;
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
