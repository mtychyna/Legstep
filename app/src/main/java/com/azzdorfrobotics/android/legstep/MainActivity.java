package com.azzdorfrobotics.android.legstep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.azzdorfrobotics.android.legstep.adapters.OptionMenuItemAdapter;
import com.azzdorfrobotics.android.legstep.controllers.OnWalkStateChangedListener;
import com.azzdorfrobotics.android.legstep.controllers.WalkController;
import com.azzdorfrobotics.android.legstep.helpers.ActionBarDecorator;
import com.azzdorfrobotics.android.legstep.helpers.Common;
import com.azzdorfrobotics.android.legstep.model.Creature;
import com.azzdorfrobotics.android.legstep.model.Direction;
import com.azzdorfrobotics.android.legstep.model.OptionMenuItem;
import com.azzdorfrobotics.android.legstep.model.Podium;
import com.azzdorfrobotics.android.legstep.model.Walk;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class MainActivity extends BaseActivity implements OnWalkStateChangedListener {

    private static final int MENU_ITEM_LANG_EN = 1;
    private static final int MENU_ITEM_LANG_RU = 2;
    private static final int MENU_ITEM_LANG_UA = 3;

    @Bind(R.id.legstep_config)
    public ImageView legtepConfig;
    @Bind(R.id.legstep_hi)
    public TextView legtepHi;
    @Bind(R.id.legstep_type)
    public TextView legtepType;
    @Bind(R.id.legstep_legs)
    public TextView legtepLegs;
    @Bind(R.id.podium_length)
    public TextView podiumLength;
    @Bind(R.id.legstep_status)
    public TextView legstepStatus;
    @Bind(R.id.legstep)
    public ImageView legstep;
    @Bind(R.id.podium_line)
    public View podiumLine;
    @Bind(R.id.legstep_play)
    public ImageView legtepPlay;
    @Bind(R.id.legstep_initial)
    public ImageView legtepInitial;

    private ActionBarDecorator mActionBar;

    private Creature mCreature;
    private Podium mPodium;

    private WalkController mWalkController;
    private int prevPosition = 0;
    private int podiumLenghtDP = 0;
    /**
     * Option menu
     */
    private ListPopupWindow optionMenuPopup;
    private ListPopupWindow optionMenuLangPopup;
    private OptionMenuItemAdapter optionMenuAdapter;
    private OptionMenuItemAdapter optionMenuLangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        mActionBar = new ActionBarDecorator(MainActivity.this, ActionBarDecorator.Type.MAIN);
        mActionBar.setSettingsOnClick(optionMenuListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WalkController.destroyController();
        loadCreaturePodium();
        getPodiumWidth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWalkController != null) {
            mWalkController.stopWalk();
        }
    }

    private void loadCreaturePodium() {
        /**
         * Load creature and podium data
         */
        mCreature = Common.getCreature(getContext());
        mPodium = Common.getPodium(getContext());

        /**
         * Load data to UI
         */
        legtepHi.setText(Common.formatString(R.string.legstep_hi, mCreature.title));
        legtepType.setText(Common.formatString(R.string.legstep_i_am, mCreature.type));
        legtepLegs.setText(Common.formatString(R.string.legstep_legs, mCreature.paws.size()));
        podiumLength.setText(Common.formatString(R.string.podium_length, mPodium.length));
        legstepStatus.setText(Common.formatString(R.string.position_initial, mCreature.title));
    }

    /**
     * OnClicks
     */

    @OnClick(R.id.legstep_config)
    public void configOnClick(View v) {
        Intent configIntent = new Intent(getContext(), SettingsActivity.class);
        startActivity(configIntent);
    }

    @OnClick(R.id.legstep_play)
    public void playOnClick(View v) {
        Walk walk = new Walk(mCreature, mPodium);
        mWalkController = WalkController.getInstance(MainActivity.this, walk);

        mWalkController.startWalk();
    }

    @OnClick(R.id.legstep_initial)
    public void initialOnClick(View v) {
        Walk walk = new Walk(mCreature, mPodium);
        mWalkController = WalkController.getInstance(MainActivity.this, walk);

        mWalkController.stopWalk();
    }

    /**
     * OnWalkStateChangedListener methods
     */

    public void returnToInitial() {
        legstepStatus.setText(Common.formatString(R.string.position_return_to_initial, mCreature.title));
        walkAnimate(true, 0);
    }

    public void move(Direction direction, int position) {
        switch (direction) {
            case BACKWARD:
                legstepStatus.setText(Common.formatString(R.string.position_move_backward, mCreature.title, position));
                break;
            case FORWARD:
                legstepStatus.setText(Common.formatString(R.string.position_move_forward, mCreature.title, position));
                break;
        }
    }

    public void atPosition(int position) {
        legstepStatus.setText(Common.formatString(R.string.position_at_position, mCreature.title, position));
        walkAnimate(false, position);
    }

    @Override
    public void walkEnd() {
        legstepStatus.setText(getString(R.string.position_complete));
        walkAnimate(true, 0);
    }

    @Override
    public void errorRised(String response) {
        Snackbar snackbar = Snackbar
                .make(legstepStatus, Common.formatString(R.string.podium_snackbar_podium_invalid, response), Snackbar.LENGTH_LONG)
                .setAction(getContext().getString(R.string.action_initial), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mWalkController != null) {
                            mWalkController.stopWalk();
                        }
                    }
                });
        View view = snackbar.getView();
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.GREEN);
        snackbar.show();
    }

    private void walkAnimate(boolean initial, int toPosition) {
        int fromX = prevPosition;
        int toX = (podiumLenghtDP * toPosition) / mPodium.length;
        TranslateAnimation animation = new TranslateAnimation(fromX, toX, 0, 0); //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1000);  // animation duration
        animation.setFillAfter(true);
        prevPosition = toX;
        legstep.startAnimation(animation);
        if (!initial) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mWalkController.resumeWalk();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            legstepStatus.setText(Common.formatString(R.string.position_initial, mCreature.title));
        }
    }

    private void getPodiumWidth() {
        ViewTreeObserver vto = podiumLine.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                podiumLine.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                podiumLenghtDP = podiumLine.getMeasuredWidth() - 100;
            }
        });
    }


    /**
     * Option menu listeners
     */
    View.OnClickListener optionMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            optionMenuAdapter = new OptionMenuItemAdapter(getContext(), new ArrayList<OptionMenuItem>());
            switch (getUserLang(getContext())) {
                case EN:
                    optionMenuAdapter.add(new OptionMenuItem(getContext().getString(R.string.option_menu_lang_en), MENU_ITEM_LANG_EN, true, R.drawable.ic_option_menu_lang_en));
                    break;
                case RU:
                    optionMenuAdapter.add(new OptionMenuItem(getContext().getString(R.string.option_menu_lang_ru), MENU_ITEM_LANG_RU, true, R.drawable.ic_option_menu_lang_ru));
                    break;
                case UA:
                    optionMenuAdapter.add(new OptionMenuItem(getContext().getString(R.string.option_menu_lang_uk), MENU_ITEM_LANG_UA, true, R.drawable.ic_option_menu_lang_ua));
                    break;
            }
            if (optionMenuPopup != null && optionMenuPopup.isShowing()) {
                optionMenuPopup.dismiss();
            }
            optionMenuPopup = new ListPopupWindow(getContext());
            optionMenuPopup.setAdapter(optionMenuAdapter);
            optionMenuPopup.setModal(true);
            optionMenuPopup.setAnchorView(mActionBar.getSettingsButton()); // the must
            optionMenuPopup.setOnItemClickListener(onMenuItemClick);

            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float halfWidth = displayMetrics.widthPixels * 2 / 3;
            optionMenuPopup.setContentWidth((int) halfWidth);
            optionMenuPopup.setHeight(ListPopupWindow.WRAP_CONTENT);
            optionMenuPopup.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shadow_5_corner_5));
            optionMenuPopup.show();
        }
    };

    /**
     * Clicking on option menu item event
     */
    private AdapterView.OnItemClickListener onMenuItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OptionMenuItem item = optionMenuAdapter.getItem(position);

            switch (item.itemId) {
                case MENU_ITEM_LANG_EN:
                case MENU_ITEM_LANG_RU:
                case MENU_ITEM_LANG_UA:
                    optionMenuPopup.dismiss();
                    optionMenuLangAdapter = new OptionMenuItemAdapter(getContext(), new ArrayList<OptionMenuItem>());
                    optionMenuLangAdapter.add(new OptionMenuItem(getContext().getString(R.string.option_menu_lang_en), MENU_ITEM_LANG_EN, true, R.drawable.ic_option_menu_lang_en));
                    optionMenuLangAdapter.add(new OptionMenuItem(getContext().getString(R.string.option_menu_lang_ru), MENU_ITEM_LANG_RU, true, R.drawable.ic_option_menu_lang_ru));
                    optionMenuLangAdapter.add(new OptionMenuItem(getContext().getString(R.string.option_menu_lang_uk), MENU_ITEM_LANG_UA, true, R.drawable.ic_option_menu_lang_ua));

                    optionMenuLangPopup = new ListPopupWindow(getContext());
                    optionMenuLangPopup.setAdapter(optionMenuLangAdapter);
                    optionMenuLangPopup.setModal(true);
                    optionMenuLangPopup.setAnchorView(mActionBar.getSettingsButton()); // the must
                    optionMenuLangPopup.setOnItemClickListener(onMenuLangItemClick);

                    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
                    float halfWidth = displayMetrics.widthPixels / 2;
                    optionMenuLangPopup.setContentWidth((int) halfWidth);
                    optionMenuLangPopup.setHeight(ListPopupWindow.WRAP_CONTENT);
                    optionMenuLangPopup.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shadow_5_corner_5));
                    optionMenuLangPopup.show();
                    break;

                default:
                    optionMenuPopup.dismiss();
                    Toast.makeText(getContext().getApplicationContext(), "Opening " + item.title, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * Option menu listener on click
     */
    private AdapterView.OnItemClickListener onMenuLangItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OptionMenuItem item = optionMenuLangAdapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            switch (item.itemId) {
                default:
                case MENU_ITEM_LANG_EN:
                    intent.putExtra(BaseActivity.EXTRA_LANG, Common.UserLang.EN.getLang());
                    break;
                case MENU_ITEM_LANG_RU:
                    intent.putExtra(BaseActivity.EXTRA_LANG, Common.UserLang.RU.getLang());
                    break;
                case MENU_ITEM_LANG_UA:
                    intent.putExtra(BaseActivity.EXTRA_LANG, Common.UserLang.UA.getLang());
                    break;
            }
            optionMenuLangPopup.dismiss();
            MainActivity.this.finish();
            MainActivity.this.startActivity(intent);
        }
    };
}
