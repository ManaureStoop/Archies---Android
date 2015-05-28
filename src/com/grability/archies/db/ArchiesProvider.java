package com.grability.archies.db;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ArchiesProvider extends ContentProvider {

	private static final String LOG_TAG = "Archies-Provider";
	public static final String PROVIDER_NAME = "com.grability.archies.db.contentprovider";

	// Identifiers

	private static final int CATEGORIES = 1;
	private static final int CATEGORY_ID = 2;
	private static final int SUBCATEGORIES = 3;
	private static final int SUBCATEGORY_ID = 4;
	private static final int PLATES = 5;
	private static final int PLATE_ID = 6;


	// URIs

	private static final String CONTENT_CATEGORY = "content://" + PROVIDER_NAME
			+ "/" + CategoryEntity.TABLE;
	public static final Uri URI_CATEGORY = Uri.parse(CONTENT_CATEGORY);

	private static final String CONTENT_SUBCATEGORY = "content://" + PROVIDER_NAME
			+ "/" + SubCategoryEntity.TABLE;
	public static final Uri URI_SUBCATEGORY = Uri.parse(CONTENT_SUBCATEGORY);

	private static final String CONTENT_PLATE = "content://" + PROVIDER_NAME
			+ "/" + PlateEntity.TABLE;
	public static final Uri URI_PLATE = Uri.parse(CONTENT_PLATE);
	
	

	// Content Types

	private static final String TYPE_CATEGORY_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + CategoryEntity.TABLE;
	private static final String TYPE_CATEGORY_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + CategoryEntity.TABLE;
	private static final String TYPE_SUBCATEGORY_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + SubCategoryEntity.TABLE;
	private static final String TYPE_SUBCATEGORY_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + SubCategoryEntity.TABLE;
	private static final String TYPE_PLATE_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + PlateEntity.TABLE;
	private static final String TYPE_PLATE_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + PlateEntity.TABLE;
	

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(PROVIDER_NAME, CategoryEntity.TABLE, CATEGORIES);
		uriMatcher.addURI(PROVIDER_NAME, CategoryEntity.TABLE + "/#", CATEGORY_ID);

		uriMatcher.addURI(PROVIDER_NAME, SubCategoryEntity.TABLE, SUBCATEGORIES);
		uriMatcher.addURI(PROVIDER_NAME, SubCategoryEntity.TABLE + "/#", SUBCATEGORY_ID);

		uriMatcher.addURI(PROVIDER_NAME, PlateEntity.TABLE, PLATES);
		uriMatcher
				.addURI(PROVIDER_NAME, PlateEntity.TABLE + "/#", PLATE_ID);
		
		
	}

	public static final String DATABASE_NAME = "archies_db";
	private SQLiteDatabase db;
	private DataBaseHelper dbHelper;
	private int dataBaseVersion = 1;

	@Override
	public boolean onCreate() {
		Log.d(LOG_TAG, "- on create");
		return createDataBaseHelper();
	}

	private boolean createDataBaseHelper() {
		Context context = getContext();

		try {
			Log.i(LOG_TAG, "Conten Provider - Database verion: "
					+ dataBaseVersion);
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
					dataBaseVersion);
			return true;

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error: " + e.getMessage());
		}
		return false;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case CATEGORIES:
			return TYPE_CATEGORY_ITEMS;
		case CATEGORY_ID:
			return TYPE_CATEGORY_ITEM;
		case SUBCATEGORIES:
			return TYPE_SUBCATEGORY_ITEMS;
		case SUBCATEGORY_ID:
			return TYPE_SUBCATEGORY_ITEM;
		case PLATES:
			return TYPE_PLATE_ITEMS;
		case PLATE_ID:
			return TYPE_PLATE_ITEM;
		
		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName = null;
		Uri target = null;

		switch (uriMatcher.match(uri)) {
		
		case CATEGORIES:
			tableName = CategoryEntity.TABLE;
			target = URI_CATEGORY;
			break;

		case SUBCATEGORIES:
			tableName = SubCategoryEntity.TABLE;
			target = URI_SUBCATEGORY;
			break;

		case PLATES:
			tableName = PlateEntity.TABLE;
			target = URI_PLATE;
			break;

		
		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}

		if (tableName != null && target != null) {
			try {
				return insert(uri, values, tableName, target);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private Uri insert(Uri uri, ContentValues values, String tableName,
			Uri target) throws SQLException {
		Log.d(LOG_TAG, " - insert :" + uri);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());

		// add item
		long rowID = db.insert(tableName, "", values);

		if (rowID > 0) {
			// added successfully
			Uri itemUri = ContentUris.withAppendedId(target, rowID);
			getContext().getContentResolver().notifyChange(itemUri, null);
			return itemUri;
		}
		throw new SQLException("Failed to insert row into " + uri + " into "
				+ tableName);

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String tableName = null;
		Uri target = null;
		boolean single = false;

		switch (uriMatcher.match(uri)) {
		case CATEGORIES:
			tableName = CategoryEntity.TABLE;
			target = URI_CATEGORY;
			break;

		case CATEGORY_ID:
			tableName = CategoryEntity.TABLE;
			target = URI_CATEGORY;
			single = true;
			break;

		case SUBCATEGORIES:
			tableName = SubCategoryEntity.TABLE;
			target = URI_SUBCATEGORY;
			break;

		case SUBCATEGORY_ID:
			tableName = SubCategoryEntity.TABLE;
			target = URI_SUBCATEGORY;
			single = true;
			break;

		case PLATES:
			tableName = PlateEntity.TABLE;
			target = URI_PLATE;
			break;

		case PLATE_ID:
			tableName = PlateEntity.TABLE;
			target = URI_PLATE;
			single = true;
			break;

		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}

		if (tableName != null && target != null) {

			return query(uri, tableName, single, projection, selection,
					selectionArgs, sortOrder);

		}
		return null;
	}

	private Cursor query(Uri uri, String tableName, boolean single,
			String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		Log.d(LOG_TAG, " - query :" + uri + " Table;" + tableName);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(tableName);

		if (single) {
			sqlBuilder.appendWhere("id" + "=" + uri.getPathSegments().get(1));
		}

		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(LOG_TAG, " - update :" + uri);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());

		int count = 0;

		switch (uriMatcher.match(uri)) {

		case CATEGORIES:
			count = db.update(CategoryEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case CATEGORY_ID:
			selection = CategoryEntity.COLUMN_SYSTEM_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(CategoryEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case SUBCATEGORIES:
			count = db
					.update(SubCategoryEntity.TABLE, values, selection, selectionArgs);
			break;

		case SUBCATEGORY_ID:
			selection = SubCategoryEntity.COLUMN_CATEGORY_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(SubCategoryEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case PLATES:
			count = db.update(PlateEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case PLATE_ID:
			selection = PlateEntity.COLUMN_SYSTEM_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(PlateEntity.TABLE, values, selection,
					selectionArgs);
			break;
			

		default:
			throw new IllegalArgumentException("Unsupported URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsAffected = 0;
		Log.d(LOG_TAG, " - query :" + uri);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());
		
		String id = null;
		
		switch (uriMatcher.match(uri)) {

		case CATEGORIES:
			rowsAffected = db.delete(CategoryEntity.TABLE, selection,
					selectionArgs);
			break;

		case CATEGORY_ID:
			selection = CategoryEntity.COLUMN_SYSTEM_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(CategoryEntity.TABLE, selection,
					selectionArgs);
			break;

		case SUBCATEGORIES:
			rowsAffected = db.delete(SubCategoryEntity.TABLE, selection,
					selectionArgs);
			break;

		case SUBCATEGORY_ID:
			selection = SubCategoryEntity.COLUMN_CATEGORY_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(SubCategoryEntity.TABLE, selection,
					selectionArgs);
			break;

		case PLATES:
			rowsAffected = db.delete(PlateEntity.TABLE, selection,
					selectionArgs);
			break;

		case PLATE_ID:
			selection = PlateEntity.COLUMN_SYSTEM_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(PlateEntity.TABLE, selection,
					selectionArgs);
			break;


		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

}
