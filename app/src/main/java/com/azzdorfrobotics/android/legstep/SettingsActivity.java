package com.azzdorfrobotics.android.legstep;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azzdorfrobotics.android.legstep.helpers.ActionBarDecorator;
import com.azzdorfrobotics.android.legstep.helpers.Common;
import com.azzdorfrobotics.android.legstep.model.Creature;
import com.azzdorfrobotics.android.legstep.model.Direction;
import com.azzdorfrobotics.android.legstep.model.Paw;
import com.azzdorfrobotics.android.legstep.model.Podium;
import com.azzdorfrobotics.android.legstep.model.Route;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.adapters.PawsRecyclerListAdapter;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.adapters.RoutesRecyclerListAdapter;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.ItemTouchHelperCallback;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.ItemTouchHelperNoRemoveCallback;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.OnRemoveItemListener;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.OnStartDragListener;
import com.azzdorfrobotics.android.legstep.widgets.InlineNumberPicker;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class SettingsActivity extends BaseActivity implements OnStartDragListener, OnRemoveItemListener<Route> {

    /**
     * General UI
     */
    @Bind(R.id.creature_config)
    public Button creatureConfig;
    @Bind(R.id.podium_config)
    public Button podiumConfig;
    @Bind(R.id.restore_defaults)
    public Button restoreDefaults;

    /**
     * Creature UI
     */
    @Bind(R.id.creature_container)
    public LinearLayout creatureContainer;
    @Bind(R.id.title_edit)
    public ImageView titleEdit;
    @Bind(R.id.creature_title)
    public EditText creatureTitle;
    @Bind(R.id.type_edit)
    public ImageView typeEdit;
    @Bind(R.id.creature_type)
    public EditText creatureType;
    @Bind(R.id.step_size_picker)
    public InlineNumberPicker stepSizePicker;
    @Bind(R.id.legs_number_picker)
    public InlineNumberPicker legsNumberPicker;
    @Bind(R.id.id_picker)
    public InlineNumberPicker idPicker;
    @Bind(R.id.paws_list)
    public RecyclerView pawsList;

    /**
     * Podium UI
     */
    @Bind(R.id.podium_container)
    public LinearLayout podiumContainer; //hide by defaults
    @Bind(R.id.podium_size_picker)
    public InlineNumberPicker podiumSizePicker;
    @Bind(R.id.route_validate)
    public Button validatePodium;
    @Bind(R.id.new_route_size_picker)
    public InlineNumberPicker newRouteSizePicker;
    @Bind(R.id.direction_backward)
    public ImageView directionBackward;
    @Bind(R.id.direction_forward)
    public ImageView directionForward;
    @Bind(R.id.routes_list)
    public RecyclerView routesList;
    @Bind(R.id.route_add)
    public Button addRoute;

    /**
     * Recycler views stuff
     */
    private ItemTouchHelper mPawsItemTouchHelper;
    private PawsRecyclerListAdapter mPawsAdapter;
    private ArrayList<Paw> mPaws;

    private ItemTouchHelper mRoutesItemTouchHelper;
    private RoutesRecyclerListAdapter mRoutesAdapter;
    private ArrayList<Route> mRoutes;

    /**
     * Activity data
     */
    private boolean configCreatureNow = true;
    private Creature mCreature;
    private Podium mPodium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(SettingsActivity.this);

        ActionBarDecorator actionBar = new ActionBarDecorator(SettingsActivity.this, ActionBarDecorator.Type.INNER);
        actionBar.setTitle(getString(R.string.activity_title_settings));

        /**
         * Init recycler view and adapter
         */
        mPawsAdapter = new PawsRecyclerListAdapter(SettingsActivity.this, pawsList, new ArrayList<Paw>());
        pawsList.setAdapter(mPawsAdapter);
        pawsList.setLayoutManager(new LinearLayoutManager(SettingsActivity.this));
        ItemTouchHelper.Callback callback = new ItemTouchHelperNoRemoveCallback(mPawsAdapter);
        mPawsItemTouchHelper = new ItemTouchHelper(callback);
        mPawsItemTouchHelper.attachToRecyclerView(pawsList);

        mRoutesAdapter = new RoutesRecyclerListAdapter(SettingsActivity.this, SettingsActivity.this, routesList, new ArrayList<Route>());
        routesList.setAdapter(mRoutesAdapter);
        routesList.setLayoutManager(new LinearLayoutManager(SettingsActivity.this));
        ItemTouchHelper.Callback callback2 = new ItemTouchHelperCallback(mRoutesAdapter);
        mRoutesItemTouchHelper = new ItemTouchHelper(callback2);
        mRoutesItemTouchHelper.attachToRecyclerView(routesList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCreaturePodium();
    }

    @Override
    protected void onPause() {
        saveCreature();
        savePodium();
        super.onPause();
    }

    /**
     * Start drag with recycler view
     * possibly could based in view holder type
     *
     * @param viewHolder The holder of the view to drag.
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (configCreatureNow) {
            mPawsItemTouchHelper.startDrag(viewHolder);
        } else {
            mRoutesItemTouchHelper.startDrag(viewHolder);
        }
    }

    /**
     * Removed item comes here and have to be loaded to add container
     *
     * @param item route is going to be removed
     */
    @Override
    public void onItemRemoved(Route item) {
        /**
         * Add route container
         */
        newRouteSizePicker.setValue(item.length);
        setNewContainerDirection(item.direction);
    }

    private void loadCreaturePodium() {
        /**
         * Default UI state set
         *
         * General UI
         */
        creatureConfig.setActivated(true);
        podiumConfig.setActivated(false);
        creatureContainer.setVisibility(View.VISIBLE);
        podiumContainer.setVisibility(View.GONE);
        /**
         * Creature UI
         */
        creatureTitle.setFocusable(false);
        creatureTitle.clearFocus();
        titleEdit.setActivated(true);
        creatureType.setFocusable(false);
        creatureType.clearFocus();
        typeEdit.setActivated(true);
        stepSizePicker.setOnChanged(onStepChangedListener);
        legsNumberPicker.setOnChanged(onLegsNumberChangedListener);
        idPicker.setOnChanged(onIdChangedListener);
        /**
         * Podium UI
         */
        podiumSizePicker.setOnChanged(onPodiumSizeChangedListener);
        newRouteSizePicker.setOnChanged(onRouteSizeChangedListener);
        newRouteSizePicker.setMaxValue(podiumSizePicker.getValue());
        setNewContainerDirection(Direction.BACKWARD);

        /**
         * Load creature and podium data
         */
        mCreature = Common.getCreature(getContext());
        mPodium = Common.getPodium(getContext());

        /**
         * Load data to UI
         *
         * Creature UI
         */
        creatureTitle.setText(mCreature.title);
        creatureType.setText(mCreature.type);
        stepSizePicker.setValue(mCreature.stepLength);
        legsNumberPicker.setValue(mCreature.paws.size());
        idPicker.setValue(mCreature.id);
        mPawsAdapter.setItems(mCreature.paws);
        /**
         * Podium UI
         */
        podiumSizePicker.setValue(mPodium.length);
        mRoutesAdapter.setItems(mPodium.getRoutes());
    }

    private void saveCreature() {
        mCreature.id = idPicker.getValue();
        mCreature.title = creatureTitle.getText().toString();
        mCreature.type = creatureType.getText().toString();
        mCreature.stepLength = stepSizePicker.getValue();
        /**
         * Set position to paw obj
         */
        ArrayList<Paw> paws = new ArrayList<>();
        paws.addAll(mPawsAdapter.getItems());
        for (int i = 0; i < paws.size(); i++) {
            paws.get(i).position = i;
        }
        mCreature.paws = paws;
        Common.setCreature(getContext(), mCreature);
    }

    private void savePodium() {
        mPodium.length = podiumSizePicker.getValue();
        /**
         * Set position to route obj
         */
        ArrayList<Route> routes = new ArrayList<>();
        routes.addAll(mRoutesAdapter.getItems());
        for (int i = 0; i < routes.size(); i++) {
            routes.get(i).position = i;
        }
        if (!mPodium.setRoutes(routes)) {
            mPodium.setDefault();
            Common.showToast(R.string.podium_toast_routes_invalid);
        }
        Common.setPodium(getContext(), mPodium);
    }

    /**
     * OnCLick General UI
     */
    @OnClick(R.id.creature_config)
    public void creatureConfigOnClick(View v) {
        creatureConfig.setActivated(true);
        podiumConfig.setActivated(false);
        creatureContainer.setVisibility(View.VISIBLE);
        podiumContainer.setVisibility(View.GONE);
        configCreatureNow = true;
    }

    @OnClick(R.id.podium_config)
    public void podiumConfigOnClick(View v) {
        creatureConfig.setActivated(false);
        podiumConfig.setActivated(true);
        creatureContainer.setVisibility(View.GONE);
        podiumContainer.setVisibility(View.VISIBLE);
        configCreatureNow = false;
    }

    @OnClick(R.id.restore_defaults)
    public void restoreDefaultsOnClick(View v) {
        Snackbar snackbar = Snackbar
                .make(restoreDefaults, getString(R.string.restore_snackbar_defaults_confirm), Snackbar.LENGTH_LONG)
                .setAction(getContext().getString(R.string.action_restore), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCreature.setDefault();
                        mPodium.setDefault();
                        Common.setCreature(getContext(), mCreature);
                        Common.setPodium(getContext(), mPodium);
                        loadCreaturePodium();
                    }
                });
        View view = snackbar.getView();
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.GREEN);
        snackbar.show();
    }

    /**
     * OnCLick OnFocusChanged OnChanged Creature UI
     */
    @OnClick(R.id.title_edit)
    public void titleEditOnClick(View v) {
        if (titleEdit.isActivated()) {
            creatureTitle.setFocusableInTouchMode(true);
            creatureTitle.requestFocus();
        } else {
            creatureTitle.clearFocus();
        }
    }

    @OnFocusChange(R.id.creature_title)
    public void creatureTitleOnFocusChangeListener(View v, boolean hasFocus) {
        if (hasFocus) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(creatureTitle, InputMethodManager.SHOW_IMPLICIT);
            titleEdit.setActivated(false);
        } else {
            InputMethodManager imm = (InputMethodManager) BaseActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(creatureTitle.getWindowToken(), 0);
            creatureTitle.setFocusable(false);
            titleEdit.setActivated(true);
        }
    }

    @OnClick(R.id.type_edit)
    public void typeEditOnClick(View v) {
        if (typeEdit.isActivated()) {
            creatureType.setFocusableInTouchMode(true);
            creatureType.requestFocus();
        } else {
            creatureType.clearFocus();
        }
    }

    @OnFocusChange(R.id.creature_type)
    public void creatureTypeOnFocusChangeListener(View v, boolean hasFocus) {
        if (hasFocus) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(creatureType, InputMethodManager.SHOW_IMPLICIT);
            typeEdit.setActivated(false);
        } else {
            InputMethodManager imm = (InputMethodManager) BaseActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(creatureType.getWindowToken(), 0);
            creatureType.setFocusable(false);
            typeEdit.setActivated(true);
        }
    }

    private InlineNumberPicker.OnValueChanged onStepChangedListener = new InlineNumberPicker.OnValueChanged() {
        @Override
        public void onChange(int newValue) {
            // can get anything
        }
    };

    private InlineNumberPicker.OnValueChanged onLegsNumberChangedListener = new InlineNumberPicker.OnValueChanged() {
        @Override
        public void onChange(int newValue) {
            if (newValue < mCreature.paws.size()) {
                Iterator<Paw> pawIterator = mCreature.paws.iterator();
                while (pawIterator.hasNext()) {
                    Paw paw = pawIterator.next();
                    boolean remove = true;
                    for (int i = 0; i < newValue; i++) {
                        if (paw.index == i) {
                            remove = false;
                        }
                    }
                    if (remove) {
                        pawIterator.remove();
                    }
                }
                mPawsAdapter.setItems(mCreature.paws);
            } else if (newValue > mCreature.paws.size()) {
                while (newValue > mCreature.paws.size()) {
                    mCreature.paws.add(new Paw(mCreature.getNextPawIndex(), mCreature.paws.size()));
                }
                mPawsAdapter.setItems(mCreature.paws);
            }
        }
    };

    private InlineNumberPicker.OnValueChanged onIdChangedListener = new InlineNumberPicker.OnValueChanged() {
        @Override
        public void onChange(int newValue) {
            // can get anything
        }
    };

    /**
     * OnClick OnChanged Podium UI
     */

    private InlineNumberPicker.OnValueChanged onPodiumSizeChangedListener = new InlineNumberPicker.OnValueChanged() {
        @Override
        public void onChange(int newValue) {
            newRouteSizePicker.setMaxValue(podiumSizePicker.getValue());
            mPodium.length = podiumSizePicker.getValue();
        }
    };


    @OnClick(R.id.route_validate)
    public void validatePodiumOnClick(View v) {
        if (mPodium.isValid(mRoutesAdapter.getItems())) {
            Snackbar snackbar = Snackbar
                    .make(validatePodium, getString(R.string.podium_snackbar_podium_valid), Snackbar.LENGTH_LONG)
                    .setAction(getContext().getString(R.string.action_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // no actions need, but buttons need
                        }
                    });
            View view = snackbar.getView();
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.GREEN);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar
                    .make(validatePodium, getString(R.string.podium_snackbar_podium_invalid), Snackbar.LENGTH_LONG)
                    .setAction(getContext().getString(R.string.action_we_all_die), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // no actions need, but buttons need
                        }
                    });
            View view = snackbar.getView();
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.RED);
            snackbar.show();
        }
    }

    private InlineNumberPicker.OnValueChanged onRouteSizeChangedListener = new InlineNumberPicker.OnValueChanged() {
        @Override
        public void onChange(int newValue) {

        }
    };

    @OnClick(R.id.direction_backward)
    public void directionBackwardOnClick(View v) {
        setNewContainerDirection(Direction.BACKWARD);
    }

    @OnClick(R.id.direction_forward)
    public void directionForwardOnClick(View v) {
        setNewContainerDirection(Direction.FORWARD);
    }

    @OnClick(R.id.route_add)
    public void addRouteOnClick(View v) {
        mRoutesAdapter.getItems().add(new Route(getNewContainerDirection(), newRouteSizePicker.getValue(), 0));
        mRoutesAdapter.notifyDataSetChanged();
    }

    /**
     * Other
     */

    private void setNewContainerDirection(Direction direction) {
        switch (direction) {
            case BACKWARD:
                directionBackward.setActivated(true);
                directionForward.setActivated(false);
                break;
            case FORWARD:
                directionBackward.setActivated(false);
                directionForward.setActivated(true);
                break;
        }
    }

    private Direction getNewContainerDirection() {
        return directionBackward.isActivated() ? Direction.BACKWARD : Direction.FORWARD;
    }
}
