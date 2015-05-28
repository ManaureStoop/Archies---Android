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

import com.grability.archies.PlatesActivity;
import com.grability.archies.R;
import com.grability.archies.db.model.Plate;
import com.grability.archies.utils.FileUtil;
import com.grability.archies.utils.FontUtil;

public class PlatesAdapter extends BaseAdapter {
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	ArrayList<Plate> plates;

	public PlatesAdapter(Context context, int resourceId,
			ArrayList<Plate> plates) {
		super();
		resource = resourceId;
		this.plates = plates;
		inflater = LayoutInflater.from(context);
		this.context = context;

	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			holder = new ViewHolder();

			holder.plateTitle = (TextView) convertView
					.findViewById(R.id.textView_plates_title);
			holder.plateDescription = (TextView) convertView
					.findViewById(R.id.textView_plates_description);
			holder.plateImage = (ImageView) convertView
					.findViewById(R.id.imageView1_textView_plates_image);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.position = position;

		final Plate plate = plates.get(position);

		setFonts(holder);

		holder.plateTitle.setText(plate.getName().substring(0, 1).toUpperCase()
				+ plate.getName().substring(1).toLowerCase());
		holder.plateDescription.setText(plate.getDescription());

		if (plate.getLocalImgPath() != null) {
			if (FileUtil.imageExists(plate.getImgPath().replace("/", ""),
					context)) {
				File filePath = context.getFileStreamPath(plate
						.getLocalImgPath());
				holder.plateImage.setImageDrawable(Drawable
						.createFromPath(filePath.toString()));
			} else {
				Log.d("Error", plate.getLocalImgPath() + " es nulo");
			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((PlatesActivity) context).onItemClicked(plate);
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
		return plates.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return plates.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private void setFonts(ViewHolder holder) {
		holder.plateTitle.setTypeface((FontUtil.getTypeface(context,
				FontUtil.HELVETICA_NEUE_LIGHT)));
		holder.plateDescription.setTypeface((FontUtil.getTypeface(context,
				FontUtil.HELVETICA_NEUE_LIGHT)));

	}

	public void setPlates(ArrayList<Plate> categories) {
		this.plates = categories;
	}

	static class ViewHolder {
		TextView plateTitle;
		TextView plateDescription;
		ImageView plateImage;
		int position;
	}
}
