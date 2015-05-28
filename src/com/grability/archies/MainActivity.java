package com.grability.archies;

import java.util.ArrayList;
import java.util.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grability.archies.adapter.CategoriesAdapter;
import com.grability.archies.db.model.Category;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.db.provider.CategoryProvider;
import com.grability.archies.db.provider.SubCategoryProvider;
import com.grability.archies.listener.ImagessUpdaterListener;
import com.grability.archies.listener.WebServiceListener;
import com.grability.archies.utils.CalendarUtil;
import com.grability.archies.ws.ArchiesWebServices;
import com.grability.archies.ws.ImagesUpdater;

public class MainActivity extends Activity implements WebServiceListener,
		ImagessUpdaterListener {
	int categoryCountDown;

	ArrayList<Category> categories;

	ArchiesWebServices wsProviderArchiesWebServices;
	CategoriesAdapter adapter;

	// Views
	AbsListView menuList;
	RelativeLayout actionBarLayout;
	RelativeLayout splashLayout;
	ImageView footerObjetcs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide ActionBar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.activity_main);

		loadViews();

		setOrientation();

		showSplash();

		updateCategory();

		categories = CategoryProvider.readCategorys(this);

		if (categories != null) {
			initializeAdapter();
		}

	}

	private void showSplash() {
		if (splashLayout == null) {

		}
		splashLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				crossfade();
				setActionBar();
			}
		}, 3000);
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
		adapter = new CategoriesAdapter(this, R.layout.category_view,
				categories, this);
		menuList.setAdapter(adapter);
	}

	private void loadViews() {
		menuList = (AbsListView) findViewById(R.id.listView_main_menu);
		menuList.setVisibility(View.GONE);
		splashLayout = (RelativeLayout) findViewById(R.id.relativeLayout_splash);
		footerObjetcs = (ImageView) findViewById(R.id.imageView_footer_objects);
	}

	private void updateCategory() {
		wsProviderArchiesWebServices = new ArchiesWebServices(this);
		wsProviderArchiesWebServices.UpdateCategories(this);
	}

	private void updateSubCategories() {

		if (categories != null) {
			categoryCountDown = categories.size();

			String st = " ";

			for (Category category : categories) {

				wsProviderArchiesWebServices.UpdateSubcategoriesPlates(this,
						category.getSystemId());

				Calendar udp = category.getUpdatedAt();
				String date = CalendarUtil.getDateFormated(udp, "yyyy-MM-dd");
				st = st + "\n " + category.getName() + " "
						+ category.getSystemId() + " " + category.getImgPath()
						+ " " + date;

			}
		} else {

		}
	}

	private void showsubcategories() {
		ArrayList<SubCategory> categories = SubCategoryProvider
				.readSubCategorys(this);
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
			refreshListView();
			updateSubCategories();
			ImagesUpdater imageUpdate = new ImagesUpdater(this);
			imageUpdate.UpdateCategorysCoversIfNeeded(this, categories);
		}

	}

	private void refreshListView() {
		categories = CategoryProvider.readCategorys(this);
		if (categories != null) {
			if (adapter == null) {
				initializeAdapter();
			} else {
				adapter.setCategories(categories);
				adapter.notifyDataSetChanged();
			}

		}
	}

	private void crossfade() {
		int mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
		// Set the content view to 0% opacity but visible, so that it is visible
		// (but fully transparent) during the animation.
		menuList.setAlpha(0f);
		menuList.setVisibility(View.VISIBLE);

		// Animate the content view to 100% opacity, and clear any animation
		// listener set on the view.
		menuList.animate().alpha(1f).setDuration(mShortAnimationDuration)
				.setListener(null);

		// Animate the loading view to 0% opacity. After the animation ends,
		// set its visibility to GONE as an optimization step (it won't
		// participate in layout passes, etc.)
		splashLayout.animate().alpha(0f).setDuration(mShortAnimationDuration)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						splashLayout.setVisibility(View.GONE);
					}
				});
	}

	@Override
	public void onSubCategoriasPlatesDone(boolean b) {
		categoryCountDown--;
		if (categoryCountDown == 0) {
			ArrayList<SubCategory> subCategories = SubCategoryProvider
					.readSubCategorys(this);
			ImagesUpdater imageUpdate = new ImagesUpdater(this);
			imageUpdate.UpdatePlatesImagesIfNeeded(this, subCategories);
			showsubcategories();
		}
	}

	public void onItemClicked(Category category) {
		Intent intent = new Intent(this, SubCategoryActivity.class);
		intent.putExtra("categoryId", category.getSystemId());
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
