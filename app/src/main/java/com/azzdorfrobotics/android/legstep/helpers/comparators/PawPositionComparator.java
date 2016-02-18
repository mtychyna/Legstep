package com.azzdorfrobotics.android.legstep.helpers.comparators;

import com.azzdorfrobotics.android.legstep.model.Paw;

import java.util.Comparator;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class PawPositionComparator implements Comparator<Paw> {
    @Override
    public int compare(Paw lhs, Paw rhs) {
        return lhs.position - rhs.position;
    }
}
