package com.zshgif.laugh.utils;

import android.util.Log;

/**日志工具
 * Created by sh on 2015/11/17.
 */
public class LogUtils {
    static boolean bo = true;
    public static void e(String tag, String msg){
        if(!bo){
            return;
        }
        Log.e(tag,msg);
    }
    public static void i(String tag, String msg){
        if(!bo){
            return;
        }
        Log.i(tag,msg);
    }
}
