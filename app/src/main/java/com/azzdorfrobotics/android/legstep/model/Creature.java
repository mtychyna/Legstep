package com.azzdorfrobotics.android.legstep.model;

import com.azzdorfrobotics.android.legstep.BaseActivity;
import com.azzdorfrobotics.android.legstep.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Creature {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("type")
    public String type;
    @SerializedName("step_length")
    public int stepLength;
    @SerializedName("paws")
    public ArrayList<Paw> paws;

    public Creature() {
        this.paws = new ArrayList<>();
    }

    public int getNextPawIndex() {
        int index = 0;
        if (paws != null && paws.size() > 0) {
            return paws.size();
        }
        return index;
    }

    public void setDefault() {
        this.id = 10;
        this.title = BaseActivity.getContext().getString(R.string.creature_default_title);
        this.type = BaseActivity.getContext().getString(R.string.creature_default_type);
        this.stepLength = 1;
        this.paws = new ArrayList<>();
        this.paws.add(new Paw(0, 0));
        this.paws.add(new Paw(2, 1));
        this.paws.add(new Paw(1, 2));
        this.paws.add(new Paw(3, 3));
    }
}
