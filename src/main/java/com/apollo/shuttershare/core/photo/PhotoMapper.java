package com.apollo.shuttershare.core.photo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            @Result(property = "createAt", column = "create_at")
    })
    public PhotoVO get(Long id);

    @Insert("INSERT INTO photos (user_id, create_at) " +
            "VALUES (#{userId}, #{createAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(PhotoVO object);

    @Update("UPDATE photos " +
            "SET " +
            "user_id = #{userId}, " +
            "create_at = #{createAt} " +
            "where id = #{id}")
    public void update(PhotoVO object);

    @Delete("DELETE FROM photos " +
            "where id = #{id}")
    public void delete(PhotoVO object);

}