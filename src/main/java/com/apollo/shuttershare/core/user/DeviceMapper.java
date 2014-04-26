package com.apollo.shuttershare.core.user;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional
@Repository
public interface DeviceMapper {
	@Transactional(readOnly = true)
	@Select("SELECT * FROM devices WHERE id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "udid", column = "udid"),
			@Result(property = "userId", column = "user_id")
	})
	public DeviceVO get(Long id);

	@Insert("INSERT INTO devices (device_type, udid, user_id) " +
			"VALUES (#{deviceType}, #{udid}, #{userId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void save(DeviceVO object);

	@Update("UPDATE devices " +
			"SET " +
			"device_type = #{deviceType}, " +
			"udid = #{udid} " +
			"user_id = #{userId} " +
			"where id = #{id}")
	public void update(DeviceVO object);

	@Delete("DELETE FROM devices " +
			"where id = #{id}")
	public void delete(DeviceVO object);

	@Transactional(readOnly = true)
	@Select("SELECT * FROM devices WHERE user_id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "udid", column = "udid"),
			@Result(property = "userId", column = "user_id")
	})
	List<DeviceVO> getListForUser(Long id);
}