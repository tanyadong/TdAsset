package com.mobile.tiandy.asset.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.tiandy.asset.common.vo.User;

import java.util.List;


public class LoginUtils {
	private static Gson gson = new Gson();

	public static User getUserInfo(Context context) {
		SharedPreferences pref = context.getSharedPreferences("user_login", 0);
		User user = gson.fromJson(pref.getString("user_info", ""),
				User.class);
		return user;
	}

	public static void saveUserInfo(Context context, User user) {
		SharedPreferences pref = context.getSharedPreferences("user_login", 0);
		// 将user对象转换成json格式并保存
		pref.edit().putString("user_info", gson.toJson(user)).commit();
	}

	public static void saveAssetPlace(Context context, List<String> list) {
		SharedPreferences pref = context.getSharedPreferences("asset_place", 0);
		// 将user对象转换成json格式并保存
		pref.edit().putString("assetplace", gson.toJson(list)).commit();
	}

	public static List<String> getAssetPlace(Context context) {
		SharedPreferences pref = context.getSharedPreferences("asset_place", 0);
		List<String> list = gson.fromJson(pref.getString("assetplace", ""),
				new TypeToken<List<String>>() {
				}.getType());
		return list;
	}
}
