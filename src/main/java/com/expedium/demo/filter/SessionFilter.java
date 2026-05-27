package com.expedium.demo.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class SessionFilter implements Filter
{

    @Override
	public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain)
	        throws IOException, ServletException
	{
	
	    HttpServletRequest req = (HttpServletRequest) pRequest;
	    HttpServletResponse res = (HttpServletResponse) pResponse;
	
	    String sContextPath = req.getContextPath();
	    String sUri = req.getRequestURI().substring(sContextPath.length());
	
	    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    res.setHeader("Pragma", "no-cache");
	    res.setDateHeader("Expires", 0);
	
	    if (
	            sUri.contains("Login.html") ||
	            sUri.contains("WebApi.html") ||
	            sUri.contains("SessionExpired.jsp") ||
	            sUri.startsWith("/loginController") ||
	            sUri.startsWith("/css/") ||
	            sUri.startsWith("/js/") ||
	            sUri.startsWith("/images/") ||
	            sUri.endsWith(".css") ||
	            sUri.endsWith(".js") ||
	            sUri.endsWith(".png") ||
	            sUri.endsWith(".jpg") ||
	            sUri.endsWith(".jpeg") ||
	            sUri.endsWith("favicon.ico")||
	            sUri.endsWith(".woff") ||
	            sUri.endsWith(".woff2") ||
	            sUri.endsWith(".pdf")
	       )
	    {
	        pChain.doFilter(pRequest, pResponse);
	        return;
	    }
	
	    HttpSession session = req.getSession(false);
	    boolean bIsAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
	
	    Object adminKey = (session != null) ? session.getAttribute("adminkey") : null;
	
	    if (adminKey == null)
	    {
	
	        if (bIsAjax)
	        {
	            res.setContentType("text/plain");
	            res.getWriter().write("session_expired");
	        }
	        else
	        {
	            if (session == null)
	            {
	                res.sendRedirect(sContextPath + "/Login.html");
	            }
	            else
	            {
	                res.sendRedirect(sContextPath + "/SessionExpired.jsp");
	            }
	        }
	        return;
	    }
	
	    pChain.doFilter(pRequest, pResponse);
	}
}