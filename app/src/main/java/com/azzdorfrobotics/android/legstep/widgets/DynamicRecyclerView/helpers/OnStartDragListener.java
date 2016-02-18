package com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers;

import android.support.v7.widget.RecyclerView;

/**
 * Created on 18.01.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
