package com.grability.archies.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;

public class FontUtil {
	
	public static String HELVETICA_NEUE_LIGHT = "fonts/HelveticaNeue-Light.otf";
	public static String FORGOTEN_FUTURIST_BD = "fonts/forgotten_futurist_bd.ttf";
	public static String FORGOTEN_FUTURIST_RG= "fonts/forgotten_futurist_rg.ttf";

	
	public static Typeface getTypeface(Context context, String template) {
	     Typeface tf = Typeface.createFromAsset(context.getAssets(),
	            template);
	     return tf;
	}
}
