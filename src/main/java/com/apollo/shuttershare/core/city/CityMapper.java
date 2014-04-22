package com.apollo.shuttershare.core.city;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

}