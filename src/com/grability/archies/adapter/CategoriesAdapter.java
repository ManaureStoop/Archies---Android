package com.grability.archies.adapter;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grability.archies.MainActivity;
import com.grability.archies.R;
import com.grability.archies.db.model.Category;
import com.grability.archies.listener.ImagessUpdaterListener;
import com.grability.archies.utils.FileUtil;
import com.grability.archies.utils.FontUtil;

public class CategoriesAdapter extends BaseAdapter {
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	ArrayList<Category> categories;
	ImagessUpdaterListener listener;

	public CategoriesAdapter(Context context, int resourceId,
			ArrayList<Category> categories, ImagessUpdaterListener listener) {
		super();
		resource = resourceId;
		this.categories = categories;
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.listener = listener;

	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			holder = new ViewHolder();

			holder.categoryBg = (ImageView) convertView
					.findViewById(R.id.imageView_menu_bck);
			holder.categoryTitle = (TextView) convertView
					.findViewById(R.id.textView_menu_title);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.position = position;

		final Category category = categories.get(position);

		setFonts(holder);

		holder.categoryTitle.setText(category.getName().substring(0, 1)
				.toUpperCase()
				+ category.getName().substring(1).toLowerCase());
		if (category.getLocalImgPath() != null) {
			if (FileUtil.imageExists(category.getImgPath().replace("/", ""),
					context)) {
				File filePath = context.getFileStreamPath(category
						.getLocalImgPath());
				holder.categoryBg.setImageDrawable(Drawable
						.createFromPath(filePath.toString()));
			} else {
				Log.d("cATEGO", category.getLocalImgPath() + " es nulo");

			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) context).onItemClicked(category);
			}
		});
		// Animating the views
		Animation animation = null;
		animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
		animation.setDuration(500);
		convertView.startAnimation(animation);
		animation = null;

		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private void setFonts(ViewHolder holder) {
		holder.categoryTitle.setTypeface((FontUtil.getTypeface(context,
				FontUtil.HELVETICA_NEUE_LIGHT)));

	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}

	static class ViewHolder {
		ImageView categoryBg;
		TextView categoryTitle;
		int position;
	}
}
