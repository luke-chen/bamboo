package com.luke.cms.cookies;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {

	public static Cookie createUUID(String cookieName, String domain) {
		Cookie cookie = new Cookie(cookieName, UUID.randomUUID().toString());
		cookie.setDomain(domain);
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setPath("/");
		return cookie;
	}

	public static String getValue(Cookie[] cookies, String cookieName) {
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(cookieName))
					return cookie.getValue();
		return null;
	}

	public static boolean deleteCookie(Cookie[] cookies, String cookieName, String domain, HttpServletResponse response) {
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(cookieName)) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					cookie.setDomain(domain);
					response.addCookie(cookie);
					return true;
				}
		return false;
	}
}