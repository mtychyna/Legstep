package com.azzdorfrobotics.android.legstep.controllers;

import com.azzdorfrobotics.android.legstep.model.Direction;

/**
 * Created on 18.02.2016
 * Listener for actions on walk state change
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public interface OnWalkStateChangedListener {

    void returnToInitial();

    void move(Direction direction, int position);

    void atPosition(int position);

    void walkEnd();

    void errorRised(String response);
}
