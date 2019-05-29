package com.zskx.pemsystem.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class ButtonSelector{
/** 设置Selector。 */
public static  StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,
                int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_focused }, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[] {}, normal);
        return bg;
	}
}

//Button btnNormal = (Button) findViewById(R.id.btnSampleNormal);
//btnNormal.setBackgroundDrawable(newSelector(this, R.drawable.btn_normal, R.drawable.btn_selected,
//                R.drawable.btn_selected, R.drawable.btn_unable));
//Button btnUnable = (Button) findViewById(R.id.btnSampleUnable);
//btnUnable.setBackgroundDrawable(newSelector(this, R.drawable.btn_normal, R.drawable.btn_selected,
//                R.drawable.btn_selected, R.drawable.btn_unable));
//btnUnable.setEnabled(false);
//btnUnable.setOnClickListener(this);