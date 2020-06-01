package com.yzl.study.db2rest.utils;

public class TypeMapper {

	@SuppressWarnings("rawtypes")
	public static Class getMappedType(String mysqlType) {

		String typeLower = mysqlType.toUpperCase();

		switch (typeLower) {
		case "BIGINT":
			return java.lang.Long.class;
		case "BINARY":
			return java.lang.Byte[].class;
		case "BIT":
			return java.lang.Integer.class;
		case "BLOB":
			return java.lang.Byte[].class;
		case "BOOLEAN":
			return java.lang.Boolean.class;
		case "CHAR":
			return java.lang.String.class;
		case "CLOB":
			return java.lang.String.class;
		case "DATE":
			return java.util.Date.class;
		case "DECIMAL":
			return java.lang.Double.class;
		case "DOUBLE":
			return java.lang.Double.class;
		case "FLOAT":
			return java.lang.Double.class;
		case "INTEGER":
			return java.lang.Integer.class;
		case "INT":
			return java.lang.Integer.class;
		case "JAVA_OBJECT":
			return java.lang.Object.class;
		case "LONGNVARCHAR":
			return java.lang.String.class;
		case "LONGVARBINARY":
			return java.lang.Byte[].class;
		case "LONGVARCHAR":
			return java.lang.String.class;
		case "NUMERIC":
			return java.lang.Double.class;
		case "NCHAR":
			return java.lang.String.class;
		case "NCLOB":
			return java.lang.String.class;
		case "NVARCHAR":
			return java.lang.String.class;
		case "OTHER":
			return java.lang.Object.class;
		case "REAL":
			return java.lang.Float.class;
		case "SMALLINT":
			return java.lang.Integer.class;
		case "TIME":
			return java.util.Date.class;
		case "TIMESTAMP":
			return java.util.Date.class;
		case "TINYINT":
			return java.lang.Byte.class;
		case "VARCHAR":
			return java.lang.String.class;
		case "VARBINARY":
			return java.lang.Byte[].class;
		case "TEXT":
			return java.lang.String.class;
		case "DATETIME":
			return java.util.Date.class;
		default:
			return null;
		// 语句
		}

	}

}
