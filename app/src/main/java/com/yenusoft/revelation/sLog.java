package com.yenusoft.revelation;

import android.util.Log;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class sLog
{
    private static void slog(String className, String content)
    {
        String t = className.replace("com.yenusoft.revelation", "");
        Log.d("sofkaints", t + " : " + content);
    }

    public static void slog(String className, int content)
    {
        slog(className, Integer.toString(content));
    }

    public static void slog(String className, boolean content)
    {
        slog(className, Boolean.toString(content));
    }
}
