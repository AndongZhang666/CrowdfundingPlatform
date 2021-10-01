package com.dong.crowd.filter;

import com.dong.crowd.AccessPassResources;
import com.dong.crowd.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.protocol.RequestContent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CrowdAccessFilter extends ZuulFilter {

    public boolean shouldFilter() {
        System.out.println("filterCheck");

        RequestContext requestContext = RequestContext.getCurrentContext();

        HttpServletRequest request = requestContext.getRequest();

        String servletPath = request.getServletPath();

        boolean containsPublicPages = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if(containsPublicPages){
            return false;
        }

        boolean containsStaticResources = AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);

        return !containsStaticResources;
    }

    public Object run() {

        RequestContext requestContext = RequestContext.getCurrentContext();

        HttpServletRequest request = requestContext.getRequest();

        HttpSession session = request.getSession();

        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        if(loginMember == null){

            HttpServletResponse response = requestContext.getResponse();

            session.setAttribute("message", CrowdConstant.MESSAGE_ACCESS_FORBIDDEN) ;

            try {

                response.sendRedirect("/auth/member/to/login/page");

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        return null;
    }

    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }
}
