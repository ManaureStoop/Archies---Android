package com.grability.archies.db;

public class PlateEntity {
	public static final String TABLE = "plate";
	public static final String COLUMN_SYSTEM_ID = "systemId";
	public static final String COLUMN_SUBCATEGORY_ID = "suCategoryId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_IMG_PATH = "imgPath";
	public static final String COLUMN_IMG_LEFT_PATH = "leftImgPath";
	public static final String COLUMN_IMG_RIGHT_PATH = "rightImgPath";
	public static final String COLUMN_LOCAL_IMG_PATH = "localImgPath";
	public static final String COLUMN_LOCAL_IMG_LEFT_PATH = "localLeftImgPath";
	public static final String COLUMN_LOCAL_IMG_RIGHT_PATH = "localRightImgPath";
	public static final String COLUMN_ENABLED = "enabled";
	public static final String COLUMN_UPDATED_AT = "updatedAt";




	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_PLATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_SYSTEM_ID 
			+ " INTEGER PRIMARY KEY , " 
			+ COLUMN_NAME
			+ " TEXT, "
			+ COLUMN_DESCRIPTION
			+ " TEXT, "
			+ COLUMN_SUBCATEGORY_ID
			+ " TEXT, "
			+ COLUMN_IMG_PATH
			+ " TEXT, "
			+ COLUMN_IMG_LEFT_PATH
			+ " TEXT, "
			+ COLUMN_IMG_RIGHT_PATH
			+ " TEXT, "
			+ COLUMN_LOCAL_IMG_PATH
			+ " TEXT, "
			+ COLUMN_LOCAL_IMG_LEFT_PATH
			+ " TEXT, "
			+ COLUMN_LOCAL_IMG_RIGHT_PATH
			+ " TEXT, "
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_ENABLED 
			+ " INTEGER);";
}
