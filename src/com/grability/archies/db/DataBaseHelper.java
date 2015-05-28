package com.grability.archies.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final String LOG_TAG = "FitTrip-DataBaseHelper";
	private Context context;
	private static int VERSION_1 = 1;
	private static final int VERSION_2 = 2;
	
	public DataBaseHelper(Context context, String database, CursorFactory factory,
			int version) {
		super(context, database, null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CategoryEntity.CREATE_TABLE_CATEGORY);
			db.execSQL(SubCategoryEntity.CREATE_TABLE_SUBCATEGORY);
			db.execSQL(PlateEntity.CREATE_TABLE_PLATE);
			
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Error creating table:"+e.getMessage());
		}catch (Exception e) {
			Log.d(LOG_TAG, "Error creating table:"+e.getMessage());
		}
		
	}
	
	private void dropTables(SQLiteDatabase db){
		try {
			db.execSQL("DROP TABLE IF EXISTS"
					+ CategoryEntity.TABLE);
			db.execSQL("DROP TABLE IF EXISTS"
					+ SubCategoryEntity.TABLE);
			db.execSQL("DROP TABLE IF EXISTS"
					+ PlateEntity.TABLE);
			
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Error Ddroping table:"+e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.i(LOG_TAG,"Current Version:"+oldVersion+" Target Version: "+newVersion);
			int target = oldVersion +1;
			int limit = getVersionAvailable() + 1;
			boolean result = true;
			for (int version = target; version < limit; version++) {
				if (result) {
					switch (version) {
					case VERSION_2:
						UpgradeToVersion2(db);	
						break;

					default:
						break;
					}
				}
			}
			
		}
		
	}

	private void UpgradeToVersion2(SQLiteDatabase db) {
		// NO VERISOn 2 AVAILABLE YET
		
	}

	public static int getVersionAvailable() {
		
		return VERSION_1;
	}

}
