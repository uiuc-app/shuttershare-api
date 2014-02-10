package net.uiucapp.apollo.core.user;

import net.uiucapp.apollo.common.ApolloException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Daniel
 */
@Service
@Slf4j
public class UserService {
	@Autowired
	UserMapper userMapper;

	public void saveUser(UserVO user) {
		log.debug("Save new user {} into DB.", user);
		userMapper.save(user);
	}

	public UserVO getUser(Long id) {
		log.debug("Retrieve UserVO with id {} from DB.", id);
		UserVO user = userMapper.get(id);
		if (user == null) {
			throw new ApolloException("User not found.");
		} else {
			return user;
		}
	}
}
