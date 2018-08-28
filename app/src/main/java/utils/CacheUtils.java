package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
    //得到保存的string类型的数据
    public static String getString(Context context, String key) {
        SharedPreferences sp=context.getSharedPreferences("longmall",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    //保存数据类型
    public static void saveString(Context context, String key,String value) {
        SharedPreferences sp=context.getSharedPreferences("longmall",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
}
