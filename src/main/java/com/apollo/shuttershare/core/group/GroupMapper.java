package com.apollo.shuttershare.core.group;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional
@Repository
public interface GroupMapper {
    @Transactional(readOnly = true)
    @Select("SELECT * FROM groups WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "passPhrase", column = "pass_phrase")
    })
    public GroupVO get(Long id);

    @Insert("INSERT INTO groups (name, created_at, pass_phrase) " +
            "VALUES (#{name}, #{createdAt}, #{passPhrase})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(GroupVO object);

    @Update("UPDATE groups " +
            "SET " +
            "name = #{name}, " +
            "created_at = #{createdAt}, " +
            "pass_phrase = #{passPhrase} " +
            "where id = #{id}")
    public void update(GroupVO object);

    @Delete("DELETE FROM groups " +
            "where id = #{id}")
    public void delete(GroupVO object);

    @Transactional(readOnly = true)
    @Select("SELECT groups.* FROM groups" +
            " JOIN group_members ON groups.id = group_members.group_id" +
            " WHERE user_id = #{userId}" +
            " GROUP BY groups.id")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "passPhrase", column = "pass_phrase")
    })
    List<GroupVO> getListWithUserId(Long userId);

    @Transactional(readOnly = true)
    @Select("SELECT * FROM groups WHERE pass_phrase = #{passPhrase}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "passPhrase", column = "pass_phrase")
    })
    List<GroupVO> getListWithPassPhrase(String passPhrase);
}
