package com.grability.archies.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.util.Log;

import com.grability.archies.db.model.Category;
import com.grability.archies.db.model.Plate;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.db.provider.CategoryProvider;
import com.grability.archies.db.provider.PlateProvider;
import com.grability.archies.listener.ImagessUpdaterListener;
import com.grability.archies.utils.FileUtil;

public class ImagesUpdater {
	private final String LOG_TAG = "Archies-ImageUpdater";
	private final String URL_BASE = "http://192.237.180.31/archies/public/";
	ImagessUpdaterListener listener;
	int pendingImages;

	public ImagesUpdater(ImagessUpdaterListener listener) {
		this.listener = listener;
	}

	public void UpdateCategorysCoversIfNeeded(Context context,
			ArrayList<Category> categories) {

		pendingImages = 0;
		for (Category category : categories) {
			// if (category.getLocalImgPath() == null) {
			if (!FileUtil.imageExists(category.getImgPath().replace("/", ""),
					context)) {
				pendingImages++;
				DownloadCategoryImage dCategoryCover = new DownloadCategoryImage(
						context, category);
				dCategoryCover.execute();
			}
			// }
		}

	}

	public void UpdateCategoryCoverIfNeeded(Context context, Category category) {

		if (!FileUtil.imageExists(category.getImgPath().replace("/", ""),
				context)) {
			pendingImages++;
			DownloadCategoryImage dCategoryAudio = new DownloadCategoryImage(
					context, category);
			dCategoryAudio.execute();
		} else {
			listener.onCategoryImagesDone(false);
		}

	}

	public void UpdatePlatesImagesIfNeeded(Context context,
			ArrayList<SubCategory> subCategories) {
		pendingImages = 0;
		for (SubCategory subCategory : subCategories) {
			ArrayList<Plate> plates = PlateProvider.readPlateBySubCategory(
					context, subCategory.getSystemId());

			for (Plate plate : plates) {

				// if (plate.getLocalImgPath() == null) {
				if (!FileUtil.imageExists(plate.getImgPath().replace("/", ""),
						context)) {
					pendingImages++;
					DownloadPlateImage dCPlateCover = new DownloadPlateImage(
							context, plate);
					dCPlateCover.execute();
				}
				// }

				// if (plate.getLocalLeftImgPath() == null) {
				if (!FileUtil.imageExists(
						plate.getLeftImgPath().replace("/", ""), context)) {
					pendingImages++;
					DownloadPlateImageLeft dCPlateCover1 = new DownloadPlateImageLeft(
							context, plate);
					dCPlateCover1.execute();
				}
				// }

				// if (plate.getLocalRightImgPath() == null) {
				if (!FileUtil.imageExists(
						plate.getRightImgPath().replace("/", ""), context)) {
					pendingImages++;
					DownloadPlateImageRight dCPlateCover2 = new DownloadPlateImageRight(
							context, plate);
					dCPlateCover2.execute();
				}
				// }
			}

		}

	}

	private class DownloadCategoryImage extends AsyncTask<Void, Void, Void> {

		Context context;
		Category category;
		String localPath;
		boolean result;

		public DownloadCategoryImage(Context context, Category category) {
			this.context = context;
			this.category = category;

			ContextWrapper ctxw = new ContextWrapper(context);
			File homeFile = ctxw.getFilesDir();
			String filesDir = homeFile.getAbsolutePath();

			localPath = filesDir + "/" + category.getImgPath().replace("/", "");
			result = false;
		}

		protected void onPreExecute() {

			// Start Progress Dialog (Message)

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Void... params) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(URL_BASE + category.getImgPath());
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Log.d(LOG_TAG,
							"Server returned HTTP "
									+ connection.getResponseCode() + " "
									+ connection.getResponseMessage());
				}

				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(localPath);

