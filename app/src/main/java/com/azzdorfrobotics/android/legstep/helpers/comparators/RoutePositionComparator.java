package com.azzdorfrobotics.android.legstep.helpers.comparators;

import com.azzdorfrobotics.android.legstep.model.Route;

import java.util.Comparator;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class RoutePositionComparator implements Comparator<Route> {
    @Override
    public int compare(Route lhs, Route rhs) {
        return lhs.position - rhs.position;
    }
}
