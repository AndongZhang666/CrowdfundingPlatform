package com.dong.crowd;

import java.util.HashSet;
import java.util.Set;

/**
    Define the accessible resources, including
    1. public pages
    2. static resources
 */

public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>();

    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message");
        PASS_RES_SET.add("/project/get/project/detail/**");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath) {
        System.out.println(servletPath);

        if(servletPath == null || servletPath.length() == 0) {

            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        String[] split = servletPath.split("/");

        String firstLevelPath = split[1];

        return STATIC_RES_SET.contains(firstLevelPath);
    }

}
