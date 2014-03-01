package com.apollo.shuttershare.core.user;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            @Result(property = "udid", column = "udid")
    })
    public DeviceVO get(Long id);

    @Insert("INSERT INTO devices (device_type, udid) " +
            "VALUES (#{deviceType}, #{udid})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(DeviceVO object);

    @Update("UPDATE devices " +
            "SET " +
            "device_type = #{deviceType}, " +
            "udid = #{udid} " +
            "where id = #{id}")
    public void update(DeviceVO object);

    @Delete("DELETE FROM devices " +
            "where id = #{id}")
    public void delete(DeviceVO object);

}