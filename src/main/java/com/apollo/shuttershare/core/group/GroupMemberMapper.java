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
public interface GroupMemberMapper {
    @Transactional(readOnly = true)
    @Select("SELECT * FROM group_members WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "joinAt", column = "join_at")
    })
    public GroupMemberVO get(Long id);

    @Insert("INSERT INTO group_members (group_id, user_id, join_at) " +
            "VALUES (#{groupId}, #{userId}, #{joinAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(GroupMemberVO object);

    @Update("UPDATE group_members " +
            "SET " +
            "group_id = #{groupId}, " +
            "user_id = #{userId}, " +
            "join_at = #{joinAt} " +
            "where id = #{id}")
    public void update(GroupMemberVO object);

    @Delete("DELETE FROM group_members " +
            "where id = #{id}")
    public void delete(GroupMemberVO object);

    @Transactional(readOnly = true)
    @Select("SELECT * FROM group_members WHERE group_id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "joinAt", column = "join_at")
    })
    List<GroupMemberVO> getListWithGroup(GroupVO group);

    @Transactional(readOnly = true)
    @Select("SELECT * FROM group_members WHERE user_id = #{userId} " +
            "AND group_id = #{groupId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "joinAt", column = "join_at")
    })
    List<GroupMemberVO> getListWithGroupIdAndUserId(@Param("groupId") Long groupId,
                                                    @Param("userId") Long userId);
}