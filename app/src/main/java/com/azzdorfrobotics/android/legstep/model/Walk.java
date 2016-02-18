package com.azzdorfrobotics.android.legstep.model;

import java.util.ArrayList;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Walk {

    private Creature mCreature;
    private Podium mPodium;

    public int currentRouteIndex;

    public Walk(Creature creature, Podium podium) {
        this.mCreature = creature;
        this.mPodium = podium;
    }

    public boolean setInitial() {
        return mPodium.initialState();
    }

    public boolean stepBackward() {
        int state = mPodium.getState();
        state -= mCreature.stepLength;
        mPodium.getRoutes().get(currentRouteIndex).progress += mCreature.stepLength;
        return mPodium.setState(state);
    }

    public boolean stepForward() {
        int state = mPodium.getState();
        state += mCreature.stepLength;
        mPodium.getRoutes().get(currentRouteIndex).progress += mCreature.stepLength;
        return mPodium.setState(state);
    }

    public ArrayList<Route> getPodiumRoutes() {
        return mPodium.getRoutes();
    }

    public int getCreatureStepSize() {
        return mCreature.stepLength;
    }

    public ArrayList<Paw> getCreaturePaws() {
        return mCreature.paws;
    }

    public int getCreatureId() {
        return mCreature.id;
    }

    public boolean isRouteEnd() { //todo define rout end based on route length
        Route route = mPodium.getRoutes().get(currentRouteIndex);
        if ((route.progress + mCreature.stepLength) > route.length) {
            route.progress = 0;
            return true;
        }
        return false;
    }
}
