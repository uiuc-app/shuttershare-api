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
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "cityId", column = "city_id")
	})
	public PhotoVO get(Long id);

	@Insert("INSERT INTO photos (user_id, create_at, latitude, longitude, city_id) " +
			"VALUES (#{userId}, #{createAt}, #{latitude}, #{longitude}, #{cityId})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void save(PhotoVO object);

	@Update("UPDATE photos " +
			"SET " +
			"user_id = #{userId}, " +
			"create_at = #{createAt}, " +
			"latitude = #{latitude}, " +
			"longitude = #{longitude} " +
			"city_id = #{cityId} " +
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
            @Result(property = "longitude", column = "longitude"),
		    @Result(property = "cityId", column = "city_id")
    })
    List<PhotoVO> getListWithGroupId(@Param("groupId") Long groupId,
                                     @Param("limit") int limit,
                                     @Param("before") Long before,
                                     @Param("after") Long after);

	@Transactional(readOnly = true)
	@Select("SELECT photos.* FROM photos" +
			" JOIN photo_entrys ON photos.id = photo_entrys.photo_id" +
			" WHERE city_id = #{cityId}" +
			" AND photos.id > #{after} AND photos.id < #{before}" +
			" GROUP BY photos.id" +
			" ORDER BY photos.id desc" +
			" LIMIT #{limit}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "createAt", column = "create_at"),
			@Result(property = "latitude", column = "latitude"),
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "cityId", column = "city_id")
	})
	List<PhotoVO> getListWithCityId(@Param("cityId") Long cityId,
	                                @Param("limit") int limit,
	                                @Param("before") Long before,
	                                @Param("after") Long after);

	@Transactional(readOnly = true)
	@Select("SELECT photos.* FROM photos" +
			" JOIN photo_entrys ON photos.id = photo_entrys.photo_id" +
			" WHERE photos.user_id = #{userId}" +
			" AND photo_entrys.user_id = #{viewerId}" +
			" AND photos.id > #{after} AND photos.id < #{before}" +
			" GROUP BY photos.id" +
			" ORDER BY photos.id desc" +
			" LIMIT #{limit}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "createAt", column = "create_at"),
			@Result(property = "latitude", column = "latitude"),
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "cityId", column = "city_id")
	})
	List<PhotoVO> getListUserAndViewerId(@Param("userId") Long userId,
	                                     @Param("viewerId") Long viewerId,
		                                 @Param("limit") int limit,
		                                 @Param("before") Long before,
		                                 @Param("after") Long after);

	@Transactional(readOnly = true)
	@Select("SELECT photos.* FROM photos" +
			" JOIN photo_entrys ON photos.id = photo_entrys.photo_id" +
			" JOIN face_detection_logs ON photos.id = face_detection_logs.photo_id" +
			" WHERE city_id = #{cityId}" +
			" AND face_detection_logs.num_faces = 0" +
			" AND photos.id > #{after} AND photos.id < #{before}" +
			" GROUP BY photos.id" +
			" ORDER BY photos.id desc" +
			" LIMIT #{limit}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "createAt", column = "create_at"),
			@Result(property = "latitude", column = "latitude"),
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "cityId", column = "city_id")
	})
	List<PhotoVO> getListWithoutFacesWithCityId(@Param("cityId") Long cityId,
	                                            @Param("limit") int limit,
	                                            @Param("before") Long before,
	                                            @Param("after") Long after);
}