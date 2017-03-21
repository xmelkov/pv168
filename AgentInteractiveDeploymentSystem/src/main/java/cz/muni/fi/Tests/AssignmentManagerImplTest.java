package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Samuel on 14.03.2017.
 */
public class AssignmentManagerImplTest {
    private AssignmentManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new AssignmentManagerImpl();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final static LocalDateTime START
            = LocalDateTime.of(2016, Month.FEBRUARY, 29, 14, 00);

    private final static LocalDateTime END
            = LocalDateTime.of(2017, Month.MARCH, 14, 14, 00);

    private MissionBuilder noChinMission() {
        return new MissionBuilder()
                .id(1L)
                .description("Eliminate mysterious inteloper with no chin")
                .numberOfRequiredAgents((short) 1)
                .difficulty(100)
                .place("Seattle")
                .successful(true);
    }

    private MissionBuilder prankMission() {
        return new MissionBuilder()
                .id(2L)
                .description("Prank some1 really good")
                .numberOfRequiredAgents((short) 3)
                .difficulty(44)
                .place("Poland")
                .successful(true);
    }

    private AgentBuilder agentKeemstar() {
        return new AgentBuilder()
                .id(1L)
                .name("Keemstar")
                .gender((short) 6)
                .age((short) 36)
                .phoneNumber("+421 999 888 777")
                .alive(true);
    }

    private AgentBuilder agentShrek() {
        return new AgentBuilder()
                .id(2L)
                .name("Shrek")
                .gender((short) 4)
                .age((short) 19)
                .phoneNumber("0123456788")
                .alive(true);
    }

    private AssignmentBuilder shrekPrankAssignment() {
        return new AssignmentBuilder()
                .id(null)
                .start(START)
                .end(END)
                .agent(agentShrek().build())
                .mission(prankMission().build());
    }

    private AssignmentBuilder keemstarNoChinAssignment() {
        return new AssignmentBuilder()
                .id(null)
                .start(START)
                .end(END)
                .agent(agentKeemstar().build())
                .mission(noChinMission().build());
    }

    @Test
    public void createAssignment() throws Exception {
        Assignment assignment = shrekPrankAssignment().build();
        manager.createAssignment(assignment);

        assertThat(assignment.getId()).isEqualTo(1L);

        assertThat(assignment)
                .isEqualTo(manager.findAssignmentById(assignment.getId()))
                .isNotSameAs(manager.findAssignmentById(assignment.getId()))
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignment.getId()));
    }

    @Test
    public void createAssignmentWithNullStart() {
        expectedException.expect(IllegalArgumentException.class);
        Assignment assignment = shrekPrankAssignment().start(null).build();
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullEnd() {
        expectedException.expect(IllegalArgumentException.class);
        Assignment assignment = shrekPrankAssignment().end(null).build();
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullAgent() {
        expectedException.expect(IllegalArgumentException.class);
        Assignment assignment = shrekPrankAssignment().agent(null).build();
        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullMission() {
        expectedException.expect(IllegalArgumentException.class);
        Assignment assignment = shrekPrankAssignment().mission(null).build();
        manager.createAssignment(assignment);
    }

    @Test
    public void findAllAssignments() throws Exception {
        assertThat(manager.findAllAssignments()).isEmpty();

        Assignment assignmentA = keemstarNoChinAssignment().build();
        manager.createAssignment(assignmentA);

        assertThat(manager.findAllAssignments()).isNotEmpty();

        Assignment assignmentB = shrekPrankAssignment().build();
        manager.createAssignment(assignmentB);

        assertThat(manager.findAllAssignments().size()).isEqualTo(2);
        assertThat(manager.findAllAssignments())
                .usingFieldByFieldElementComparator()
                .containsOnly(assignmentA, assignmentB);
    }

    @Test
    public void updateAssignment() throws Exception {
        Assignment assignmentToBeUpdated = shrekPrankAssignment().build();
        Assignment assignmentToBeUnchanged = keemstarNoChinAssignment().build();

        manager.createAssignment(assignmentToBeUpdated);
        manager.createAssignment(assignmentToBeUnchanged);

        assignmentToBeUpdated.setAgent(agentKeemstar().build());
        manager.updateAssignment(assignmentToBeUpdated);

        assertThat(assignmentToBeUpdated)
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUpdated.getId()));

        assignmentToBeUpdated.setMission(noChinMission().build());
        manager.updateAssignment(assignmentToBeUpdated);
        assertThat(assignmentToBeUpdated)
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUpdated.getId()));

        assignmentToBeUpdated.setEnd(START);
        manager.updateAssignment(assignmentToBeUpdated);
        assertThat(assignmentToBeUpdated)
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUpdated.getId()));

        assertThat(assignmentToBeUnchanged)
                .isEqualToComparingFieldByField(keemstarNoChinAssignment().build())
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUnchanged.getId()));
    }

    @Test
    public void updateNullAssignment() {
        expectedException.expect(IllegalArgumentException.class);
        manager.updateAssignment(null);
    }

    @Test
    public void deleteAssignment() throws Exception {
        Assignment assignmentToBeDeleted = keemstarNoChinAssignment().build();
        Assignment assignmentToBeUnchanged = shrekPrankAssignment().build();

        manager.createAssignment(assignmentToBeDeleted);
        manager.createAssignment(assignmentToBeUnchanged);

        assertThat(manager.findAllAssignments().size()).isEqualTo(2);

        manager.deleteAssignment(assignmentToBeDeleted);

        assertThat(manager.findAllAssignments())
                .usingFieldByFieldElementComparator()
                .containsOnly(assignmentToBeUnchanged);
        assertThat(manager.findAllAssignments().size()).isEqualTo(1);
    }
}