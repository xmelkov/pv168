package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Samuel on 14.03.2017.
 */
public class AssignmentManagerImplTest {
    private AssignmentManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new AssignmentManagerImpl();
    }

    @Test
    public void createAssignment() throws Exception {
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(LocalDateTime.now(),
                LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4), LocalTime.of(19, 0)), mufasa, defeatScar);

        manager.createAssignment(assignment);
        Long assignmentId = assignment.getId();
        assertNotNull(assignmentId);
        Assignment result = manager.findAssignmentById(assignmentId);
        assertEquals(assignment, result);
        assertNotSame(assignment, result);
        assertDeepEquals(assignment, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullStart() {
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(null, LocalDateTime.now(), mufasa, defeatScar);

        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullEnd() {
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(LocalDateTime.now(), null, mufasa, defeatScar);

        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullAgent() {
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(LocalDateTime.now(), LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4),
                LocalTime.of(19, 0)), null, defeatScar);

        manager.createAssignment(assignment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAssignmentWithNullMission() {
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);

        Assignment assignment = newAssignment(LocalDateTime.now(), LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4),
                LocalTime.of(19, 0)), mufasa, null);

        manager.createAssignment(assignment);
    }

    @Test
    public void findAllAssignments() throws Exception {
        assertTrue(manager.findAllAssignments().isEmpty());
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(LocalDateTime.now(),
                LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4), LocalTime.of(19, 0)), mufasa, defeatScar);
        manager.createAssignment(assignment);

        assertFalse(manager.findAllAssignments().isEmpty());

        Agent simba = newAgent("Simba", (short)3, (short)4, "777 777 777", true);
        Mission theReturnOfScar = newMission(
                "Simba is here to avenge his dead father",
                (short)1, 10, "Lion King", true);

        Assignment avengeMufasa = newAssignment(LocalDateTime.now(),
                LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4), LocalTime.of(19, 0)), simba, theReturnOfScar);

        manager.createAssignment(avengeMufasa);

        int expectedSize = 2;

        assertEquals(manager.findAllAssignments().size(), expectedSize);

        List<Assignment> assignments = new ArrayList<>();
        assignments.add(assignment);
        assignments.add(avengeMufasa);

        assertDeepEquals(assignments, manager.findAllAssignments());
    }

    @Test
    public void updateAssignment() throws Exception {
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(LocalDateTime.now(),
                LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4), LocalTime.of(19, 0)), mufasa, defeatScar);

        manager.createAssignment(assignment);

        Agent simba = newAgent("Simba", (short)3, (short)4, "777 777 777", true);

        Assignment update1 = newAssignment(assignment);
        update1.setAgent(simba);
        manager.updateAssignment(update1);
        assertDeepEquals(update1, manager.findAssignmentById(update1.getId()));

        Mission theReturnOfScar = newMission(
                "Simba is here to avenge his dead father",
                (short)1, 10, "Lion King", true);

        Assignment update2 = newAssignment(update1);
        update2.setMission(theReturnOfScar);
        manager.updateAssignment(update2);
        assertDeepEquals(update2, manager.findAssignmentById(update2.getId()));

        Assignment update3 = newAssignment(update2);
        update3.setEnd(LocalDateTime.now());
        manager.updateAssignment(update3);
        assertDeepEquals(update3, manager.findAssignmentById(update3.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullAssignment() {
        manager.updateAssignment(null);
    }

    @Test
    public void deleteAssignment() throws Exception {
        assertTrue(manager.findAllAssignments().isEmpty());
        Agent mufasa = newAgent("Mufasa", (short)3, (short)9, "999 999 999", true);
        Mission defeatScar = newMission(
                "Scar is plotting against the one and only great Mufasa and Mufasa has to stop him at all cost",
                (short)1, 9, "Lion King", true);

        Assignment assignment = newAssignment(LocalDateTime.now(),
                LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 4), LocalTime.of(19, 0)), mufasa, defeatScar);

        manager.createAssignment(assignment);
        assertFalse(manager.findAllAssignments().isEmpty());
        assertDeepEquals(assignment, manager.findAssignmentById(assignment.getId()));
        manager.deleteAssignment(assignment);
        assertTrue(manager.findAllAssignments().isEmpty());
    }

    private static Agent newAgent(String name, short gender, short age, String phoneNumber, boolean alive) {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setGender(gender);
        agent.setAge(age);
        agent.setPhoneNumber(phoneNumber);
        agent.setAlive(alive);
        return agent;
    }

    private static Agent newAgent(Agent originalAgent) {
        return newAgent(originalAgent.getName(),originalAgent.getGender(),originalAgent.getAge(),
                originalAgent.getPhoneNumber(),originalAgent.isAlive());
    }

    private static Mission newMission(Mission originalMission) {
        return newMission(originalMission.getDescription(),
                originalMission.getNumberOfRequiredAgents(),originalMission.getDifficulty(),originalMission.getPlace(),
                originalMission.isSuccessful());
    }

    private static Mission newMission(String description, short requiredAgents,
                                      int difficulty,String place,boolean successful) {
        Mission mission = new Mission();
        mission.setDescription(description);
        mission.setNumberOfRequiredAgents(requiredAgents);
        mission.setDifficulty(difficulty);
        mission.setPlace(place);
        mission.setSuccessful(successful);
        return mission;
    }

    private static Assignment newAssignment(LocalDateTime start, LocalDateTime end, Agent agent, Mission mission) {
        Assignment assignment = new Assignment();
        assignment.setStart(start);
        assignment.setEnd(end);
        assignment.setAgent(newAgent(agent));
        assignment.setMission(newMission(mission));
        return assignment;
    }

    private static Assignment newAssignment(Assignment originalAssignment) {
        return newAssignment(originalAssignment.getStart(), originalAssignment.getEnd(),
                originalAssignment.getAgent(), originalAssignment.getMission());
    }

    private void assertDeepEquals(Assignment originalAssignment, Assignment expectedAssignment) {
        assertNotNull(expectedAssignment);
        assertEquals(originalAssignment.getId(),expectedAssignment.getId());
        assertEquals(originalAssignment.getStart(),expectedAssignment.getStart());
        assertEquals(originalAssignment.getEnd(),expectedAssignment.getEnd());
        assertEquals(originalAssignment.getAgent(),expectedAssignment.getAgent());
        assertEquals(originalAssignment.getMission(),expectedAssignment.getMission());
    }

    private void assertDeepEquals(List<Assignment> expectedList, List<Assignment> originalList) {
        assertEquals(expectedList.size(),originalList.size());
        for (int i = 0 ; i < originalList.size() ; ++i) {
            assertDeepEquals(expectedList.get(i),originalList.get(i));
        }
    }
}