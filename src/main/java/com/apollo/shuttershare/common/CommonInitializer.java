package com.apollo.shuttershare.common;

import com.apollo.shuttershare.core.group.GroupMemberVO;
import com.apollo.shuttershare.core.group.GroupService;
import com.apollo.shuttershare.core.group.GroupVO;
import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${profileinfo.currentprofile}")
	private String currentProfile;

    @PostConstruct
    public void init() {
	    if (currentProfile.equals("dev")) {
		    UserVO user1 = new UserVO();
		    user1.setApiKey("test_api_key_0");
		    user1.setName("Yosub Shin");
		    user1.setJointAt(System.currentTimeMillis());
		    userService.saveUser(user1);

		    UserVO user2 = new UserVO();
		    user2.setApiKey("test_api_key_1");
		    user2.setName("Joe Benassi");
		    user2.setJointAt(System.currentTimeMillis());
		    userService.saveUser(user2);

		    UserVO user3 = new UserVO();
		    user3.setApiKey("test_api_key_2");
		    user3.setName("Zac Swedberg");
		    user3.setJointAt(System.currentTimeMillis());
		    userService.saveUser(user3);

		    GroupVO group = new GroupVO();
		    group.setCreatedAt(System.currentTimeMillis());
		    group.setName("Test group 0");
		    group.setPassPhrase("test_passphrase_0");
		    groupService.saveGroup(group);
		    groupService.joinGroup(group, user1);
		    groupService.joinGroup(group, user2);
		    groupService.joinGroup(group, user3);

		    group = new GroupVO();
		    group.setCreatedAt(System.currentTimeMillis());
		    group.setName("Test group 1");
		    group.setPassPhrase("test_passphrase_1");
		    groupService.saveGroup(group);
		    groupService.joinGroup(group, user1);
		    groupService.joinGroup(group, user2);
		    groupService.joinGroup(group, user3);

		    group = new GroupVO();
		    group.setCreatedAt(System.currentTimeMillis());
		    group.setName("Test group 2");
		    group.setPassPhrase("test_passphrase_2");
		    groupService.saveGroup(group);
		    groupService.joinGroup(group, user1);
		    groupService.joinGroup(group, user2);
	    }
    }
}
