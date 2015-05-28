package com.grability.archies.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.grability.archies.db.ArchiesProvider;
import com.grability.archies.db.SubCategoryEntity;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.utils.CalendarUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SubCategoryProvider {
	private static final String LOG_TAG = "Archies-SSubCategoryProvider";

	public static final Uri URI_SUBCATEGORY = Uri.parse("content://"
			+ ArchiesProvider.PROVIDER_NAME + "/" + SubCategoryEntity.TABLE);

	public static long insertSubCategory(Context context,
			SubCategory subCategory) {

		if (context == null || subCategory == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(SubCategoryEntity.COLUMN_SYSTEM_ID,
					subCategory.getSystemId());
			values.put(SubCategoryEntity.COLUMN_CATEGORY_ID,
					subCategory.getCategoryId());

			if (subCategory.getName() != null) {
				values.put(SubCategoryEntity.COLUMN_NAME, subCategory.getName());

			} else {
				Log.d(LOG_TAG,
						"Name is null inserting: " + subCategory.getSystemId());
			}

			if (subCategory.isEnabled()) {

				values.put(SubCategoryEntity.COLUMN_ENABLED, 1);

			} else {
				values.put(SubCategoryEntity.COLUMN_ENABLED, 0);
			}

			if (subCategory.isAdditionEnabled()) {

				values.put(SubCategoryEntity.COLUMN_ADDITION_ENABLED, 1);

			} else {
				values.put(SubCategoryEntity.COLUMN_ADDITION_ENABLED, 0);
			}

			if (subCategory.getUpdatedAt() != null) {

				values.put(SubCategoryEntity.COLUMN_UPDATED_AT, subCategory
						.getUpdatedAt().getTimeInMillis());

			} else {
				Log.d(LOG_TAG,
						"Updated at is null inserting: "
								+ subCategory.getSystemId());
			}

			final Uri result = context.getContentResolver().insert(
					URI_SUBCATEGORY, values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " SubCategory :" + subCategory.getSystemId()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " SubCategory :" + subCategory.getSystemId()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " SubCategory :" + subCategory.getSystemId()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateSubCategory(Context context,
			SubCategory subCategory) {

		if (context == null || subCategory == null)
			return false;

		try {
			ContentValues values = new ContentValues();

			values.put(SubCategoryEntity.COLUMN_SYSTEM_ID,
					subCategory.getSystemId());
			values.put(SubCategoryEntity.COLUMN_CATEGORY_ID,
					subCategory.getCategoryId());

			if (subCategory.getName() != null) {
				values.put(SubCategoryEntity.COLUMN_NAME, subCategory.getName());

			} else {
				Log.d(LOG_TAG,
						"Name is null inserting: " + subCategory.getSystemId());
			}

			if (subCategory.isEnabled()) {

				values.put(SubCategoryEntity.COLUMN_ENABLED, 1);

			} else {
				values.put(SubCategoryEntity.COLUMN_ENABLED, 0);
			}

			if (subCategory.isAdditionEnabled()) {

				values.put(SubCategoryEntity.COLUMN_ADDITION_ENABLED, 1);

			} else {
				values.put(SubCategoryEntity.COLUMN_ADDITION_ENABLED, 0);
			}

			if (subCategory.getUpdatedAt() != null) {

				values.put(SubCategoryEntity.COLUMN_UPDATED_AT, subCategory
						.getUpdatedAt().getTimeInMillis());

			} else {
				Log.d(LOG_TAG,
						"Updated at is null inserting: "
								+ subCategory.getSystemId());
			}
			String condition = SubCategoryEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(subCategory.getSystemId()) + "'";

			int row = context.getContentResolver().update(URI_SUBCATEGORY,
					values, condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " SubCategory :" + subCategory.getSystemId()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " SubCategory :" + subCategory.getSystemId()
					+ " has not bee updated" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static SubCategory readSubCategory(Context context, int i) {

		if (context == null)
			return null;

		String condition = SubCategoryEntity.COLUMN_SYSTEM_ID + " = " + "'" + i
				+ "'";

		final Cursor cursor = context.getContentResolver().query(
				URI_SUBCATEGORY, null, condition, null, null);

		SubCategory subCategory = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_SYSTEM_ID));
					final int categoryId = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_CATEGORY_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SubCategoryEntity.COLUMN_NAME));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(SubCategoryEntity.COLUMN_ENABLED));
					final int additionEnabled = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_ADDITION_ENABLED));
					final long updated_at = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					subCategory = new SubCategory();
					subCategory.setSystemId(systemId);
					subCategory.setCategoryId(categoryId);
					subCategory.setName(name);
					if (enabled == 1) {
						subCategory.setEnabled(true);

					} else {
						subCategory.setEnabled(false);

					}
					if (additionEnabled == 1) {
						subCategory.setAdditionEnabled(true);

					} else {
						subCategory.setAdditionEnabled(false);

					}
					subCategory.setUpdatedAt(updatedAt);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			subCategory = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return subCategory;
	}

	public static ArrayList<SubCategory> readSubCategorys(Context context) {

		if (context == null)
			return null;

		ArrayList<SubCategory> subCategorys = new ArrayList<SubCategory>();

		final Cursor cursor = context.getContentResolver().query(
				URI_SUBCATEGORY, null, null, null, null);

		SubCategory subCategory = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_SYSTEM_ID));
					final int categoryId = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_CATEGORY_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SubCategoryEntity.COLUMN_NAME));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(SubCategoryEntity.COLUMN_ENABLED));
					final int additionEnabled = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_ADDITION_ENABLED));
					final long updated_at = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					subCategory = new SubCategory();
					subCategory.setSystemId(systemId);
					subCategory.setCategoryId(categoryId);
					subCategory.setName(name);
					if (enabled == 1) {
						subCategory.setEnabled(true);

					} else {
						subCategory.setEnabled(false);

					}
					if (additionEnabled == 1) {
						subCategory.setAdditionEnabled(true);

					} else {
						subCategory.setAdditionEnabled(false);

					}
					subCategory.setUpdatedAt(updatedAt);

					subCategorys.add(subCategory);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			subCategorys = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return subCategorys;
	}

	public static ArrayList<SubCategory> readSubCategoryByCategory(
			Context context, int categoryId) {

		if (context == null)
			return null;

		ArrayList<SubCategory> subCategorys = new ArrayList<SubCategory>();

		String condition = SubCategoryEntity.COLUMN_CATEGORY_ID + " = " + " "
				+ categoryId + " AND " + SubCategoryEntity.COLUMN_ENABLED
				+ " = " + 1;

		final Cursor cursor = context.getContentResolver().query(
				URI_SUBCATEGORY, null, condition, null, null);

		SubCategory subCategory = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SubCategoryEntity.COLUMN_NAME));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(SubCategoryEntity.COLUMN_ENABLED));
					final int additionEnabled = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_ADDITION_ENABLED));
					final long updated_at = cursor
							.getInt(cursor
									.getColumnIndex(SubCategoryEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					subCategory = new SubCategory();
					subCategory.setSystemId(systemId);
					subCategory.setCategoryId(categoryId);
					subCategory.setName(name);
					if (enabled == 1) {
						subCategory.setEnabled(true);

					} else {
						subCategory.setEnabled(false);

					}
					if (additionEnabled == 1) {
						subCategory.setAdditionEnabled(true);

					} else {
						subCategory.setAdditionEnabled(false);

					}
					subCategory.setUpdatedAt(updatedAt);

					subCategorys.add(subCategory);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			subCategorys = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return subCategorys;
	}

	public static boolean removeSubCategory(Context context, int subCategoryId) {

		try {
			String condition = SubCategoryEntity.COLUMN_SYSTEM_ID + " = "
					+ String.valueOf(subCategoryId);
			int rows = context.getContentResolver().delete(URI_SUBCATEGORY,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "SubCategory : " + subCategoryId
						+ "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting subCategory: " + e.getMessage());
		}
		return false;
	}

	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(
				URI_SUBCATEGORY, null, null, null,
				SubCategoryEntity.COLUMN_UPDATED_AT + " DESC");

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				final long updated_at = cursor.getLong(cursor
						.getColumnIndex(SubCategoryEntity.COLUMN_UPDATED_AT));
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
