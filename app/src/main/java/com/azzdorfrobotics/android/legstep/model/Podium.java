package com.azzdorfrobotics.android.legstep.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Podium {

    @SerializedName("length")
    public int length;
    @SerializedName("routes")
    private ArrayList<Route> routes;

    private transient int state;

    public Podium() {
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public boolean setRoutes(ArrayList<Route> routes) {
        if (isValid(routes)) {
            this.routes = routes;
            return true;
        }
        return false;
    }

    public boolean isValid(ArrayList<Route> routes) {
        if (length < 1) {
            return false;
        }
        initialState();
        for (Route route : routes) {
            switch (route.direction) {
                case FORWARD:
                    this.state += route.length;
                    break;
                case BACKWARD:
                    this.state -= route.length;
                    break;
            }
            if (this.state < 0 || this.state > this.length) {
                return false;
            }
        }
        return true;
    }

    public boolean initialState() {
        this.state = 0;
        return true;
    }

    public int getState() {
        return this.state;
    }

    public boolean setState(int state) {
        if (state < 0 || state > this.length) {
            return false;
        }
        this.state = state;
        return true;
    }

    public void setDefault() {
        this.length = 5;
        this.routes = new ArrayList<>();
        this.routes.add(new Route(Direction.FORWARD, this.length, 0));
        this.routes.add(new Route(Direction.BACKWARD, this.length, 1));
        this.routes.add(new Route(Direction.FORWARD, this.length, 2));
        this.routes.add(new Route(Direction.BACKWARD, this.length, 3));
    }
}
