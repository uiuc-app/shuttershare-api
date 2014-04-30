package com.apollo.shuttershare.core.facedetect;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
@Repository
public interface FaceMapper {
	@Transactional(readOnly = true)
	@Select("SELECT * FROM faces WHERE id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "photoId", column = "photo_id"),
			@Result(property = "faceIndex", column = "face_index"),
			@Result(property = "x", column = "x"),
			@Result(property = "y", column = "y"),
			@Result(property = "width", column = "width"),
			@Result(property = "height", column = "height")
	})
	public FaceVO get(String id);

	@Insert("INSERT INTO faces (photo_id, face_index, x, y, width, height) " +
			"VALUES (#{photoId}, #{faceIndex}, #{x}, #{y}, #{width}, #{height})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void save(FaceVO object);

	@Update("UPDATE faces " +
			"SET " +
			"photo_id = #{photoId}, " +
			"face_index = #{faceIndex}, " +
			"x = #{x}, " +
			"y = #{y}, " +
			"width = #{width}, " +
			"height = #{height} " +
			"where id = #{id}")
	public void update(FaceVO object);

	@Delete("DELETE FROM faces " +
			"where id = #{id}")
	public void delete(FaceVO object);

}
