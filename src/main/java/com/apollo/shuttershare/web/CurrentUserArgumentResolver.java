package com.apollo.shuttershare.web;

import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * User: Daniel
 * Date: 2/10/14
 * Time: 3:02 PM
 */
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserVO.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String apiKey = webRequest.getParameter("api_key");
        if (apiKey == null) {
            throw new UnauthorizedException("api_key is not provided on secured api");
        } else {
            UserVO currentUser = userService.getUserForApiKey(apiKey);
            return currentUser;
        }
    }
}
