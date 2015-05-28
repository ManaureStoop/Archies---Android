package com.grability.archies.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grability.archies.R;
import com.grability.archies.SubCategoryActivity;
import com.grability.archies.db.model.SubCategory;
import com.grability.archies.utils.FontUtil;


public class SubCategoriesAdapter extends BaseAdapter {
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	ArrayList<SubCategory> subCategories;
	
	public SubCategoriesAdapter(Context context, int resourceId,
			ArrayList<SubCategory> categories) {
		super();
		resource = resourceId;
		this.subCategories = categories;
		inflater = LayoutInflater.from(context);
		this.context = context;

	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			holder = new ViewHolder();

			holder.subCategoryTitle = (TextView) convertView
					.findViewById(R.id.textView_menu_title);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.position = position;

		final SubCategory subCategory = subCategories.get(position);

		setFonts(holder);

		holder.subCategoryTitle.setText(subCategory.getName().substring(0,1).toUpperCase() + subCategory.getName().substring(1).toLowerCase());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SubCategoryActivity) context).onItemClicked(subCategory);
			}
		});
//Animating the views
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
		return subCategories.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subCategories.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private void setFonts(ViewHolder holder) {
		holder.subCategoryTitle.setTypeface((FontUtil.getTypeface(context,
				FontUtil.HELVETICA_NEUE_LIGHT)));

	}

	public void setSubCategories(ArrayList<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	static class ViewHolder {
		TextView subCategoryTitle;
		int position;
	}
}
