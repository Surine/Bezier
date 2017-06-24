package com.realtoraccess.baizer.libary;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

/**
 * Created by Allen Zhang on 2017/6/24.
 */

public class Utils {

    public static int getScreenWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getHeight();
    }
}
