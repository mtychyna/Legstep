package com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers;

/**
 * Created on 27.01.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public interface OnFavoriteChangeCallback {

    /**
     * Called when favorite flag on any item was changed and its required to includ or exclude data
     * from auto hint dictionary
     *
     * @param state   MainActivity.STATE_EXCLUDE || MainActivity.STATE_INCLUDE
     * @param content exact data
     */
    void favoriteChanged(int state, String content);
}
