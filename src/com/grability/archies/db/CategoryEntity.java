package com.grability.archies.db;

public class CategoryEntity {
	public static final String TABLE = "category";
	public static final String COLUMN_SYSTEM_ID = "systemId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_IMG_PATH = "imgPath";
	public static final String COLUMN_LOCAL_IMG_PATH = "localImgPath";
	public static final String COLUMN_ENABLED = "enabled";
	public static final String COLUMN_UPDATED_AT = "updatedAt";




	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_SYSTEM_ID 
			+ " INTEGER PRIMARY KEY , " 
			+ COLUMN_NAME
			+ " TEXT, "
			+ COLUMN_IMG_PATH
			+ " TEXT, "
			+ COLUMN_LOCAL_IMG_PATH
			+ " TEXT, "
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_ENABLED 
			+ " INTEGER);";
}
