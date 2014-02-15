package com.apollo.shuttershare.common;

import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Daniel
 */
@Slf4j
public class JavaPojoToMyBatisMapperAnnotationsScript {
	public static void main(String[] args) {
		JavaPojoToMyBatisMapperAnnotationsScript script = new JavaPojoToMyBatisMapperAnnotationsScript();

		Class<?> clazz = script.compileJavaAndGetClass(
				"package net.uiucapp.apollo.core.test;\n" +
				"\n" +
				"import lombok.Data;\n" +
				"\n" +
				"import java.io.Serializable;\n" +
				"\n" +
				"/**\n" +
				" * @author Daniel\n" +
				" */\n" +
				"@Data\n" +
				"public class DocumentVO implements Serializable {\n" +
				"\tprivate static final long serialVersionUID = -7291703209170654261L;\n" +
				"\n" +
				"\tprivate Long id;\n" +
				"\tprivate String title;\n" +
				"\tprivate Long authorId;\n" +
				"\tprivate String authorName;\n" +
				"\tprivate String authorEmpId;\n" +
				"\tprivate String formId;\n" +
				"\tprivate Long documentSnapshotId;\n" +
				"\tprivate Long createAt;\n" +
				"\tprivate Long updateAt;\n" +
				"\tprivate Long submitAt;\n" +
				"\tprivate Integer state;\n" +
				"}\n");

		System.out.println(script.buildMapperClass(clazz, new HashMap<String, String>()));
		System.out.println(script.buildMySqlSchema(clazz, new HashMap<String, String>()));
	}

