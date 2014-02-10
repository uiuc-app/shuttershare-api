package net.uiucapp.apollo.common;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel
 */
@Transactional
@Repository
public interface SqlInjectionMapper {
	@Transactional(readOnly = true)
	@Select("${sql}")
	public List<Map<String, Object>> select(@Param("sql") String sql);
}
