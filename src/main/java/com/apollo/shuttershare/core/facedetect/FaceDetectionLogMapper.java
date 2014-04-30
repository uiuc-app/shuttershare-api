package com.apollo.shuttershare.core.facedetect;
import com.apollo.shuttershare.core.photo.PhotoVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional
@Repository
public interface FaceDetectionLogMapper {
	@Transactional(readOnly = true)
	@Select("SELECT * FROM face_detection_logs WHERE id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "photoId", column = "photo_id"),
			@Result(property = "createAt", column = "create_at"),
			@Result(property = "numFaces", column = "num_faces")
	})
	public FaceDetectionLogVO get(Long id);

	@Insert("INSERT INTO face_detection_logs (photo_id, create_at, num_faces) " +
			"VALUES (#{photoId}, #{createAt}, #{numFaces})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void save(FaceDetectionLogVO object);

	@Update("UPDATE face_detection_logs " +
			"SET " +
			"photo_id = #{photoId}, " +
			"create_at = #{createAt}, " +
			"num_faces = #{numFaces} " +
			"where id = #{id}")
	public void update(FaceDetectionLogVO object);

	@Delete("DELETE FROM face_detection_logs " +
			"where id = #{id}")
	public void delete(FaceDetectionLogVO object);

	@Transactional(readOnly = true)
	@Select("SELECT photos.* FROM photos" +
			" LEFT JOIN face_detection_logs ON photos.id = face_detection_logs.photo_id" +
			" WHERE face_detection_logs.id IS NULL" +
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
	List<PhotoVO> getUnprocessedPhotos(int limit);
}