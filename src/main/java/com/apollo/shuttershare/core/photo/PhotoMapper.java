package com.apollo.shuttershare.core.photo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional
@Repository
public interface PhotoMapper {
    @Transactional(readOnly = true)
    @Select("SELECT * FROM photos WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createAt", column = "create_at"),
            @Result(property = "latitude", column = "latitude"),
            @Result(property = "longitude", column = "longitude")
    })
    public PhotoVO get(Long id);

    @Insert("INSERT INTO photos (user_id, create_at, latitude, longitude) " +
            "VALUES (#{userId}, #{createAt}, #{latitude}, #{longitude})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(PhotoVO object);

    @Update("UPDATE photos " +
            "SET " +
            "user_id = #{userId}, " +
            "create_at = #{createAt}, " +
            "latitude = #{latitude}, " +
            "longitude = #{longitude} " +
            "where id = #{id}")
    public void update(PhotoVO object);

    @Delete("DELETE FROM photos " +
            "where id = #{id}")
    public void delete(PhotoVO object);

    @Transactional(readOnly = true)
    @Select("SELECT photos.* FROM photos" +
            " JOIN photo_entrys ON photos.id = photo_entrys.photo_id" +
            " WHERE group_id = #{groupId}" +
            " AND photos.id > #{after} AND photos.id < #{before}" +
            " GROUP BY photos.id" +
            " ORDER BY photos.id desc" +
            " LIMIT #{limit}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createAt", column = "create_at"),
            @Result(property = "latitude", column = "latitude"),
            @Result(property = "longitude", column = "longitude")
    })
    List<PhotoVO> getListWithGroupId(@Param("groupId") Long groupId,
                                     @Param("limit") Long limit,
                                     @Param("before") Long before,
                                     @Param("after") Long after);
}