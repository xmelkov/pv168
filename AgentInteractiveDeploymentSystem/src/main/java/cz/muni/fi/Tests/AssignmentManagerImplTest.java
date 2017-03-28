package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;
import cz.muni.fi.common.ValidationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.time.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

/**
 * Created by Samuel on 14.03.2017.
 */
public class AssignmentManagerImplTest {
    private EmbeddedDatabase db;
    private AssignmentManagerImpl manager;
    private AgentManagerImpl agentManager;
    private MissionManagerImpl missionManager;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder().setType(DERBY).addScript("my-schema.sql").build();
        agentManager = new AgentManagerImpl();
        agentManager.setDataSource(db);
        missionManager = new MissionManagerImpl();
        missionManager.setDataSource(db);
        manager = new AssignmentManagerImpl();
        manager.setAgentManager(agentManager);
        manager.setMissionManager(missionManager);
        manager.setDataSource(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final static LocalDateTime START
            = LocalDateTime.of(2016, Month.FEBRUARY, 29, 14, 00);

    private final static LocalDateTime END
            = LocalDateTime.of(2017, Month.MARCH, 14, 14, 00);

    private MissionBuilder noChinMission() {
        return new MissionBuilder()
                .description("Eliminate mysterious inteloper with no chin")
                .numberOfRequiredAgents((short) 1)
                .difficulty(100)
                .place("Seattle")
                .successful(true);
    }

    private MissionBuilder prankMission() {
        return new MissionBuilder()
                .description("Prank some1 really good")
                .numberOfRequiredAgents((short) 3)
                .difficulty(44)
                .place("Poland")
                .successful(true);
    }

    private AgentBuilder agentKeemstar() {
        return new AgentBuilder()
                .name("Keemstar")
                .gender((short) 6)
                .age((short) 36)
                .phoneNumber("+421 999 888 777")
                .alive(true);
    }

    private AgentBuilder agentShrek() {
        return new AgentBuilder()
                .name("Shrek")
                .gender((short) 4)
                .age((short) 19)
                .phoneNumber("0123456788")
                .alive(true);
    }

    private AssignmentBuilder shrekPrankAssignment() {
        Agent shrek = agentShrek().build();
        Mission prank = prankMission().build();
        agentManager.createAgent(shrek);
        missionManager.createMission(prank);
        return new AssignmentBuilder()
                .id(null)
                .start(START)
                .end(END)
                .agent(shrek)
                .mission(prank);
    }

    private AssignmentBuilder keemstarNoChinAssignment() {
        Agent keemstar = agentKeemstar().build();
        Mission noChin = noChinMission().build();
        agentManager.createAgent(keemstar);
        missionManager.createMission(noChin);
        return new AssignmentBuilder()
                .id(null)
                .start(START)
                .end(END)
                .agent(keemstar)
                .mission(noChin);
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
        expectedException.expect(ValidationException.class);
        Assignment assignment = shrekPrankAssignment().start(null).build();
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullEnd() {
        expectedException.expect(ValidationException.class);
        Assignment assignment = shrekPrankAssignment().end(null).build();
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullAgent() {
        expectedException.expect(ValidationException.class);
        Assignment assignment = shrekPrankAssignment().agent(null).build();
        manager.createAssignment(assignment);
    }

    @Test
    public void createAssignmentWithNullMission() {
        expectedException.expect(ValidationException.class);
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

        Agent keemstar = agentKeemstar().build();
        agentManager.createAgent(keemstar);

        assignmentToBeUpdated.setAgent(keemstar);
        manager.updateAssignment(assignmentToBeUpdated);

        assertThat(assignmentToBeUpdated)
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUpdated.getId()));

        Mission noChin = noChinMission().build();
        missionManager.createMission(noChin);

        assignmentToBeUpdated.setMission(noChin);
        manager.updateAssignment(assignmentToBeUpdated);
        assertThat(assignmentToBeUpdated)
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUpdated.getId()));

        assignmentToBeUpdated.setEnd(START);
        manager.updateAssignment(assignmentToBeUpdated);
        assertThat(assignmentToBeUpdated)
                .isEqualToComparingFieldByField(manager.findAssignmentById(assignmentToBeUpdated.getId()));

        assertThat(assignmentToBeUnchanged)
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