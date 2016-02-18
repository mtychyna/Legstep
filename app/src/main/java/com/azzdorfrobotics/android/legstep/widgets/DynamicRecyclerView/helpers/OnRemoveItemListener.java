package com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers;

/**
 * Created on 18.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public interface OnRemoveItemListener<T> {

    /**
     * Will be run on item remove
     * @param item
     */
    void onItemRemoved(T item);
}
