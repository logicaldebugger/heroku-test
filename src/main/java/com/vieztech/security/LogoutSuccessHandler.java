package com.vieztech.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (authentication == null) {
			System.out.println("Not Authorized Request");
		}
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		String eventBaseUrl = request.getParameter("evetnBaseUrl");
		String targetUrl = null;
		if (!StringUtils.isEmpty(eventBaseUrl)) {
			targetUrl = baseUrl + eventBaseUrl;
		} else {
			targetUrl = determineTargetUrl(baseUrl, authentication);
		}
		System.out.println("Target " + targetUrl);
		response.sendRedirect(targetUrl);
		super.onLogoutSuccess(request, response, authentication);
	}

	/*
	 * This method extracts the roles of currently logged-in user and returns
	 * appropriate URL according to his/her role.
	 */
	protected String determineTargetUrl(String baseUrl, Authentication authentication) {
		String url = "";

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}
		System.out.println(roles);

		if (isAdmin(roles)) {
			url = baseUrl + "/u/login";
		} else if (isSuperAdmin(roles)) {
			url = baseUrl + "/u/superadmin/login";
		} else {

		}

		return url;
	}

	private boolean isUser(List<String> roles) {
		if (roles.contains("ROLE_USER")) {
			return true;
		}
		return false;
	}

	private boolean isAdmin(List<String> roles) {
		if (roles.contains("ROLE_ADMIN")) {
			return true;
		}
		return false;
	}

	private boolean isSuperAdmin(List<String> roles) {
		if (roles.contains("ROLE_SUPERADMIN")) {
			return true;
		}
		return false;
	}
}
