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
public interface UserMapper {
    @Transactional(readOnly = true)
    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "jointAt", column = "joint_at"),
            @Result(property = "apiKey", column = "api_key"),
            @Result(property = "deviceType", column = "device_type"),
            @Result(property = "udid", column = "udid")
    })
    public UserVO get(Long id);

    @Insert("INSERT INTO users (name, joint_at, api_key, device_type, udid) " +
            "VALUES (#{name}, #{jointAt}, #{apiKey}, #{deviceType}, #{udid})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(UserVO object);

    @Update("UPDATE users " +
            "SET " +
            "name = #{name}, " +
            "joint_at = #{jointAt}, " +
            "api_key = #{apiKey}, " +
            "device_type = #{deviceType}, " +
            "udid = #{udid} " +
            "where id = #{id}")
    public void update(UserVO object);

    @Delete("DELETE FROM users " +
            "where id = #{id}")
    public void delete(UserVO object);

    @Transactional(readOnly = true)
    @Select("SELECT * FROM users WHERE api_key = #{apiKey}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "jointAt", column = "joint_at"),
            @Result(property = "apiKey", column = "api_key"),
            @Result(property = "deviceType", column = "device_type"),
            @Result(property = "udid", column = "udid")
    })
    List<UserVO> getWithApiKey(String apiKey);
}