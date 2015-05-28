package com.grability.archies.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.grability.archies.db.ArchiesProvider;
import com.grability.archies.db.CategoryEntity;
import com.grability.archies.db.PlateEntity;
import com.grability.archies.db.model.Plate;
import com.grability.archies.utils.CalendarUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class PlateProvider {
	private static final String LOG_TAG = "Archies-PlateProvider";

	public static final Uri URI_PLATE = Uri.parse("content://"
			+ ArchiesProvider.PROVIDER_NAME + "/" + PlateEntity.TABLE);

	public static long insertPlate(Context context, Plate plate) {

		if (context == null || plate == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(PlateEntity.COLUMN_SYSTEM_ID, plate.getSystemId());
			values.put(PlateEntity.COLUMN_SUBCATEGORY_ID, plate.getSubCategoryId());

			if (plate.getName() != null) {
				values.put(PlateEntity.COLUMN_NAME, plate.getName());

			} else {
				Log.d(LOG_TAG,
						"Name is null inserting: " + plate.getSystemId());
			}

			if (plate.getDescription() != null) {
				values.put(PlateEntity.COLUMN_DESCRIPTION, plate.getDescription());

			} else {
				Log.d(LOG_TAG,
						"Description is null inserting: " + plate.getSystemId());
			}


			if (plate.isEnabled()) {

				values.put(PlateEntity.COLUMN_ENABLED, 1);

			} else {
				values.put(PlateEntity.COLUMN_ENABLED, 0);
			}
			


			if (plate.getImgPath() != null) {
				values.put(CategoryEntity.COLUMN_IMG_PATH,
						plate.getImgPath());

			} else {
				Log.d(LOG_TAG,
						"ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getRightImgPath() != null) {
				values.put(PlateEntity.COLUMN_IMG_RIGHT_PATH,
						plate.getRightImgPath());

			} else {
				Log.d(LOG_TAG,
						"Right ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getLeftImgPath() != null) {
				values.put(PlateEntity.COLUMN_IMG_LEFT_PATH,
						plate.getLeftImgPath());

			} else {
				Log.d(LOG_TAG,
						"Left ImgPath is null inserting: " + plate.getSystemId());
			}
			
			if (plate.getLocalImgPath() != null) {
				values.put(CategoryEntity.COLUMN_LOCAL_IMG_PATH,
						plate.getLocalImgPath());

			} else {
				Log.d(LOG_TAG,
						"Local ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getLocalRightImgPath() != null) {
				values.put(PlateEntity.COLUMN_LOCAL_IMG_RIGHT_PATH,
						plate.getLocalRightImgPath());

			} else {
				Log.d(LOG_TAG,
						"Local Right ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getLocalLeftImgPath() != null) {
				values.put(PlateEntity.COLUMN_LOCAL_IMG_LEFT_PATH,
						plate.getLocalLeftImgPath());

			} else {
				Log.d(LOG_TAG,
						"Local Left ImgPath is null inserting: " + plate.getSystemId());
			}

			if (plate.getUpdatedAt() != null) {

				values.put(PlateEntity.COLUMN_UPDATED_AT, plate
						.getUpdatedAt().getTimeInMillis());

			} else {
				Log.d(LOG_TAG,
						"Updated at is null inserting: "
								+ plate.getSystemId());
			}

			final Uri result = context.getContentResolver().insert(
					URI_PLATE, values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Plate :" + plate.getSystemId()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Plate :" + plate.getSystemId()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Plate :" + plate.getSystemId()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updatePlate(Context context, Plate plate) {

		if (context == null || plate == null)
			return false;

		try {
			ContentValues values = new ContentValues();

			values.put(PlateEntity.COLUMN_SYSTEM_ID, plate.getSystemId());
			values.put(PlateEntity.COLUMN_SUBCATEGORY_ID, plate.getSubCategoryId());

			if (plate.getName() != null) {
				values.put(PlateEntity.COLUMN_NAME, plate.getName());

			} else {
				Log.d(LOG_TAG,
						"Name is null inserting: " + plate.getSystemId());
			}

			if (plate.getDescription() != null) {
				values.put(PlateEntity.COLUMN_DESCRIPTION, plate.getDescription());

			} else {
				Log.d(LOG_TAG,
						"Description is null inserting: " + plate.getSystemId());
			}


			if (plate.isEnabled()) {

				values.put(PlateEntity.COLUMN_ENABLED, 1);

			} else {
				values.put(PlateEntity.COLUMN_ENABLED, 0);
			}
			


			if (plate.getImgPath() != null) {
				values.put(CategoryEntity.COLUMN_IMG_PATH,
						plate.getImgPath());

			} else {
				Log.d(LOG_TAG,
						"ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getRightImgPath() != null) {
				values.put(PlateEntity.COLUMN_IMG_RIGHT_PATH,
						plate.getRightImgPath());

			} else {
				Log.d(LOG_TAG,
						"Right ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getLeftImgPath() != null) {
				values.put(PlateEntity.COLUMN_IMG_LEFT_PATH,
						plate.getLeftImgPath());

			} else {
				Log.d(LOG_TAG,
						"Left ImgPath is null inserting: " + plate.getSystemId());
			}
			
			if (plate.getLocalImgPath() != null) {
				values.put(CategoryEntity.COLUMN_LOCAL_IMG_PATH,
						plate.getLocalImgPath());

			} else {
				Log.d(LOG_TAG,
						"Local ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getLocalRightImgPath() != null) {
				values.put(PlateEntity.COLUMN_LOCAL_IMG_RIGHT_PATH,
						plate.getLocalRightImgPath());

			} else {
				Log.d(LOG_TAG,
						"Local Right ImgPath is null inserting: " + plate.getSystemId());
			}
			if (plate.getLocalLeftImgPath() != null) {
				values.put(PlateEntity.COLUMN_LOCAL_IMG_LEFT_PATH,
						plate.getLocalLeftImgPath());

			} else {
				Log.d(LOG_TAG,
						"Local Left ImgPath is null inserting: " + plate.getSystemId());
			}

			if (plate.getUpdatedAt() != null) {

				values.put(PlateEntity.COLUMN_UPDATED_AT, plate
						.getUpdatedAt().getTimeInMillis());

			} else {
				Log.d(LOG_TAG,
						"Updated at is null inserting: "
								+ plate.getSystemId());
			}

			String condition = PlateEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(plate.getSystemId()) + "'";

			int row = context.getContentResolver().update(URI_PLATE, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Plate :" + plate.getSystemId()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Plate :" + plate.getSystemId()
					+ " has not bee updated" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Plate readPlate(Context context, int i) {

		if (context == null)
			return null;

		String condition = PlateEntity.COLUMN_SYSTEM_ID + " = " + "'"
				+ i + "'";

		final Cursor cursor = context.getContentResolver().query(URI_PLATE,
				null, condition, null, null);

		Plate plate = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_SYSTEM_ID));
					final int subCategoryId = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_SUBCATEGORY_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_NAME));
					final String description = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_DESCRIPTION));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_UPDATED_AT));
					final String imgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_PATH));
					final String rightImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_RIGHT_PATH));
					final String leftImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_LEFT_PATH));
					final String localImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_PATH));
					final String localRightImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_RIGHT_PATH));
					final String localLeftImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_LEFT_PATH));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					plate = new Plate();
					plate.setSystemId(systemId);
					plate.setSubCategoryId(subCategoryId);
					plate.setName(name);
					plate.setDescription(description);
					plate.setImgPath(imgPath);
					plate.setLeftImgPath(leftImgPath);
					plate.setRightImgPath(rightImgPath);
					plate.setLocalImgPath(localImgPath);
					plate.setLocalLeftImgPath(localLeftImgPath);
					plate.setLocalRightImgPath(localRightImgPath);
					if (enabled == 1) {
						plate.setEnabled(true);

					} else {
						plate.setEnabled(false);

					}
					
					plate.setUpdatedAt(updatedAt);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			plate = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return plate;
	}

	public static ArrayList<Plate> readPlates(Context context) {

		if (context == null)
			return null;

		ArrayList<Plate> plates = new ArrayList<Plate>();

		final Cursor cursor = context.getContentResolver().query(URI_PLATE,
				null, null, null, null);

		Plate plate = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_SYSTEM_ID));
					final int subCategoryId = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_SUBCATEGORY_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_NAME));
					final String description = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_DESCRIPTION));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_UPDATED_AT));
					final String imgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_PATH));
					final String rightImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_RIGHT_PATH));
					final String leftImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_LEFT_PATH));
					final String localImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_PATH));
					final String localRightImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_RIGHT_PATH));
					final String localLeftImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_LEFT_PATH));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					plate = new Plate();
					plate.setSystemId(systemId);
					plate.setSubCategoryId(subCategoryId);
					plate.setName(name);
					plate.setDescription(description);
					plate.setImgPath(imgPath);
					plate.setLeftImgPath(leftImgPath);
					plate.setRightImgPath(rightImgPath);
					plate.setLocalImgPath(localImgPath);
					plate.setLocalLeftImgPath(localLeftImgPath);
					plate.setLocalRightImgPath(localRightImgPath);
					if (enabled == 1) {
						plate.setEnabled(true);

					} else {
						plate.setEnabled(false);

					}
					
					plate.setUpdatedAt(updatedAt);
					plates.add(plate);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			plates = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return plates;
	}

	public static ArrayList<Plate> readPlateBySubCategory(Context context, int subCategoryId) {

		if (context == null)
			return null;

		ArrayList<Plate> plates = new ArrayList<Plate>();

		String condition = PlateEntity.COLUMN_SUBCATEGORY_ID + " = " + 
				+ subCategoryId + " AND "+PlateEntity.COLUMN_ENABLED+ " = " + 1;

		final Cursor cursor = context.getContentResolver().query(URI_PLATE,
				null, condition, null,null);

		Plate plate = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final int systemId = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_NAME));
					final String description = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_DESCRIPTION));
					final int enabled = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(PlateEntity.COLUMN_UPDATED_AT));
					final String imgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_PATH));
					final String rightImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_RIGHT_PATH));
					final String leftImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_IMG_LEFT_PATH));
					final String localImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_PATH));
					final String localRightImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_RIGHT_PATH));
					final String localLeftImgPath = cursor.getString(cursor
							.getColumnIndex(PlateEntity.COLUMN_LOCAL_IMG_LEFT_PATH));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					plate = new Plate();
					plate.setSystemId(systemId);
					plate.setSubCategoryId(subCategoryId);
					plate.setName(name);
					plate.setDescription(description);
					plate.setImgPath(imgPath);
					plate.setLeftImgPath(leftImgPath);
					plate.setRightImgPath(rightImgPath);
					plate.setLocalImgPath(localImgPath);
					plate.setLocalLeftImgPath(localLeftImgPath);
					plate.setLocalRightImgPath(localRightImgPath);
					if (enabled == 1) {
						plate.setEnabled(true);

					} else {
						plate.setEnabled(false);

					}
					
					plate.setUpdatedAt(updatedAt);
					plates.add(plate);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			plates = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return plates;
	}

	public static boolean removePlate(Context context, int plateId) {

		try {
			String condition = PlateEntity.COLUMN_SYSTEM_ID + " = "
					+ String.valueOf(plateId);
			int rows = context.getContentResolver().delete(URI_PLATE,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Plate : " + plateId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting plate: " + e.getMessage());
		}
		return false;
	}

	

	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(URI_PLATE,
				null, null, null, PlateEntity.COLUMN_UPDATED_AT + " DESC");

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				final long updated_at = cursor.getLong(cursor
						.getColumnIndex(PlateEntity.COLUMN_UPDATED_AT));
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
