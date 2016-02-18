package com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.adapters;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azzdorfrobotics.android.legstep.R;
import com.azzdorfrobotics.android.legstep.model.Route;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.ItemTouchHelperViewHolder;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.OnRemoveItemListener;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class RoutesRecyclerListAdapter
        extends RecyclerView.Adapter<RoutesRecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private ArrayList<Route> mRoutes = new ArrayList<>();
    private final OnStartDragListener mDragStartListener;
    private final OnRemoveItemListener mOnRemoveItemListener;
    public RecyclerView recyclerView;

    public RoutesRecyclerListAdapter(OnStartDragListener dragStartListener,
                                     OnRemoveItemListener<Route> removeItemListener,
                                     RecyclerView recyclerView, ArrayList<Route> items) {
        this.mDragStartListener = dragStartListener;
        this.mOnRemoveItemListener = removeItemListener;
        this.recyclerView = recyclerView;
        setItems(items);
    }

    public ArrayList<Route> getItems() {
        return mRoutes;
    }

    public void setItems(ArrayList<Route> items) {
        this.mRoutes.clear();
        this.mRoutes.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Click listeners here!!!
     * Always check for NO_POSITION
     * {pos = holder.getAdapterPosition; pos != NO_POSITION}
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routes_list, parent, false);
        return new ItemViewHolder(container);
    }

    /**
     * NO position final!!! USE holder.getAdapterPosition()!!!
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        switch (mRoutes.get(holder.getAdapterPosition()).direction) {
            case BACKWARD:
                holder.direction.setImageResource(R.drawable.ic_direction_backward);
                break;
            case FORWARD:
                holder.direction.setImageResource(R.drawable.ic_direction_forward);
                break;
        }
        holder.content.setText(String.valueOf(mRoutes.get(holder.getAdapterPosition()).length));
        holder.handleDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, boolean force) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mRoutes, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mRoutes, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        if (position != RecyclerView.NO_POSITION) {
            final Route routeToRemove = mRoutes.get(position);
            mOnRemoveItemListener.onItemRemoved(routeToRemove);
            mRoutes.remove(routeToRemove);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void onItemChanged(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void onItemInserted(int position) {
        notifyItemInserted(position);
    }

    /**
     * ViewHolder
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public RelativeLayout container;
        public ImageView direction;
        public TextView content;
        public ImageView handleDrag;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView;
            direction = (ImageView) itemView.findViewById(R.id.direction);
            content = (TextView) itemView.findViewById(R.id.content);
            handleDrag = (ImageView) itemView.findViewById(R.id.handle_drag);
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }
}
