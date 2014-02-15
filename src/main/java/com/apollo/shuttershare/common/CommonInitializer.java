package com.apollo.shuttershare.common;

import com.apollo.shuttershare.core.group.GroupMemberVO;
import com.apollo.shuttershare.core.group.GroupService;
import com.apollo.shuttershare.core.group.GroupVO;
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

    @Autowired
    GroupService groupService;

    @PostConstruct
    public void init() {
        UserVO user = new UserVO();
        user.setApiKey("test_api_key");
        user.setName("Yosub Shin");
        user.setJointAt(System.currentTimeMillis());
        user.setDeviceType("Test device OS/Type");
        user.setUdid("Test udid");

        userService.saveUser(user);


        GroupVO group = new GroupVO();
        group.setCreatedAt(System.currentTimeMillis());
        group.setName("Test group 1");
        group.setPassPhrase("test-group-passphrase");
        groupService.saveGroup(group);

        groupService.joinGroup(group, user);

        group = new GroupVO();
        group.setCreatedAt(System.currentTimeMillis());
        group.setName("Test group 2");
        group.setPassPhrase("test-group-passphrase");
        groupService.saveGroup(group);
    }
}
