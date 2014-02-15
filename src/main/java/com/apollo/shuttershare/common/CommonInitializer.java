package com.apollo.shuttershare.common;

import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Author: Daniel
 * Date: 2/10/14
 * Time: 3:15 PM
 */
@Component
public class CommonInitializer {
    @Autowired
    UserService userService;

    @PostConstruct
    public void init() {
    }
}
