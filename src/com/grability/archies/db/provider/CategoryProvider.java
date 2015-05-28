package com.grability.archies.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.grability.archies.db.ArchiesProvider;
import com.grability.archies.db.CategoryEntity;
import com.grability.archies.db.model.Category;
import com.grability.archies.utils.CalendarUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class CategoryProvider {
	private static final String LOG_TAG = "Archies-CategoryProvider";

	public static final Uri URI_CATEGORY = Uri.parse("content://"
			+ ArchiesProvider.PROVIDER_NAME + "/" + CategoryEntity.TABLE);

	public static long insertCategory(Context context, Category category) {

		if (context == null || category == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(CategoryEntity.COLUMN_SYSTEM_ID, category.getSystemId());

			if (category.getName() != null) {
				values.put(CategoryEntity.COLUMN_NAME, category.getName());

			} else {
				Log.d(LOG_TAG,
						"Name is null inserting: " + category.getSystemId());
			}

			if (category.getImgPath() != null) {
				values.put(CategoryEntity.COLUMN_IMG_PATH,
						category.getImgPath());

			} else {
				Log.d(LOG_TAG,
						"ImgPath is null inserting: " + category.getSystemId());
			}
			
			if (category.getLocalImgPath() != null) {
				values.put(CategoryEntity.COLUMN_LOCAL_IMG_PATH,
						category.getLocalImgPath());

			} else {
				Log.d(LOG_TAG,
						"local ImgPath is null inserting: " + category.getSystemId());
			}

			if (category.isEnabled()) {

				values.put(CategoryEntity.COLUMN_ENABLED, 1);

			} else {
				values.put(CategoryEntity.COLUMN_ENABLED, 0);
			}

			if (category.getUpdatedAt() != null) {

				values.put(CategoryEntity.COLUMN_UPDATED_AT, category
						.getUpdatedAt().getTimeInMillis());

			} else {
				Log.d(LOG_TAG,
						"Updated at is null inserting: "
								+ category.getSystemId());
			}

			final Uri result = context.getContentResolver().insert(
					URI_CATEGORY, values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Category :" + category.getSystemId()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Category :" + category.getSystemId()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Category :" + category.getSystemId()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateCategory(Context context, Category category) {

		if (context == null || category == null)
			return false;

		try {
			ContentValues values = new ContentValues();

			values.put(CategoryEntity.COLUMN_SYSTEM_ID, category.getSystemId());

			if (category.getName() != null) {
				values.put(CategoryEntity.COLUMN_NAME, category.getName());

			} else {
				Log.d(LOG_TAG,
						"Name is null inserting: " + category.getSystemId());
			}

			if (category.getImgPath() != null) {
				values.put(CategoryEntity.COLUMN_IMG_PATH,
						category.getImgPath());

			} else {
				Log.d(LOG_TAG,
						"ImgPath is null inserting: " + category.getSystemId());
			}
			
			if (category.getLocalImgPath() != null) {
				values.put(CategoryEntity.COLUMN_LOCAL_IMG_PATH,
						category.getLocalImgPath());

			} else {
				Log.d(LOG_TAG,
						"local ImgPath is null inserting: " + category.getSystemId());
			}

			if (category.isEnabled()) {

				values.put(CategoryEntity.COLUMN_ENABLED, 1);

			} else {
				values.put(CategoryEntity.COLUMN_ENABLED, 0);
			}

			if (category.getUpdatedAt() != null) {

				values.put(CategoryEntity.COLUMN_UPDATED_AT, category
						.getUpdatedAt().getTimeInMillis());

			} else {
				Log.d(LOG_TAG,
						"Updated at is null inserting: "
								+ category.getSystemId());
			}

			String condition = CategoryEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(category.getSystemId()) + "'";

			int row = context.getContentResolver().update(URI_CATEGORY, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Category :" + category.getSystemId()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Category :" + category.getSystemId()
					+ " has not bee updated" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Category readCategory(Context context, int categoryID) {

		if (context == null)
			return null;

		String condition = CategoryEntity.COLUMN_SYSTEM_ID + " = " + "'"
				+ categoryID + "'";

		final Cursor cursor = context.getContentResolver().query(URI_CATEGORY,
				null, condition, null, null);

		Category category = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_NAME));
					final String imgPath = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_IMG_PATH));
					final String localImgPath = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_LOCAL_IMG_PATH));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					category = new Category();
					category.setSystemId(systemId);
					category.setName(name);
					category.setImgPath(imgPath);
					category.setLocalImgPath(localImgPath);
					if (enabled == 1) {
						category.setEnabled(true);

					} else {
						category.setEnabled(false);

					}
					category.setUpdatedAt(updatedAt);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			category = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return category;
	}

	public static ArrayList<Category> readCategorys(Context context) {

		if (context == null)
			return null;

		ArrayList<Category> categorys = new ArrayList<Category>();

		final Cursor cursor = context.getContentResolver().query(URI_CATEGORY,
				null, null, null, null);

		Category category = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_NAME));
					final String imgPath = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_IMG_PATH));
					final String localImgPath = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_LOCAL_IMG_PATH));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					category = new Category();
					category.setSystemId(systemId);
					category.setName(name);
					category.setImgPath(imgPath);
					category.setLocalImgPath(localImgPath);
					if (enabled == 1) {
						category.setEnabled(true);

					} else {
						category.setEnabled(false);

					}
					category.setUpdatedAt(updatedAt);

					categorys.add(category);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			categorys = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return categorys;
	}

	public static ArrayList<Category> readCategoryByEnabled(Context context) {

		if (context == null)
			return null;

		ArrayList<Category> categorys = new ArrayList<Category>();

		String condition = CategoryEntity.COLUMN_ENABLED + " = " + "'"
				+ true + "'";

		final Cursor cursor = context.getContentResolver().query(URI_CATEGORY,
				null, condition, null,null);

		Category category = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_NAME));
					final String imgPath = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_IMG_PATH));
					final String localImgPath = cursor.getString(cursor
							.getColumnIndex(CategoryEntity.COLUMN_LOCAL_IMG_PATH));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(CategoryEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					category = new Category();
					category.setSystemId(systemId);
					category.setName(name);
					category.setImgPath(imgPath);
					category.setLocalImgPath(localImgPath);
					if (enabled == 1) {
						category.setEnabled(true);

					} else {
						category.setEnabled(false);

					}
					category.setUpdatedAt(updatedAt);


					categorys.add(category);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			categorys = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return categorys;
	}

	public static boolean removeCategory(Context context, int categoryId) {

		try {
			String condition = CategoryEntity.COLUMN_SYSTEM_ID + " = "
					+ String.valueOf(categoryId);
			int rows = context.getContentResolver().delete(URI_CATEGORY,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Category : " + categoryId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting category: " + e.getMessage());
		}
		return false;
	}

	

	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(URI_CATEGORY,
				null, null, null, CategoryEntity.COLUMN_UPDATED_AT + " DESC");

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				final long updated_at = cursor.getLong(cursor
						.getColumnIndex(CategoryEntity.COLUMN_UPDATED_AT));
				Date date = new Date(updated_at);
				Log.d(LOG_TAG,
						"last update "
								+ CalendarUtil.getDateFormated(date,
										"dd MM yyy mm:ss"));

				return date;
			}

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}

		return null;
	}

}
