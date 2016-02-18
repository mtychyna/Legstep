package com.azzdorfrobotics.android.legstep.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Route {

    @SerializedName("direction")
    public Direction direction;
    @SerializedName("length")
    public int length;
    @SerializedName("position")
    public int position;

    public transient int progress;

    public Route() {
        this.direction = Direction.FORWARD;
    }

    public Route(Direction direction, int length, int position) {
        this.direction = direction;
        this.length = length;
        this.position = position;
    }
}
