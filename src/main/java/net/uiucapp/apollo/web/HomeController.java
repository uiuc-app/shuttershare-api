package net.uiucapp.apollo.web;

import net.uiucapp.apollo.common.JavaPojoToMyBatisMapperAnnotationsScript;
import net.uiucapp.apollo.common.SqlInjectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
@Slf4j
public class HomeController {

	@Autowired
	SqlInjectionMapper sqlInjectionMapper;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}

	@RequestMapping(value = "/dev/sql", method = RequestMethod.GET)
	public String sqlInterface(Model model) {
		return "sql";
	}

	@RequestMapping(value = "/dev/sql", method = RequestMethod.POST)
	public String sqlResult(@RequestParam String sql,
	                        Model model) {
		List<Map<String, Object>> rows = sqlInjectionMapper.select (sql);
		if (!rows.isEmpty()) {
			List<String> columnKeys = new ArrayList<>(rows.get(0).keySet());
			model.addAttribute("columnKeys", columnKeys);
		}

		model.addAttribute("rows", rows);
		model.addAttribute("isResult", true);
		model.addAttribute("sql", sql);
		return "sql";
	}

    @RequestMapping(value = "/dev/mapper", method = RequestMethod.GET)
    public String mapperInterface() {
        return "mapper";
    }

    @RequestMapping(value = "/dev/mapper", method = RequestMethod.POST)
    public String mapperInterface(@RequestParam MultipartFile source,
                                  Model model) {
        log.debug("source : {}", source);
        JavaPojoToMyBatisMapperAnnotationsScript script = new JavaPojoToMyBatisMapperAnnotationsScript();
        Class<?> clazz = null;
        try {
            clazz = script.compileJavaAndGetClass(new String(source.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("class : {}", clazz);

        model.addAttribute("mapperClassName", script.getMapperClassName(clazz));
        model.addAttribute("mapper", script.buildMapperClass(clazz, new HashMap<String, String>()));
        model.addAttribute("tableName", script.getTableName(clazz, new HashMap<String, String>()));
        model.addAttribute("tableSchema", script.buildMySqlSchema(clazz, new HashMap<String, String>()));
        return "mapper";
    }
}
