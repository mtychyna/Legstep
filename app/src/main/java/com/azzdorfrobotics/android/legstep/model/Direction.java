package com.azzdorfrobotics.android.legstep.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public enum Direction {

    @SerializedName("StepForward")
    FORWARD("StepForward"),

    @SerializedName("StepBackward")
    BACKWARD("StepBackward");

    private final String command;

    private Direction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }


    @Override
    public String toString() {
        return this.command;
    }
}
