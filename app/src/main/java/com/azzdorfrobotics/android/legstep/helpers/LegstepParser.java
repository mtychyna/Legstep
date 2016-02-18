package com.azzdorfrobotics.android.legstep.helpers;

import com.azzdorfrobotics.android.legstep.model.Direction;
import com.azzdorfrobotics.android.legstep.model.ServerRobotState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 18.02.2016
 * Parse server response and get valid ServerRobotState object
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class LegstepParser {

    public static final String INFO_MATCH = "[info]";
    public static final String ERROR_MATCH = "[error]";
    public static final String INITIAL_MATCH = "в исходную позицию";
    public static final String DIRECTION_MOVE = "движ";
    public static final String DIRECTION_STATE = "проходит";
    public static final String DIRECTION_B_MATCH = "назад";
    public static final String DIRECTION_F_MATCH = "вперед";
    public static final String POSITION_MATCH = "Позиция:";

    public static ServerRobotState parseServerResponse(String response) {
        ServerRobotState robotState = new ServerRobotState();

        if (LegstepParser.isErrorMatch(response)) {
            robotState.setResponseType(ServerRobotState.ResponseType.ERROR);
        } else if (LegstepParser.isInfoMatch(response)) {
            if (LegstepParser.isInitialMatch(response)) {
                robotState.setResponseType(ServerRobotState.ResponseType.INITIAL);
            } else if (LegstepParser.isStateMatch(response)) {
                robotState.setResponseType(ServerRobotState.ResponseType.STATE);
                robotState.position = LegstepParser.getPositiom(response);
            } else if (LegstepParser.isMoveMatch(response)) {
                robotState.setResponseType(ServerRobotState.ResponseType.PAW_MOVE);
                if (LegstepParser.isMoveBackwardMatch(response)) {
                    robotState.direction = Direction.BACKWARD;
                } else if (LegstepParser.isMoveForwardMatch(response)) {
                    robotState.direction = Direction.FORWARD;
                }
                robotState.position = LegstepParser.getPositiom(response);
            }
        }
        return robotState;
    }

    public static boolean isErrorMatch(String response) {
        return response.contains(ERROR_MATCH);
    }

    public static boolean isInfoMatch(String response) {
        return response.contains(INFO_MATCH);
    }

    public static boolean isInitialMatch(String response) {
        return response.contains(INITIAL_MATCH);
    }

    public static boolean isMoveMatch(String response) {
        return response.contains(DIRECTION_MOVE);
    }

    public static boolean isStateMatch(String response) {
        return response.contains(DIRECTION_STATE);
    }

    public static boolean isMoveBackwardMatch(String response) {
        return response.contains(DIRECTION_B_MATCH);
    }

    public static boolean isMoveForwardMatch(String response) {
        return response.contains(DIRECTION_F_MATCH);
    }

    public static int getPositiom(String response) {
        int result = -1;
        if (response.contains(POSITION_MATCH)) {
            int from = response.indexOf(POSITION_MATCH) + POSITION_MATCH.length();
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(response.substring(from));
            try {
                if(matcher.find()) {
                    result = Integer.parseInt(matcher.group());
                }
            } catch (NumberFormatException nfe) {
                return result;
            }
        }
        return result;
    }


}
