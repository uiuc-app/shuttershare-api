package com.apollo.shuttershare.core.city;
import com.apollo.shuttershare.web.CityElements;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional
@Repository
public interface CityMapper {
	@Transactional(readOnly = true)
	@Select("SELECT * FROM citys WHERE id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "name", column = "name"),
			@Result(property = "latitude", column = "latitude"),
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "county", column = "county"),
			@Result(property = "stateAbbreviation", column = "state_abbreviation"),
			@Result(property = "state", column = "state")
	})
	public CityVO get(Long id);

	@Insert("INSERT INTO citys (name, latitude, longitude, county, state_abbreviation, state) " +
			"VALUES (#{name}, #{latitude}, #{longitude}, #{county}, #{stateAbbreviation}, #{state})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void save(CityVO object);

	@Update("UPDATE citys " +
			"SET " +
			"name = #{name}, " +
			"latitude = #{latitude}, " +
			"longitude = #{longitude}, " +
			"county = #{county}, " +
			"state_abbreviation = #{stateAbbreviation}, " +
			"state = #{state} " +
			"where id = #{id}")
	public void update(CityVO object);

	@Delete("DELETE FROM citys " +
			"where id = #{id}")
	public void delete(CityVO object);

	@Transactional(readOnly = true)
	@Select("SELECT * FROM citys WHERE " +
			"latitude > (#{latitude, javaType=double, jdbcType=DOUBLE} - #{threshold, javaType=double, jdbcType=DOUBLE}) " +
			"AND latitude < (#{latitude, javaType=double, jdbcType=DOUBLE} + #{threshold, javaType=double, jdbcType=DOUBLE}) " +
			"AND longitude > (#{longitude, javaType=double, jdbcType=DOUBLE} - #{threshold, javaType=double, jdbcType=DOUBLE}) " +
			"AND longitude < (#{longitude, javaType=double, jdbcType=DOUBLE} + #{threshold, javaType=double, jdbcType=DOUBLE})")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "name", column = "name"),
			@Result(property = "latitude", column = "latitude"),
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "county", column = "county"),
			@Result(property = "stateAbbreviation", column = "state_abbreviation"),
			@Result(property = "state", column = "state")
	})
	public List<CityVO> getClosestListHeuristic(@Param("latitude") double latitude,
	                                            @Param("longitude") double longitude,
	                                            @Param("threshold") double threshold);

	@Transactional(readOnly = true)
	@Select("SELECT citys.* FROM citys " +
			"JOIN photos ON citys.id = photos.city_id " +
			"JOIN photo_entrys ON photos.id = photo_entrys.photo_id " +
			"WHERE photo_entrys.user_id = #{userId} " +
			"GROUP BY citys.id " +
			"ORDER BY ABS(#{latitude} - citys.latitude)*ABS(#{latitude} - citys.latitude) + ABS(#{longitude} - citys.longitude)*ABS(#{longitude} - citys.longitude) " +
			"LIMIT 10")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "name", column = "name"),
			@Result(property = "latitude", column = "latitude"),
			@Result(property = "longitude", column = "longitude"),
			@Result(property = "county", column = "county"),
			@Result(property = "stateAbbreviation", column = "state_abbreviation"),
			@Result(property = "state", column = "state"),
	})
	public List<CityVO> getClosestCitiesWithPhotosForUser(@Param("userId") Long userId,
                                                          @Param("latitude") double latitude,
                                                          @Param("longitude") double longitude);

}