package com.apollo.shuttershare.core.user;

import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.common.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Daniel
 */
@Service
@Slf4j
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
}