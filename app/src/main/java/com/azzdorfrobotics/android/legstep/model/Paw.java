package com.azzdorfrobotics.android.legstep.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Paw {

    @SerializedName("paw")
    public int index;
    @SerializedName("position")
    public int position;

    public Paw() {
    }

    public Paw(int index, int position) {
        this.index = index;
        this.position = position;
    }
}
