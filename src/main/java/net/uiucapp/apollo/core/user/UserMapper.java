package net.uiucapp.apollo.core.user;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
@Repository
public interface UserMapper {
	@Transactional(readOnly = true)
	@Select("SELECT * FROM users WHERE id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "name", column = "name"),
			@Result(property = "email", column = "email"),
			@Result(property = "imageUrl", column = "image_url"),
	})
	public UserVO get(Long id);

	@Insert("INSERT INTO users (name, email, image_url) " +
			"VALUES (#{name}, #{email}, #{imageUrl})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void save(UserVO object);

	@Update("UPDATE users " +
			"SET " +
			"name = #{name}, " +
			"email = #{email}, " +
			"image_url = #{imageUrl}, " +
			"provider_id = #{providerId}, " +
			"provider_user_id = #{providerUserId} " +
			"where id = #{id}")
	public void update(UserVO object);

	@Delete("DELETE FROM users " +
			"where id = #{id}")
	public void delete(UserVO object);

}