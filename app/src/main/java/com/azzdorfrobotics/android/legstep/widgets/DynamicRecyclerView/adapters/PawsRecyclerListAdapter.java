package com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.adapters;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
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
import com.azzdorfrobotics.android.legstep.model.Paw;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.ItemTouchHelperViewHolder;
import com.azzdorfrobotics.android.legstep.widgets.DynamicRecyclerView.helpers.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class PawsRecyclerListAdapter
        extends RecyclerView.Adapter<PawsRecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private ArrayList<Paw> mPaws = new ArrayList<>();
    private final OnStartDragListener mDragStartListener;
    public RecyclerView recyclerView;

    public PawsRecyclerListAdapter(OnStartDragListener dragStartListener, RecyclerView recyclerView, ArrayList<Paw> items) {
        this.mDragStartListener = dragStartListener;
        this.recyclerView = recyclerView;
        setItems(items);
    }

    public ArrayList<Paw> getItems() {
        return mPaws;
    }

    public void setItems(ArrayList<Paw> items) {
        this.mPaws.clear();
        this.mPaws.addAll(items);
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
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_paws_list, parent, false);
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
        holder.content.setText(String.valueOf(mPaws.get(holder.getAdapterPosition()).index));
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
        return mPaws.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, boolean force) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mPaws, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mPaws, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        notifyItemRemoved(position);
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
        public TextView content;
        public ImageView handleDrag;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView;
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
