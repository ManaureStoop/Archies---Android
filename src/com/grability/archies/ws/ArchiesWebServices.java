package com.grability.archies.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.grability.archies.db.model.Category;
import com.grability.archies.db.model.Plate;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.db.provider.CategoryProvider;
import com.grability.archies.db.provider.PlateProvider;
import com.grability.archies.db.provider.SubCategoryProvider;
import com.grability.archies.listener.WebServiceListener;
import com.grability.archies.utils.CalendarUtil;

public class ArchiesWebServices {
	private final String LOG_TAG = "Archies-WebServices";
	private final String URL_UPDATE_CATEGORIES = "http://192.237.180.31/archies/public/category?format=json";
	private final String URL_UPDATE_SUBCATEGORIES_PLATES = "http://192.237.180.31/archies/public/category/details/";

	private final String TAG_NAME = "name";
	private final String TAG_ID = "id";
	private final String TAG_IMGPATH = "img_path";
	private final String TAG_ENABLED = "enabled";
	private final String TAG_UPDATED_AT = "updated_at";
	private final String TAG_SUBCATEGORY = "subcategory";

	private final String TAG_CATEGORY_ID = "category_id";
	private final String TAG_ADDITION_ENABLED = "addition_enable";
	private final String TAG_ITEMS = "items";

	private final String TAG_SUBCATEGORY_ID = "subcategory_id";
	private final String TAG_DESCRIPTION = "description";
	private final String TAG_LEFT_IMG_PATH = "left_img_path";
	private final String TAG_RIGHT_IMG_PATH = "right_img_path";

	WebServiceListener listener;

	public ArchiesWebServices(WebServiceListener listener) {
		this.listener = listener;
	}

	public void UpdateCategories(Context context) {

		UpdateCategoriesTask updateTask = new UpdateCategoriesTask(context);
		updateTask.execute();

	}

	public void UpdateSubcategoriesPlates(Context context, int categoryId) {

		UpdateSubcategoriesPlatesTask updateTask = new UpdateSubcategoriesPlatesTask(
				context, categoryId);
		updateTask.execute();

	}

	private class UpdateCategoriesTask extends AsyncTask<Void, Void, Void> {

		// Required initialization
		private String Content;
		private String Error = null;
		Context context;

