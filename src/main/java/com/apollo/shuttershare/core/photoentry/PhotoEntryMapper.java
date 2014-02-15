package com.apollo.shuttershare.core.photoentry;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
@Repository
public interface PhotoEntryMapper {
    @Transactional(readOnly = true)
    @Select("SELECT * FROM photo_entrys WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "photoId", column = "photo_id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createAt", column = "create_at"),
            @Result(property = "acknowledged", column = "acknowledged")
    })
    public PhotoEntryVO get(Long id);

    @Insert("INSERT INTO photo_entrys (photo_id, group_id, user_id, create_at, acknowledged) " +
            "VALUES (#{photoId}, #{groupId}, #{userId}, #{createAt}, #{acknowledged})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(PhotoEntryVO object);

    @Update("UPDATE photo_entrys " +
            "SET " +
            "photo_id = #{photoId}, " +
            "group_id = #{groupId}, " +
            "user_id = #{userId}, " +
            "create_at = #{createAt}, " +
            "acknowledged = #{acknowledged} " +
            "where id = #{id}")
    public void update(PhotoEntryVO object);

    @Delete("DELETE FROM photo_entrys " +
            "where id = #{id}")
    public void delete(PhotoEntryVO object);

}