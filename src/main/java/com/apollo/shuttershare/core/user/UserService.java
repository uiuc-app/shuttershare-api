package com.apollo.shuttershare.core.user;

import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.group.GroupVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Daniel
 */
@Service
@Slf4j
@Transactional
public class UserService {
	@Autowired
	UserMapper userMapper;

	public void saveUser(UserVO user) {
		log.debug("Saving new user {} into DB.", user);
		userMapper.save(user);
	}

	public UserVO getUser(Long id) {
		log.debug("Retrieving UserVO with id {} from DB.", id);
		UserVO user = userMapper.get(id);
		if (user == null) {
			throw new ShutterShareException("User not found.");
		} else {
			return user;
		}
	}

    public UserVO getUserForApiKey(String apiKey) {
        log.debug("Retrieving UserVO with apiKey {}", apiKey);
        List<UserVO> users = userMapper.getWithApiKey(apiKey);
        if (users.size() == 1) {
            return users.get(0);
        } else {
            throw new UnauthorizedException("Cannot find unique user with given api key");
        }
    }

    public List<UserVO> getUsersWithGroup(GroupVO group) {
        log.debug("Getting UserVOs for the group members of the group {}", group);
        return userMapper.getListWithGroupId(group.getId());
    }

	public UserVO createNewUser(String name) {
		log.debug("Create a new user with name {}", name);
		UserVO user = new UserVO();
		user.setApiKey(UUID.randomUUID().toString());
		user.setName(name);
		user.setJointAt(System.currentTimeMillis());
		userMapper.save(user);
		return user;
	}
}
