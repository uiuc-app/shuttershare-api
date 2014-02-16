package com.apollo.shuttershare.web.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Daniel
 */
@Slf4j
public class GlobalInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            ModelMap map = modelAndView.getModelMap();
            map.addAttribute("_contextRoot", request.getContextPath());
        }
    }
}