package com.azzdorfrobotics.android.legstep.helpers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azzdorfrobotics.android.legstep.BaseActivity;
import com.azzdorfrobotics.android.legstep.R;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class ActionBarDecorator {

    private AppCompatActivity activity;
    private ActionBar actionBar;

    private FrameLayout backButton;
    private TextView titleView;
    private FrameLayout settingsButton;

    private Type type;

    public enum Type {
        MAIN, INNER
    }

    /**
     * @param activity Activity for context usage
     * @param type
     */
    public ActionBarDecorator(AppCompatActivity activity, Type type) {
        this.activity = activity;
        this.type = type;

        actionBar = activity.getSupportActionBar();

        if (type != null && actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            setType(type);
        }
    }

    public ActionBarDecorator(AppCompatActivity activity) {
        this(activity, null);
    }

    /**
     * Inflates needed layout by ActionBar type & loads it's elements for further usage
     *
     * @param type
     * @return
     */
    private void setType(Type type) {
        LinearLayout actionBarLayout = null;
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.LEFT);

        /**
         * Inflate needed layout and init variables for it
         */
        switch (type) {
            case MAIN:
                actionBarLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.actionbar_main, null);
                settingsButton = (FrameLayout) actionBarLayout.findViewById(R.id.actionbar_settings_button);
                break;

            case INNER:
                actionBarLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.actionbar_inner, null);
                backButton = (FrameLayout) actionBarLayout.findViewById(R.id.actionbar_back_button);
                titleView = (TextView) actionBarLayout.findViewById(R.id.actionbar_title);
                settingsButton = (FrameLayout) actionBarLayout.findViewById(R.id.actionbar_settings_button);
                break;
        }

        /**
         * If layout is not inited - do nothing
         */
        if (actionBarLayout == null) return;

        /**
         * Place action bar
         */
        actionBar.setCustomView(actionBarLayout, params);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setElevation(BaseActivity.getContext().getResources().getDimension(R.dimen.action_bar_elevation));

        Toolbar parent = (Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        /**
         * Attach default click events
         */
        if (backButton != null) backButton.setOnClickListener(backClick);

    }

    /**
     * Handles Back button clicks for INNER action bar
     */
    private View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.finish();
        }
    };

    /**
     * Sets the title for inner action bar
     *
     * @param title
     * @return
     */
    public ActionBarDecorator setTitle(String title) {
        // set title only if view is inited
        if (titleView != null) {
            titleView.setText(title);
            activity.setTitle(title);
        }
        return this;
    }

    /**
     * Set onClick listener for settings button
     *
     * @param click View.OnClickListener with required actions
     * @throws IllegalArgumentException if wrong null onclick is assigned
     */
    public void setSettingsOnClick(View.OnClickListener click) {
        if (settingsButton != null) {
            settingsButton.setVisibility(View.VISIBLE);
            settingsButton.setOnClickListener(click);
        } else {
            throw new IllegalArgumentException("Settings button do not initial");
        }
    }

    /**
     * Set custom onBackClick otherwise default listener will be used
     *
     * @param click custom listener
     */
    public void setBackOnClick(View.OnClickListener click) {
        if (type == Type.INNER && backButton != null) {
            backButton.setOnClickListener(click);
        }
    }

    /**
     * Required for float menus
     *
     * @return view object
     */
    public View getSettingsButton() {
        return settingsButton;
    }
}
