package cz.muni.fi.ManagersImpl;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.Managers.AssignmentManager;

import java.util.List;

/**
 * Created by Samuel on 08.03.2017.
 */
public class AssignmentManagerImpl implements AssignmentManager {

    @Override
    public void createAssignment(Assignment assignment) {

    }

    @Override
    public Assignment findAssignmentById(long assignmentId) {
        return null;
    }

    @Override
    public List<Assignment> findAllAssignments() {
        return null;
    }

    @Override
    public void updateAssignment(Assignment assignment) {

    }

    @Override
    public void deleteAssignment(Assignment assignment) {

    }

    @Override
    public List<Assignment> findAllAssignmentsForAgent(Agent agent) {
        return null;
    }

    @Override
    public List<Assignment> findAllAssignmentsForMission(Mission mission) {
        return null;
    }
}
