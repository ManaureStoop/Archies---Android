package com.grability.archies;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grability.archies.db.model.Category;
import com.grability.archies.db.model.Plate;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.db.provider.CategoryProvider;
import com.grability.archies.db.provider.PlateProvider;
import com.grability.archies.db.provider.SubCategoryProvider;
import com.grability.archies.listener.ImagessUpdaterListener;
import com.grability.archies.listener.WebServiceListener;
import com.grability.archies.utils.CalendarUtil;
import com.grability.archies.utils.FileUtil;
import com.grability.archies.utils.FontUtil;
import com.grability.archies.ws.ArchiesWebServices;
import com.grability.archies.ws.ImagesUpdater;

public class PlatesDetailActivity extends Activity implements
		WebServiceListener, ImagessUpdaterListener {
	private static final String LOG_TAG = "Archies-Plate Details- Activity";

	// Main Variables
	ArchiesWebServices wsProviderArchiesWebServices;
	int categoryCountDown;
	int plateId;
	Plate plate;
	boolean leftisMsing;
	boolean rightMissing;
	// Views
	RelativeLayout actionBarLayout;
	TextView plateTitle;
	TextView plateDescription;
	ImageView plateImgLeft;
	ImageView plateImgRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide ActionBar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setActionBar();

		setContentView(R.layout.activity_plate_detail);

		setOrientation();

		Intent intent = getIntent();
		plateId = intent.getExtras().getInt("plateId");
		plate = PlateProvider.readPlate(this, plateId);

		loadViews();
		setFonts();

	}

	private void setFonts() {
		plateTitle.setTypeface((FontUtil.getTypeface(this,
				FontUtil.HELVETICA_NEUE_LIGHT)));
		plateDescription.setTypeface((FontUtil.getTypeface(this,
				FontUtil.HELVETICA_NEUE_LIGHT)));

	}

	private void setOrientation() {
		if (getResources().getBoolean(R.bool.landscape_only)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	private void setActionBar() {
		// Inflate your custom layout
		actionBarLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.actionbar, null);

		// Set up your ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);
		actionBar.show();

	}

	private void loadViews() {
		plateTitle = (TextView) findViewById(R.id.textView_plate_detail_title);
		plateDescription = (TextView) findViewById(R.id.textView_plate_detail_description);
		plateImgLeft = (ImageView) findViewById(R.id.imageView_plate_detail_left);
		plateImgRight = (ImageView) findViewById(R.id.imageView_plate_detail_right);

		if (plate.getName() != null) {
			plateTitle.setText(plate.getName().substring(0, 1).toUpperCase()
					+ plate.getName().substring(1).toLowerCase());
		}
		if (plate.getDescription() != null) {
			plateDescription.setText(plate.getDescription());
		}

		leftisMsing = false;
		rightMissing = false;

		if (plate.getLocalLeftImgPath() != null) {
			if (FileUtil.imageExists(plate.getLocalLeftImgPath(), this)) {
				File filePath = getFileStreamPath(plate.getLocalLeftImgPath());
				plateImgLeft.setImageDrawable(Drawable.createFromPath(filePath
						.toString()));
			} else {
				leftisMsing = true;
				plateImgLeft
						.setBackgroundResource(R.drawable.animation_loading);
				plateImgLeft.post(new Runnable() {

					@Override
					public void run() {
						AnimationDrawable frameAnimation = (AnimationDrawable) plateImgLeft
								.getBackground();
						frameAnimation.start();
					}
				});

			}
		} else {
			leftisMsing = true;
			plateImgLeft.setBackgroundResource(R.drawable.animation_loading);
			plateImgLeft.post(new Runnable() {

				@Override
				public void run() {
					AnimationDrawable frameAnimation = (AnimationDrawable) plateImgLeft
							.getBackground();
					frameAnimation.start();
				}
			});

		}

		if (plate.getLocalRightImgPath() != null) {
			if (FileUtil.imageExists(plate.getLocalRightImgPath(), this)) {
				File filePath = getFileStreamPath(plate.getLocalRightImgPath());
				plateImgRight.setImageDrawable(Drawable.createFromPath(filePath
						.toString()));
			} else {
				rightMissing = true;
				plateImgRight
						.setBackgroundResource(R.drawable.animation_loading);
				plateImgRight.post(new Runnable() {

					@Override
					public void run() {
						AnimationDrawable frameAnimation = (AnimationDrawable) plateImgRight
								.getBackground();
						frameAnimation.start();
					}
				});

			}
		} else {
			rightMissing = true;
			plateImgRight.setBackgroundResource(R.drawable.animation_loading);
			plateImgRight.post(new Runnable() {

				@Override
				public void run() {
					AnimationDrawable frameAnimation = (AnimationDrawable) plateImgRight
							.getBackground();
					frameAnimation.start();
				}
			});

		}

		if (rightMissing && leftisMsing) {
			if (!getResources().getBoolean(R.bool.landscape_only)) {
				plateImgRight.setVisibility(View.GONE);
			}
		}

	}

	private void updateSubCategories() {

		ArrayList<Category> categories = CategoryProvider.readCategorys(this);

		if (categories != null) {
			categoryCountDown = categories.size();

			String st = " ";

			for (Category category : categories) {

				wsProviderArchiesWebServices.UpdateSubcategoriesPlates(this,
						category.getSystemId());

				// Calendar udp = category.getUpdatedAt();
				// String date = CalendarUtil.getDateFormated(udp,
				// "yyyy-MM-dd");
				// st = st + "\n " + category.getName() + " "
				// + category.getSystemId() + " " + category.getImgPath()
				// + " " + date;

			}
		} else {

		}
	}

	private void showsubcategories() {
		ArrayList<SubCategory> categories = SubCategoryProvider
				.readSubCategoryByCategory(this, plateId);
		if (categories != null) {
			String st = " ";
			for (SubCategory category : categories) {
				Calendar udp = category.getUpdatedAt();
				String date = CalendarUtil.getDateFormated(udp, "yyyy-MM-dd");
				st = st + "\n " + category.getName() + " "
						+ category.getSystemId() + " "
						+ category.getCategoryId() + " " + date;
			}
		} else {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCategoriasDone(boolean b) {
		if (b) {
			updateSubCategories();
			ArrayList<Category> categories = CategoryProvider
					.readCategorys(this);
			ImagesUpdater imageUpdate = new ImagesUpdater(this);
			imageUpdate.UpdateCategorysCoversIfNeeded(this, categories);
		}

	}

	@Override
	public void onSubCategoriasPlatesDone(boolean b) {
		categoryCountDown--;
		if (categoryCountDown == 0) {
			// showsubcategories();
		}
	}

	@Override
	public void onCategoryImagesDone(boolean b) {
		if (b) {

		}
	}

	@Override
	public void onPlatesImagesDone(boolean b) {
		// TODO Auto-generated method stub

	}
}
