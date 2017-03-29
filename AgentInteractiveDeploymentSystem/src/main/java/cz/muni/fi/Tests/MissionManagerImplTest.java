package cz.muni.fi.Tests;

import com.sun.javaws.exceptions.InvalidArgumentException;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;
import cz.muni.fi.common.ValidationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

/**
 * Created by Samuel on 14.03.2017.
 */
public class MissionManagerImplTest {
    private EmbeddedDatabase db;
    private MissionManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder().setType(DERBY).addScript("my-schema.sql").build();
        manager = new MissionManagerImpl();
        manager.setDataSource(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MissionBuilder noChinMission() {
        return new MissionBuilder()
                .id(null)
                .description("Eliminate mysterious inteloper with no chin")
                .numberOfRequiredAgents((short) 1)
                .difficulty(100)
                .place("Seattle")
                .successful(true);
    }

    private MissionBuilder prankMission() {
        return new MissionBuilder()
                .id(null)
                .description("Prank some1 really good")
                .numberOfRequiredAgents((short) 3)
                .difficulty(44)
                .place("Poland")
                .successful(true);
    }

    private MissionBuilder chromosomeMission() {
        return new MissionBuilder()
                .id(null)
                .description("Chin-chin is not pleased and requires sacrifice in form of cake. " +
                        "To do so we need chromosomes")
                .numberOfRequiredAgents((short) 1)
                .difficulty(10)
                .place("Franku's apartment")
                .successful(false);
    }

    private MissionBuilder pizzaInvestigation() {
        return new MissionBuilder()
                .id(null)
                .description("A mysterious entity has been found as content of pizza in academic canteen. " +
                        "The presumed origin of the food is mexico, so involvement of a drug cartel is taken " +
                        "into account.")
                .numberOfRequiredAgents((short) 10)
                .difficulty(84)
                .place("Mexico")
                .successful(false);
    }

    @Test
    public void createMission() throws Exception {
        assertThat(manager.findAllMissions()).isEmpty();
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        assertThat(manager.findAllMissions()).isNotEmpty();

        assertThat(mission.getId()).isEqualTo(1);

        assertThat(manager.findMissionById(mission.getId()))
                .isNotNull()
                .isNotSameAs(noChinMission().build())
                .isEqualToComparingFieldByField(noChinMission().id(1L).build());
    }

    @Test
    public void createMissionWithNullDescription() {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().description(null).build();
        manager.createMission(mission);
    }


    @Test
    public void createMissionWithoutDescription() {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().description("").build();
        manager.createMission(mission);
    }

    @Test
    public void createMissionForIncorrectNumberOfAgentsBecauseOfReasonsBeyondHumanKnowledge() {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().numberOfRequiredAgents((short) -3).build();
        manager.createMission(mission);
    }

    @Test
    public void createMissionForNoAgents() {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().numberOfRequiredAgents((short) 0).build();
        manager.createMission(mission);
    }

    @Test
    public void createMissionWithNegativeDifficulty() {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().difficulty(-5).build();
        manager.createMission(mission);
    }

    @Test
    public void createMissionAtNoWhereWhichMakesObviouslyNoSenseThatsWhyWeHaveToTestItCauseItsOurJob () {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().place("").build();
        manager.createMission(mission);
    }

    @Test
    public void createMissionButThisTimeInsteadOfEmptyPlaceWellUseNullPlaceCoolMIRITE () {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().place(null).build();
        manager.createMission(mission);
    }

    @Test
    public void createNullMission () {
        expectedException.expect(IllegalArgumentException.class);
        manager.createMission(null);
    }

    @Test
    public void findMissionById () {
        Mission mission = pizzaInvestigation().build();
        manager.createMission(mission);
        assertThat(mission).isEqualToComparingFieldByField(manager.findMissionById(mission.getId()));
    }

    @Test
    public void findMissionByNullIdWhichIsClearMistakeObviousAF() {
        expectedException.expect(IllegalArgumentException.class);
        manager.findMissionById(null);
    }

    @Test
    public void findMissionWithNoExistingIdWhichShouldReturnNullCauseMissionDoesNotExistsWhatIsDeadMayNeverDieTheonGreyjoy() {
        Mission mission = chromosomeMission().build();
        manager.createMission(mission);
        assertNull(manager.findMissionById(mission.getId() + 1));
    }

    @Test
    public void findAllMissionsEmpty() {
        assertTrue(manager.findAllMissions().isEmpty());
    }

    @Test
    public void findAllMissionsNotEmptyBecauseNaoItShouldContainAnEntryThusMakingItNotEmptyAndNotEmptyGetIt () {
        Mission mission = prankMission().build();
        manager.createMission(mission);
        assertFalse(manager.findAllMissions().isEmpty());
    }

    @Test
    public void findAllMissionActuallyFindsAllMissionsCauseThatsWhatTheNameSays () {
        Mission mission1 = prankMission().build();
        Mission mission2 = pizzaInvestigation().build();
        Mission mission3 = noChinMission().build();

        manager.createMission(mission1);
        assertThat(manager.findAllMissions()).containsExactly(mission1);

        manager.createMission(mission2);
        assertThat(manager.findAllMissions().size()).isEqualTo(2);
        assertThat(manager.findAllMissions()).usingFieldByFieldElementComparator().containsOnly(mission1,mission2);

        manager.createMission(mission3);
        assertThat(manager.findAllMissions().size()).isEqualTo(3);
        assertThat(manager.findAllMissions()).usingFieldByFieldElementComparator()
                .containsOnly(mission1,mission2,mission3);
    }

    @Test
    public void updateDescription() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        mission.setDescription("Arrest Calvin Vail for cyber-bullying");
        manager.updateMission(mission);
        assertThat(mission).isEqualToComparingFieldByField(manager.findMissionById(mission.getId()));
    }

