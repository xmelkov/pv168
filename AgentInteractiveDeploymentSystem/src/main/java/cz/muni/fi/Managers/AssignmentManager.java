package cz.muni.fi.Managers;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;

import java.util.List;

/**
 * Created by xmelkov on 8.3.17.
 */
public interface AssignmentManager {
    void createAssignment(Assignment assignment);

    Assignment findAssignmentById(Long assignmentId);

    List<Assignment> findAllAssignments();

    void updateAssignment(Assignment assignment);

    void deleteAssignment(Assignment assignment);

    List<Assignment> findAllAssignmentsForAgent(Agent agent);

    List<Assignment> findAllAssignmentsForMission(Mission mission);

}