	/**
	 * @see <a href="http://stackoverflow.com/questions/2946338/how-do-i-programmatically-compile-and-instantiate-a-java-class">http://stackoverflow.com/questions/2946338/how-do-i-programmatically-compile-and-instantiate-a-java-class</a>
	 * @param source
	 * @return
	 */
	public Class<?> compileJavaAndGetClass(String source) {
		String pkgName = source.substring(source.indexOf("package") + 8, source.indexOf(";\n", source.indexOf("package"))).trim();
		log.debug("package name of the source file is {}", pkgName);

		String className = source.substring(source.indexOf("public class ") + 13, source.indexOf(" ", source.indexOf("public class ") + 13)).trim();
		log.debug("class name of the source file is {}", className);

		// Save source in .java file.
		File root = new File("/tmp/");
		log.debug("root file {}", root);
		File sourceFile = new File(root, pkgName.replaceAll("\\.", "/") + "/" + className + ".java");
		log.debug("soruce file {}", sourceFile);
		sourceFile.getParentFile().mkdirs();
		try {
			new FileWriter(sourceFile).append(source).close();
			compile(Arrays.asList(sourceFile));

			// Load and instantiate compiled class.
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()}, ClassUtils.getDefaultClassLoader());
			return Class.forName(pkgName + "." + className, true, classLoader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see <a href="http://stackoverflow.com/questions/5819376/java-compiler-api-classloader?lq=1">http://stackoverflow.com/questions/5819376/java-compiler-api-classloader?lq=1</a>
	 * @param filesToCompile
	 */
	private void compile(List<File> filesToCompile) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		StandardJavaFileManager stdFileManager =
				compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> fileObjects = stdFileManager
				.getJavaFileObjectsFromFiles(filesToCompile);

		List<String> optionList = new ArrayList<String>();
		// set compiler's classpath to be same as the runtime's
		optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path").
				concat(":").
				concat(Lombok.class.getProtectionDomain().getCodeSource().getLocation().getPath())));

		JavaCompiler.CompilationTask task = compiler.getTask(null, stdFileManager, null, optionList, null, fileObjects);
		Boolean result = task.call();
		if (result == true) {
			System.out.println("Compilation has succeeded");
		} else {
			System.out.println("Compilation FAILED");
		}
	}

	private String buildGetMethod(Class<?> clazz, Map<String, String> params) {
		String tableName = params.containsKey("tableName") ? params.get("tableName") : getTableName(clazz.getSimpleName());
		List<Map<String, String>> fieldMapList = getFieldMapList(clazz);
		Map<String, String> pkFieldMap = getPKFieldMap(clazz, params);

		StringBuilder sb = new StringBuilder();
		sb.append("@Transactional(readOnly = true)\n").
				append(String.format("@Select(\"SELECT * FROM %s WHERE %s = #{%s}\")\n", tableName, pkFieldMap.get("columnName"), pkFieldMap.get("name"))).
				append("@Results(value = {\n");

		Iterator<Map<String, String>> iterator = fieldMapList.listIterator();
		while (iterator.hasNext()) {
			Map<String, String> fieldMap = iterator.next();
			sb.append(String.format("\t\t@Result(property = \"%s\", column = \"%s\")", fieldMap.get("name"), fieldMap.get("columnName")));
			if (iterator.hasNext()) {
				sb.append(",\n");
			} else {
				sb.append("\n");
			}
		}

		sb.append("})\n").
				append(String.format("public %s get(%s %s);\n", clazz.getSimpleName(), pkFieldMap.get("type"), pkFieldMap.get("name")));
		return sb.toString();
	}

	private String buildSaveMethod(Class<?> clazz, Map<String, String> params) {
		String tableName = params.containsKey("tableName") ? params.get("tableName") : getTableName(clazz.getSimpleName());
		List<Map<String, String>> fieldMapList = getFieldMapList(clazz);
		Map<String, String> pkFieldMap = getPKFieldMap(clazz, params);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("@Insert(\"INSERT INTO %s (", tableName));

		Iterator<Map<String, String>> iterator = fieldMapList.listIterator();
		while (iterator.hasNext()) {
			Map<String, String> fieldMap = iterator.next();
			if (!pkFieldMap.get("name").equals(fieldMap.get("name"))) {
				sb.append(fieldMap.get("columnName"));
				if (iterator.hasNext()) {
					sb.append(", ");
				} else {
					sb.append(") \" +\n");
				}
			}
		}
		sb.append("\t\t\"VALUES (");

		iterator = fieldMapList.listIterator();
		while (iterator.hasNext()) {
			Map<String, String> fieldMap = iterator.next();
			if (!pkFieldMap.get("name").equals(fieldMap.get("name"))) {
				sb.append(String.format("#{%s}", fieldMap.get("name")));
				if (iterator.hasNext()) {
					sb.append(", ");
				} else {
					sb.append(")\")\n");
				}
			}
		}
		sb.append(String.format("@Options(useGeneratedKeys = true, keyProperty = \"%s\")\n", pkFieldMap.get("name"))).
				append(String.format("public void save(%s object);\n", clazz.getSimpleName()));
		return sb.toString();
	}

	private String buildUpdateMethod(Class<?> clazz, Map<String, String> params) {
		String tableName = params.containsKey("tableName") ? params.get("tableName") : getTableName(clazz.getSimpleName());
		List<Map<String, String>> fieldMapList = getFieldMapList(clazz);
		Map<String, String> pkFieldMap = getPKFieldMap(clazz, params);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("@Update(\"UPDATE %s \" +\n", tableName)).
				append("\t\t\"SET \" +\n");

		Iterator<Map<String, String>> iterator = fieldMapList.listIterator();
		while (iterator.hasNext()) {
			Map<String, String> fieldMap = iterator.next();
			if (!pkFieldMap.get("name").equals(fieldMap.get("name"))) {
				sb.append(String.format("\t\t\"%s = #{%s}", fieldMap.get("columnName"), fieldMap.get("name")));
				if (iterator.hasNext()) {
					sb.append(", \" +\n");
				} else {
					sb.append(" \" +\n");
				}
			}
		}

		sb.append(String.format("\t\t\"where %s = #{%s}\")\n", pkFieldMap.get("columnName"), pkFieldMap.get("name"))).
				append(String.format("public void update(%s object);\n", clazz.getSimpleName()));
		return sb.toString();
	}

	private String buildDeleteMethod(Class<?> clazz, Map<String, String> params) {
		String tableName = params.containsKey("tableName") ? params.get("tableName") : getTableName(clazz.getSimpleName());
		Map<String, String> pkFieldMap = getPKFieldMap(clazz, params);

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("@Delete(\"DELETE FROM %s \" +\n", tableName)).
				append(String.format("\t\t\"where %s = #{%s}\")\n", pkFieldMap.get("columnName"), pkFieldMap.get("name"))).
				append(String.format("public void delete(%s object);\n", clazz.getSimpleName()));
		return sb.toString();
	}

	public String buildMapperClass(Class<?> clazz, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.
				append(String.format("%s;", clazz.getPackage().toString())).
				append("\n").
				append("import org.apache.ibatis.annotations.*;\n").
				append("import org.springframework.stereotype.Repository;\n").
				append("import org.springframework.transaction.annotation.Transactional;\n").
				append("\n").
				append("/**\n").
				append(" *\n").
				append(" */\n").
				append("@Transactional\n").
				append("@Repository\n").
				append(String.format("public interface %s {\n", getMapperClassName(clazz))).
				append(tabify(buildGetMethod(clazz, params), 1)).
				append("\n").
				append("\n").
				append(tabify(buildSaveMethod(clazz, params), 1)).
				append("\n").
				append("\n").
				append(tabify(buildUpdateMethod(clazz, params), 1)).
				append("\n").
				append("\n").
				append(tabify(buildDeleteMethod(clazz, params), 1)).
				append("\n").
				append("\n").
				append("}\n");
		return sb.toString();
	}

	public String buildMySqlSchema(Class<?> clazz, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.
				append(String.format("DROP TABLE IF EXISTS %s;\n", getTableName(clazz, params))).
				append(String.format("CREATE TABLE %s (\n", getTableName(clazz, params)));

		Map<String, String> pkFieldMap = getPKFieldMap(clazz, params);

		Iterator<Map<String, String>> iterator = getFieldMapList(clazz).listIterator();
		while (iterator.hasNext()) {
			Map<String, String> fieldMap = iterator.next();
			String typeString;
			switch (fieldMap.get("type")) {
				case "Long":
					typeString = "BIGINT";
					break;
				case "String":
					typeString = "VARCHAR(45)";
					break;
				case "Boolean":
					typeString = "TINYINT";
					break;
				case "Integer":
					typeString = "INT";
					break;
				default:
					typeString = "#CHANGE_ME#";
			}
			if (pkFieldMap.get("name").equals(fieldMap.get("name"))) {
				// assume that primary key is of 'Long' type, and auto_incremented.
				if (fieldMap.get("type").equals("Long")) {
					sb.append(String.format("\t%s BIGINT NOT NULL AUTO_INCREMENT ,\n", pkFieldMap.get("columnName")));
				} else {
					sb.append(String.format("\t%s %s NOT NULL #CHANGE_ME# ,\n", pkFieldMap.get("columnName"), typeString));
				}
			} else {
				sb.append(String.format("\t%s %s NULL ,\n", fieldMap.get("columnName"), typeString));
			}
		}
		sb.append(String.format("\tPRIMARY KEY (%s));\n", pkFieldMap.get("columnName")));
		return sb.toString();
	}

	public String getTableName(Class<?> clazz, Map<String, String> params) {
		String tableName = params.containsKey("tableName") ? params.get("tableName") : getTableName(clazz.getSimpleName());
		return tableName;
	}

	public String getTableName(String className) {
		log.debug("Convert class name to table name : {}", className);
		//Trims trailing "VO" string.
		String result = className.substring(0, className.lastIndexOf("VO"));
		log.debug("trimmed trailing 'VO' String : {}", result);
		//Pluralize
		result = result + "s";
		log.debug("Pluralized : {}", result);
		result = camelCaseToUnderScoreAndLowerCase(result);
		log.debug("Converted Camel cases to underscores follwed by lowercase letters : {}", result);
		return result;
	}

	public String getMapperClassName(Class<?> clazz) {
		String className = clazz.getSimpleName();
		//Trims trailing "VO" string.
		String result = className.substring(0, className.lastIndexOf("VO"));
		//Concatenates "Mapper" at the end.
		result = result + "Mapper";
		return result;
	}

	private String camelCaseToUnderScoreAndLowerCase(String source) {
		log.debug("camel case to underscore + lowercase : {}", source);
		String result = source;
		boolean prevCharIsUpperCase = true;
		for (int i = 0; i < result.length(); i++) {
			char c = result.charAt(i);
			if (isUpperCase(c)) {
				if (!prevCharIsUpperCase) {
					result = result.substring(0, i) + "_" + asciiToStr(c + 32) + result.substring(i + 1);
				} else {
					result = result.substring(0, i) + asciiToStr(c + 32) + result.substring(i + 1);
				}
				prevCharIsUpperCase = true;
			} else {
				prevCharIsUpperCase = false;
			}
		}
		return result;
	}

	private boolean isUpperCase(char c) {
		return (c >= 65 && c <= 90);
	}

	private boolean isLowerCase(char c) {
		return (c >= 97 && c <= 122);
	}

	private String asciiToStr(int c) {
		return String.valueOf((char) c);
	}

	private List<Map<String, String>> getFieldMapList(Class<?> clazz) {
		List<Map<String, String>> result = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				Map<String, String> fieldMap = new HashMap<>();
				fieldMap.put("name", field.getName());
				fieldMap.put("columnName", camelCaseToUnderScoreAndLowerCase(field.getName()));
				fieldMap.put("type", field.getType().getSimpleName());
				log.debug("field : {}, column : {}", fieldMap.get("name"), fieldMap.get("columnName"));
				result.add(fieldMap);
			}
		}
		return result;
	}

	private Map<String, String> getPKFieldMap(Class<?> clazz, Map<String, String> params) {
		List<Map<String, String>> fieldMapList = getFieldMapList(clazz);
		String pkField = params.containsKey("pkField") ? params.get("pkField") : "id";
		Map<String, String> pkFieldMap = null;
		for (Map<String, String> fieldMap : fieldMapList) {
			if (fieldMap.get("name").equals(pkField)) {
				pkFieldMap = fieldMap;
				break;
			}
		}
		if (pkFieldMap == null) {
			throw new IllegalStateException("Primary Key cannot be resolved.");
		} else {
			return pkFieldMap;
		}
	}

	private String tabify(String source, int tabCount) {
		StringBuilder sb = new StringBuilder();
		String tabs = "";
		for (int i = 0; i < tabCount; i++) {
			tabs = tabs + "\t";
		}

		String[] lines = source.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			sb.append(tabs).append(line);
			if (i < lines.length - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