				byte data[] = new byte[4096];
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}

					Log.d(LOG_TAG, "File lenght: " + fileLength);
					// publishing the progress....
					// if (fileLength > 0) // only if total length is known

					output.write(data, 0, count);
				}
				result = true;
			} catch (Exception e) {
				Log.d(LOG_TAG,
						"Exception while downloading image from category: "
								+ category.getName() + " : " + e.toString());
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
					Log.d(LOG_TAG,
							"Exception while closing downloaded cover from category: "
									+ category.getName() + " : "
									+ ignored.toString());
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void results) {
			if (result) {
				Log.d(LOG_TAG,
						" downloaded cover from category: "
								+ category.getName() + " : " + localPath);
				category.setLocalImgPath(category.getImgPath().replace("/", ""));
				CategoryProvider.updateCategory(context, category);

				File newfile = new File(localPath);
				if (newfile.exists()) {
					Log.d(LOG_TAG, String.valueOf(newfile.length()));
				} else {
					Log.d(LOG_TAG, "Cover not exists!!");
				}

			} else {
				Log.d(LOG_TAG, " NOT downloaded cover from category: "
						+ category.getName() + " : " + localPath);
			}
			categoriesCoversDownloaded();
		}

	}

	private class DownloadPlateImage extends AsyncTask<Void, Void, Void> {

		Context context;
		Plate plate;
		String localPath;
		boolean result;

		public DownloadPlateImage(Context context, Plate plate) {
			this.context = context;
			this.plate = plate;

			ContextWrapper ctxw = new ContextWrapper(context);
			File homeFile = ctxw.getFilesDir();
			String filesDir = homeFile.getAbsolutePath();

			localPath = filesDir + "/" + plate.getImgPath().replace("/", "");
			result = false;
		}

		protected void onPreExecute() {

			// Start Progress Dialog (Message)

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Void... params) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(URL_BASE + plate.getImgPath());
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Log.d(LOG_TAG,
							"Server returned HTTP "
									+ connection.getResponseCode() + " "
									+ connection.getResponseMessage());
				}

				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(localPath);

				byte data[] = new byte[4096];
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}

					Log.d(LOG_TAG, "File lenght: " + fileLength);
					// publishing the progress....
					// if (fileLength > 0) // only if total length is known

					output.write(data, 0, count);
				}
				result = true;
			} catch (Exception e) {
				Log.d(LOG_TAG, "Exception while downloading image from plate: "
						+ plate.getName() + " : " + e.toString());
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
					Log.d(LOG_TAG,
							"Exception while closing downloaded cover from plate: "
									+ plate.getName() + " : "
									+ ignored.toString());
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void results) {
			if (result) {
				Log.d(LOG_TAG,
						" downloaded cover from plate: " + plate.getName()
								+ " : " + localPath);
				plate.setLocalImgPath(plate.getImgPath().replace("/", ""));
				PlateProvider.updatePlate(context, plate);

				File newfile = new File(localPath);
				if (newfile.exists()) {
					Log.d(LOG_TAG, String.valueOf(newfile.length()));
				} else {
					Log.d(LOG_TAG, "Cover not exists!!");
				}

			} else {
				Log.d(LOG_TAG,
						" NOT downloaded cover from plate: " + plate.getName()
								+ " : " + localPath);
			}
			platesCoversDownloaded();
		}

	}

	private class DownloadPlateImageLeft extends AsyncTask<Void, Void, Void> {

		Context context;
		Plate plate;
		String localPath;
		boolean result;

		public DownloadPlateImageLeft(Context context, Plate plate) {
			this.context = context;
			this.plate = plate;

			ContextWrapper ctxw = new ContextWrapper(context);
			File homeFile = ctxw.getFilesDir();
			String filesDir = homeFile.getAbsolutePath();

			localPath = filesDir + "/"
					+ plate.getLeftImgPath().replace("/", "");
			result = false;
		}

		protected void onPreExecute() {

			// Start Progress Dialog (Message)

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Void... params) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(URL_BASE + plate.getLeftImgPath());
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Log.d(LOG_TAG,
							"Server returned HTTP "
									+ connection.getResponseCode() + " "
									+ connection.getResponseMessage());
				}

				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(localPath);

				byte data[] = new byte[4096];
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}

					Log.d(LOG_TAG, "File lenght: " + fileLength);
					// publishing the progress....
					// if (fileLength > 0) // only if total length is known

					output.write(data, 0, count);
				}
				result = true;
			} catch (Exception e) {
				Log.d(LOG_TAG, "Exception while downloading image from plate: "
						+ plate.getName() + " : " + e.toString());
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
					Log.d(LOG_TAG,
							"Exception while closing downloaded cover from plate: "
									+ plate.getName() + " : "
									+ ignored.toString());
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void results) {
			if (result) {
				Log.d(LOG_TAG,
						" downloaded cover from plate: " + plate.getName()
								+ " : " + localPath);
				plate.setLocalLeftImgPath(plate.getLeftImgPath().replace("/",
						""));
				PlateProvider.updatePlate(context, plate);

				File newfile = new File(localPath);
				if (newfile.exists()) {
					Log.d(LOG_TAG, String.valueOf(newfile.length()));
				} else {
					Log.d(LOG_TAG, "Cover not exists!!");
				}

			} else {
				Log.d(LOG_TAG,
						" NOT downloaded cover from plate: " + plate.getName()
								+ " : " + localPath);
			}
			platesCoversDownloaded();
		}

	}

	private class DownloadPlateImageRight extends AsyncTask<Void, Void, Void> {

		Context context;
		Plate plate;
		String localPath;
		boolean result;

		public DownloadPlateImageRight(Context context, Plate plate) {
			this.context = context;
			this.plate = plate;

			ContextWrapper ctxw = new ContextWrapper(context);
			File homeFile = ctxw.getFilesDir();
			String filesDir = homeFile.getAbsolutePath();

			localPath = filesDir + "/"
					+ plate.getRightImgPath().replace("/", "");
			result = false;
		}

		protected void onPreExecute() {

			// Start Progress Dialog (Message)

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(Void... params) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(URL_BASE + plate.getRightImgPath());
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Log.d(LOG_TAG,
							"Server returned HTTP "
									+ connection.getResponseCode() + " "
									+ connection.getResponseMessage());
				}

				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(localPath);

				byte data[] = new byte[4096];
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}

					Log.d(LOG_TAG, "File lenght: " + fileLength);
					// publishing the progress....
					// if (fileLength > 0) // only if total length is known

					output.write(data, 0, count);
				}
				result = true;
			} catch (Exception e) {
				Log.d(LOG_TAG, "Exception while downloading image from plate: "
						+ plate.getName() + " : " + e.toString());
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
					Log.d(LOG_TAG,
							"Exception while closing downloaded cover from plate: "
									+ plate.getName() + " : "
									+ ignored.toString());
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void results) {
			if (result) {
				Log.d(LOG_TAG,
						" downloaded cover from plate: " + plate.getName()
								+ " : " + localPath);
				plate.setLocalRightImgPath(plate.getRightImgPath().replace("/",
						""));
				PlateProvider.updatePlate(context, plate);

				File newfile = new File(localPath);
				if (newfile.exists()) {
					Log.d(LOG_TAG, String.valueOf(newfile.length()));
				} else {
					Log.d(LOG_TAG, "Cover not exists!!");
				}

			} else {
				Log.d(LOG_TAG,
						" NOT downloaded cover from plate: " + plate.getName()
								+ " : " + localPath);
			}
			platesCoversDownloaded();
		}

	}

	public void categoriesCoversDownloaded() {

		pendingImages--;
		if (pendingImages == 0) {
			listener.onCategoryImagesDone(true);
		}

	}

	public void platesCoversDownloaded() {

		pendingImages--;
		if (pendingImages == 0) {
			listener.onPlatesImagesDone(true);
		}

	}
}
