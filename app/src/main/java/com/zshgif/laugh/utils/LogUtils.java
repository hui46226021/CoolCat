package com.zshgif.laugh.utils;

import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**日志工具
 * Created by sh on 2015/11/17.
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
    static boolean bo = true;


    public static void e(String tag, String msg){
        if(!bo){
            return;
        }
        Log.e(tag,msg);
//        logger.debug(msg);
    }
    public static void i(String tag, String msg){
        if(!bo){
            return;
        }
        Log.i(tag,msg);
    }
}
