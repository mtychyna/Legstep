package com.azzdorfrobotics.android.legstep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.azzdorfrobotics.android.legstep.R;
import com.azzdorfrobotics.android.legstep.model.OptionMenuItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 26/01/2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class OptionMenuItemAdapter extends ArrayAdapter<OptionMenuItem> {

    public static class ViewHolder {
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public OptionMenuItemAdapter(Context context, ArrayList<OptionMenuItem> menuItems) {
        super(context, R.layout.adapter_option_menu_list_item, menuItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        OptionMenuItem menuItem = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_option_menu_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(menuItem.title);
        if (menuItem.langItem) {
            viewHolder.icon.setImageResource(menuItem.imageId);
            viewHolder.icon.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
