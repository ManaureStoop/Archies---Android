package com.grability.archies.db;

public class SubCategoryEntity {
	public static final String TABLE = "subCategory";
	public static final String COLUMN_SYSTEM_ID = "systemId";
	public static final String COLUMN_CATEGORY_ID = "categoryId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ADDITION_ENABLED = "additionEnabled";
	public static final String COLUMN_ENABLED = "enabled";
	public static final String COLUMN_UPDATED_AT = "updatedAt";




	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_SUBCATEGORY = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_SYSTEM_ID 
			+ " INTEGER PRIMARY KEY , " 
			+ COLUMN_CATEGORY_ID
			+ " INTEGER, "
			+ COLUMN_NAME
			+ " TEXT, "
			+ COLUMN_ADDITION_ENABLED
			+ " INTEGER, "
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_ENABLED 
			+ " INTEGER);";
}
