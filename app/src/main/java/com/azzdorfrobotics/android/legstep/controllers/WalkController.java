package com.azzdorfrobotics.android.legstep.controllers;

import com.azzdorfrobotics.android.legstep.helpers.LegstepParser;
import com.azzdorfrobotics.android.legstep.model.Direction;
import com.azzdorfrobotics.android.legstep.model.Route;
import com.azzdorfrobotics.android.legstep.model.ServerRobotState;
import com.azzdorfrobotics.android.legstep.model.Walk;
import com.azzdorfrobotics.android.legstep.repositories.RequestCallback;
import com.azzdorfrobotics.android.legstep.repositories.RobotRepository;

/**
 * Created on 18.02.2016
 * Coordinate Walk state and Server State
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class WalkController {

    private Walk mWalk;
    private ServerRobotState mServerRobotState;
    private OnWalkStateChangedListener mListener;
    private RobotRepository mRobotRepository = new RobotRepository();

    private static WalkController sWalkController;

    private WalkController(OnWalkStateChangedListener listener, Walk walk) {
        this.mListener = listener;
        this.mWalk = walk;
    }

    public static WalkController getInstance(OnWalkStateChangedListener listener, Walk walk) {
        if (sWalkController == null) {
            sWalkController = new WalkController(listener, walk);
        }
        return sWalkController;
    }

    public static void destroyController(){
        sWalkController = null;
    }

    /**
     * General process control
     */

    public void startWalk() {
        mWalk.setInitial();
        mWalk.currentRouteIndex = 0;
        Route route = mWalk.getPodiumRoutes().get(mWalk.currentRouteIndex);
        switch (route.direction) {
            case BACKWARD:
                stepBackward(mWalk.getCreaturePaws().size(), 0);
                break;
            case FORWARD:
                stepForward(mWalk.getCreaturePaws().size(), 0);
                break;
        }
    }

    /**
     * Used for animation wait
     */
    public void resumeWalk() {
        if (mWalk.isRouteEnd()) {
            mWalk.currentRouteIndex++;
        }
        if (mWalk.currentRouteIndex > mWalk.getPodiumRoutes().size() - 1) {
            mListener.walkEnd();
            setInitial(false);
            return;
        }
        Route route = mWalk.getPodiumRoutes().get(mWalk.currentRouteIndex);
        switch (route.direction) {
            case BACKWARD:
                stepBackward(mWalk.getCreaturePaws().size(), 0);
                break;
            case FORWARD:
                stepForward(mWalk.getCreaturePaws().size(), 0);
                break;
        }
    }

    public void stopWalk() {
        setInitial(true);
    }


    /**
     * State change methods
     */

    private void setInitial(final boolean inform) {
        mRobotRepository.initRobot(mWalk.getCreatureId(), new RequestCallback() {

            @Override
            public void error(int code, String response) {
                super.error(code, response);
                mListener.errorRised(response);
            }

            @Override
            public void success(String response) {
                mServerRobotState = LegstepParser.parseServerResponse(response);
                switch (mServerRobotState.getResponseType()) {
                    case ERROR:
                        mListener.errorRised(response);
                        break;
                    case INITIAL:
                        if (inform) {
                            mListener.returnToInitial();
                        }
                        break;
                }
            }
        });
    }

    private void stepForward(int runs, int runNumber) {
        mRobotRepository.moveRobotPaw(mWalk.getCreatureId(), Direction.FORWARD, mWalk.getCreaturePaws().get(runNumber).index, new RequestCallback(runs, runNumber) {

            @Override
            public void error(int code, String response) {
                super.error(code, response);
                mListener.errorRised(response);
            }

            @Override
            public void success(String response) {
                super.success(response);
                mServerRobotState = LegstepParser.parseServerResponse(response);
                switch (mServerRobotState.getResponseType()) {
                    case ERROR:
                        mListener.errorRised(response);
                        break;
                    case PAW_MOVE:
                        mListener.move(Direction.FORWARD, mServerRobotState.position);
                        if (++this.recurtionOrder < this.recurtionDeep) {
                            stepForward(this.recurtionDeep, this.recurtionOrder);
                        }
                        break;
                    case STATE:
                        if (mWalk.stepForward()) {
                            mListener.atPosition(mServerRobotState.position);
                        }
                        break;
                }
            }
        });
    }

    private void stepBackward(int runs, int runNumber) {
        mRobotRepository.moveRobotPaw(mWalk.getCreatureId(), Direction.BACKWARD, mWalk.getCreaturePaws().get(runNumber).index, new RequestCallback(runs, runNumber) {

            @Override
            public void error(int code, String response) {
                super.error(code, response);
                mListener.errorRised(response);
            }

            @Override
            public void success(String response) {
                super.success(response);
                mServerRobotState = LegstepParser.parseServerResponse(response);
                switch (mServerRobotState.getResponseType()) {
                    case ERROR:
                        mListener.errorRised(response);
                        break;
                    case PAW_MOVE:
                        mListener.move(Direction.BACKWARD, mServerRobotState.position);
                        if (++this.recurtionOrder < this.recurtionDeep) {
                            stepBackward(this.recurtionDeep, this.recurtionOrder);
                        }
                        break;
                    case STATE:
                        if (mWalk.stepBackward()) {
                            mListener.atPosition(mServerRobotState.position);
                        }
                        break;
                }
            }
        });
    }

}
