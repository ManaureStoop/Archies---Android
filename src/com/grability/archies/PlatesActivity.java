package com.grability.archies;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.grability.archies.adapter.PlatesAdapter;
import com.grability.archies.db.model.Category;
import com.grability.archies.db.model.Plate;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.db.provider.CategoryProvider;
import com.grability.archies.db.provider.PlateProvider;
import com.grability.archies.db.provider.SubCategoryProvider;
import com.grability.archies.listener.ImagessUpdaterListener;
import com.grability.archies.listener.WebServiceListener;
import com.grability.archies.utils.CalendarUtil;
import com.grability.archies.ws.ArchiesWebServices;
import com.grability.archies.ws.ImagesUpdater;

public class PlatesActivity extends Activity implements WebServiceListener,
		ImagessUpdaterListener {
	int categoryCountDown;
	int subCategoryId;

	ArrayList<Plate> plates;

	ArchiesWebServices wsProviderArchiesWebServices;
	PlatesAdapter adapter;

	// Views
	AbsListView menuList;
	RelativeLayout actionBarLayout;
	RelativeLayout splashLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide ActionBar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setActionBar();

		setContentView(R.layout.activity_plates);

		setOrientation();

		loadViews();

		Intent intent = getIntent();
		subCategoryId = intent.getExtras().getInt("subCategoryId");
		plates = PlateProvider.readPlateBySubCategory(this, subCategoryId);

		if (plates != null) {
			initializeAdapter();
		}

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

	private void initializeAdapter() {
		adapter = new PlatesAdapter(this, R.layout.plates_view, plates);
		menuList.setAdapter(adapter);
	}

	private void loadViews() {
		menuList = (AbsListView) findViewById(R.id.listView_plates_menu);
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
				.readSubCategoryByCategory(this, subCategoryId);
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

	private void refreshListView() {
		plates = PlateProvider.readPlateBySubCategory(this, subCategoryId);
		if (plates != null) {
			if (adapter == null) {
				initializeAdapter();
			} else {
				adapter.setPlates(plates);
				adapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public void onSubCategoriasPlatesDone(boolean b) {
		categoryCountDown--;
		if (categoryCountDown == 0) {
			refreshListView();
			// showsubcategories();
		}
	}

	public void onItemClicked(Plate plate) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, PlatesDetailActivity.class);
		intent.putExtra("plateId", plate.getSystemId());
		startActivity(intent);
	}

	@Override
	public void onCategoryImagesDone(boolean b) {
		if (b) {
			refreshListView();
		}
	}

	@Override
	public void onPlatesImagesDone(boolean b) {
		// TODO Auto-generated method stub

	}
}