		public UpdateCategoriesTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Void... params) {

			StringBuilder stringBuilder = new StringBuilder();
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL_UPDATE_CATEGORIES);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line;
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					inputStream.close();
				} else {
					Log.d(LOG_TAG, "Categories: error getting data");
				}

			} catch (Exception e) {
				Log.d("LOG_TAG", e.getLocalizedMessage());
				Error = e.getLocalizedMessage();
			}

			Content = stringBuilder.toString();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// NOTE: You can call UI Element here.

			if (Error != null) {

				Log.d(LOG_TAG, Error);

			} else {

				// Show Response Json On Screen (activity)
				// Log.d(LOG_TAG, Content);
				ArrayList<Category> categorys = new ArrayList<Category>();

				Date lastUpdatedCategory = CategoryProvider
						.getLastUpdate(context);

				if (lastUpdatedCategory == null) {
					Log.d(LOG_TAG, "UPDATED CATEGORY IS NULL");

					lastUpdatedCategory = CalendarUtil.getCalendarFromString(
							"0000-00-00 00:00:00", "yyy-MM-dd HH:mm:ss")
							.getTime();
				}

				try {

					JSONArray jsonMainNode = new JSONArray(Content);

					int lengthJsonArr = jsonMainNode.length();

					for (int i = 0; i < lengthJsonArr; i++) {
						Category category = new Category();

						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);

						category.setSystemId(jsonChildNode.getInt(TAG_ID));

						String dateString = jsonChildNode
								.getString((TAG_UPDATED_AT).toString());
						Calendar updatedAt = CalendarUtil
								.getCalendarFromString(dateString,
										"yyyy-MM-dd HH:mm:ss");

						if (lastUpdatedCategory.compareTo(updatedAt.getTime()) <= 0) {

							category.setName(jsonChildNode.getString((TAG_NAME)
									.toString()));
							category.setImgPath(jsonChildNode
									.getString((TAG_IMGPATH).toString()));
							category.setUpdatedAt(updatedAt);
							if (jsonChildNode.getInt(TAG_ENABLED) == 1) {
								category.setEnabled(true);
							} else {
								category.setEnabled(false);
							}

							if (CategoryProvider.readCategory(context,
									category.getSystemId()) == null) {
								CategoryProvider.insertCategory(context,
										category);
							} else {
								CategoryProvider.updateCategory(context,
										category);
							}

						}

					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
			listener.onCategoriasDone(true);
			super.onPostExecute(result);
		}

	}

	private class UpdateSubcategoriesPlatesTask extends
			AsyncTask<Void, Void, Void> {

		// Required initialization
		private String Content;
		private String Error = null;
		Context context;
		int idCategory;

		public UpdateSubcategoriesPlatesTask(Context context, int idCategory) {
			this.context = context;
			this.idCategory = idCategory;
		}

		protected void onPreExecute() {

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Void... params) {

			StringBuilder stringBuilder = new StringBuilder();
			HttpClient httpClient = new DefaultHttpClient();
			String totalURL = URL_UPDATE_SUBCATEGORIES_PLATES + idCategory;
			HttpGet httpGet = new HttpGet(totalURL);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line;
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					inputStream.close();
				} else {
					Log.d(LOG_TAG, "Subcategories: error getting data");
				}

			} catch (Exception e) {
				Log.d("LOG_TAG", e.getLocalizedMessage());
				Error = e.getLocalizedMessage();
			}

			Content = stringBuilder.toString();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// NOTE: You can call UI Element here.

			if (Error != null) {

				Log.d(LOG_TAG, Error);

			} else {

				// Show Response Json On Screen (activity)
				// Log.d(LOG_TAG, Content);

				Date lastUpdatedSubCategory = SubCategoryProvider
						.getLastUpdate(context);

				if (lastUpdatedSubCategory == null) {
					Log.d(LOG_TAG, "UPDATED SUBCATEGORY IS NULL");
					lastUpdatedSubCategory = CalendarUtil
							.getCalendarFromString("0000-00-00 00:00:00",
									"yyy-MM-dd HH:mm:ss").getTime();

				}

				Date lastUpdatedPlate = PlateProvider.getLastUpdate(context);

				if (lastUpdatedPlate == null) {
					Log.d(LOG_TAG, "UPDATED PLATE IS NULL");
					lastUpdatedPlate = new Date(0);
				}

				try {

					JSONObject jsonMainNode = new JSONObject(Content);

					JSONArray subCategorysArray = jsonMainNode
							.getJSONArray(TAG_SUBCATEGORY);

					if (subCategorysArray != null) {

						int lengthSubCategorysJsonArr = subCategorysArray
								.length();

						for (int j = 0; j < lengthSubCategorysJsonArr; j++) {

							SubCategory subCategory = new SubCategory();

							JSONObject jsonSubCategoryChildNode = subCategorysArray
									.getJSONObject(j);

							subCategory.setSystemId(jsonSubCategoryChildNode
									.getInt(TAG_ID));

							String dateStringSubCategory = jsonSubCategoryChildNode
									.getString((TAG_UPDATED_AT).toString());
							Calendar updatedAtSubCategory = CalendarUtil
									.getCalendarFromString(
											dateStringSubCategory,
											"yyyy-MM-dd HH:mm:ss");

							if (lastUpdatedSubCategory
									.compareTo(updatedAtSubCategory.getTime()) <= 0
									|| SubCategoryProvider.readSubCategory(
											context, subCategory.getSystemId()) == null) {

								subCategory.setName(jsonSubCategoryChildNode
										.getString((TAG_NAME).toString()));
								if (jsonSubCategoryChildNode
										.getInt(TAG_ENABLED) == 1) {
									subCategory.setEnabled(true);
								} else {
									subCategory.setEnabled(false);
								}

								if (jsonSubCategoryChildNode
										.getInt(TAG_ADDITION_ENABLED) == 1) {
									subCategory.setAdditionEnabled(true);
								} else {
									subCategory.setAdditionEnabled(false);
								}

								subCategory.setCategoryId(idCategory);

								subCategory.setUpdatedAt(updatedAtSubCategory);

								if (SubCategoryProvider.readSubCategory(
										context, subCategory.getSystemId()) == null) {
									SubCategoryProvider.insertSubCategory(
											context, subCategory);
								} else {
									SubCategoryProvider.updateSubCategory(
											context, subCategory);
								}

							}

							JSONArray platessArray = jsonSubCategoryChildNode
									.getJSONArray(TAG_ITEMS);

							int lengthPlatesJsonArray = platessArray.length();
							for (int h = 0; h < lengthPlatesJsonArray; h++) {

								JSONObject jsonPlateChildNode = platessArray
										.getJSONObject(h);

								Plate plate = new Plate();

								plate.setSystemId(jsonPlateChildNode
										.getInt(TAG_ID));

								String dateString = jsonPlateChildNode
										.getString((TAG_UPDATED_AT).toString());
								Calendar updatedAt = CalendarUtil
										.getCalendarFromString(dateString,
												"yyyy-MM-dd HH:mm:ss");

								if (lastUpdatedPlate.compareTo(updatedAt
										.getTime()) < 0
										|| PlateProvider.readPlate(context,
												plate.getSystemId()) == null) {

									plate.setName(jsonPlateChildNode
											.getString((TAG_NAME).toString()));
									plate.setDescription(jsonPlateChildNode
											.getString((TAG_DESCRIPTION)
													.toString()));
									plate.setSubCategoryId(jsonPlateChildNode
											.getInt((TAG_SUBCATEGORY_ID)
													.toString()));
									plate.setImgPath(jsonPlateChildNode
											.getString((TAG_IMGPATH).toString()));
									plate.setRightImgPath(jsonPlateChildNode
											.getString((TAG_RIGHT_IMG_PATH)
													.toString()));
									plate.setLeftImgPath(jsonPlateChildNode
											.getString((TAG_LEFT_IMG_PATH)
													.toString()));
									plate.setUpdatedAt(updatedAt);
									if (jsonPlateChildNode.getInt(TAG_ENABLED) == 1) {
										plate.setEnabled(true);
									} else {
										plate.setEnabled(false);
									}

									if (PlateProvider.readPlate(context,
											plate.getSystemId()) == null) {
										PlateProvider.insertPlate(context,
												plate);
									} else {
										PlateProvider.updatePlate(context,
												plate);
									}

								}

							}

						}
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
			listener.onSubCategoriasPlatesDone(true);
			super.onPostExecute(result);
		}

	}

}
