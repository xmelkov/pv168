package cz.muni.fi.common;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;

/**
 * Created by Matúš on 28.3.2017.
 */
public class DBUtils {
    public static void validate(Agent agent) throws IllegalArgumentException, ValidationException{
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }

        if (agent.getName() == null) {
            throw new ValidationException("name is null");
        }

        if (agent.getAge() < 18) {
            throw new ValidationException("agent age is under 18");
        }

        if (agent.getGender() < 0) {
            throw new ValidationException("gender is negative");
        }

        if (agent.getPhoneNumber() == null) {
            throw new ValidationException("phone number is null");
        }

        if (agent.getPhoneNumber().isEmpty()) {
            throw new ValidationException("phone number is empty");
        }

        if (agent.getName().isEmpty()) {
            throw new ValidationException("name is empty");
        }
    }

    public static void validate(Mission mission) throws IllegalArgumentException, ValidationException{

    }

    public static void validate(Assignment assignment) throws IllegalArgumentException, ValidationException{
        if (assignment == null) {
            throw new IllegalArgumentException("assignment is null");
        }

        if (assignment.getAgent() == null) {
            throw new ValidationException("agent is null");
        }

        if (assignment.getEnd() == null) {
            throw new ValidationException("end of assignment is null");
        }

        if (assignment.getStart() == null) {
            throw new ValidationException("start of assignment is null");
        }

        if (assignment.getEnd().isBefore(assignment.getStart())) {
            throw new ValidationException("end is before start");
        }

        if (assignment.getMission() == null) {
            throw new ValidationException("mission is null");
        }
    }
}
