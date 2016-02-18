package com.azzdorfrobotics.android.legstep.repositories;

import com.azzdorfrobotics.android.legstep.model.Direction;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class RobotRepository extends Repository {

    private static final String ROBOT_ID = "RobotID";
    private static final String COMMAND = "Command";
    private static final String COMMAND_INIT = "Init";
    private static final String PAW_INDEX = "PawIndex";

    private static final String ENCODING_RESPONSE = "windows-1251";

    public void initRobot(int robotId, final RequestCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ROBOT_ID, String.valueOf(robotId));
        params.add(COMMAND, COMMAND_INIT);
        get("/", params, new TextHttpResponseHandler(ENCODING_RESPONSE) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String s, Throwable throwable) {
                callback.error(statusCode, s);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String s) {
                callback.success(s);
            }
        });
    }

    public void moveRobotPaw(int robotId, Direction direction, int pawIndex, final RequestCallback callback) {
        RequestParams params = new RequestParams();
        params.add(ROBOT_ID, String.valueOf(robotId));
        params.add(COMMAND, direction.toString());
        params.add(PAW_INDEX, String.valueOf(pawIndex));
        get("/", params, new TextHttpResponseHandler(ENCODING_RESPONSE) {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.error(statusCode, responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                callback.success(responseString);
            }
        });
    }


}
