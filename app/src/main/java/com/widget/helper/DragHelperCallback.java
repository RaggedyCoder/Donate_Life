package com.widget.helper;

import android.support.v4.widget.ViewDragHelper;
import android.view.View;

/**
 * Created by tuman on 16/11/2014.
 */
public class DragHelperCallback extends ViewDragHelper.Callback {
    @Override
    public boolean tryCaptureView(View view, int i) {
        return false;
    }
}