    @Test
    public void updateEmptyDescription() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        expectedException.expect(ValidationException.class);
        mission.setDescription("");
        manager.updateMission(mission);
    }

    @Test
    public void updateNegativeNumberOfRequiredAgents() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        expectedException.expect(ValidationException.class);
        mission.setNumberOfRequiredAgents((short)-(mission.getNumberOfRequiredAgents()));
        manager.updateMission(mission);
    }

    @Test
    public void updateDifficulty() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        mission.setDifficulty(55);
        manager.updateMission(mission);
        assertThat(mission).isEqualToComparingFieldByField(manager.findMissionById(mission.getId()));
    }

    @Test
    public void updateNegativeDifficulty() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        expectedException.expect(ValidationException.class);
        mission.setDifficulty(-1);
        manager.updateMission(mission);
    }

    @Test
    public void updatePlace() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        mission.setPlace("Goldshire, Lyo's pride inn, Elwynn Forest");
        manager.updateMission(mission);
        assertThat(mission).isEqualToComparingFieldByField(manager.findMissionById(mission.getId()));
    }

    @Test
    public void updateEmptyPlace() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        expectedException.expect(ValidationException.class);
        mission.setPlace("");
        manager.updateMission(mission);
    }

    @Test
    public void updateSuccessful() {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        mission.setSuccessful(!mission.isSuccessful());
        manager.updateMission(mission);
        assertThat(mission).isEqualToComparingFieldByField(manager.findMissionById(mission.getId()));
    }

    @Test
    public void updateNullMission() {
        expectedException.expect(IllegalArgumentException.class);
        manager.updateMission(null);
    }

    @Test
    public void deleteMission() {
        Mission missionToBeDeleted = pizzaInvestigation().build();
        Mission missionUnchanged = chromosomeMission().build();

        manager.createMission(missionToBeDeleted);
        manager.createMission(missionUnchanged);

        assertThat(manager.findAllMissions().size()).isEqualTo(2);
        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(missionToBeDeleted,missionUnchanged);

        manager.deleteMission(missionToBeDeleted);

        assertThat(manager.findAllMissions().size()).isEqualTo(1);
        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(missionUnchanged);
    }

    @Test
    public void deleteFromEmpty() {
        Mission mission = pizzaInvestigation().build();
        assertTrue(manager.findAllMissions().isEmpty());
        manager.deleteMission(mission);
        assertTrue(manager.findAllMissions().isEmpty());
    }

    @Test
    public void deleteNonExistingMission() {
        Mission saved = chromosomeMission().build();
        manager.createMission(saved);

        Mission unsaved = noChinMission().build();
        assertThat(manager.findAllMissions().size()).isEqualTo(1);
        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(saved);

        manager.deleteMission(unsaved);
        assertThat(manager.findAllMissions().size()).isEqualTo(1);
        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(saved);
    }

    @Test
    public void deleteLast() {
        Mission missionToBeDeleted = pizzaInvestigation().build();
        manager.createMission(missionToBeDeleted);

        assertThat(manager.findAllMissions().size()).isEqualTo(1);

        manager.deleteMission(missionToBeDeleted);
        assertTrue(manager.findAllMissions().isEmpty());
    }

    @Test
    public void deleteNull() {
        expectedException.expect(IllegalArgumentException.class);
        manager.deleteMission(null);
    }


}