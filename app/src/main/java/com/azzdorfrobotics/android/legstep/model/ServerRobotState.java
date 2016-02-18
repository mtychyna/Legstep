package com.azzdorfrobotics.android.legstep.model;

/**
 * Created on 18.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class ServerRobotState {

    public enum ResponseType {
        ERROR, INITIAL, PAW_MOVE, STATE
    }

    private ResponseType mResponseType;
    public Direction direction;
    public int position;

    public ServerRobotState() {
    }

    public ResponseType getResponseType() {
        return mResponseType;
    }

    public void setResponseType(ResponseType responseType) {
        switch (responseType) {
            case ERROR:
            case INITIAL:
                direction = null;
                position = 0;
                break;
            case STATE:
                direction = null;
                break;
        }
        mResponseType = responseType;
    }
}
