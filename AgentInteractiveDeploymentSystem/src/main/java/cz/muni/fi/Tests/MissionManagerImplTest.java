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

import java.util.List;

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

    @Test
    public void createMission() throws Exception {
        Mission mission = noChinMission().build();
        manager.createMission(mission);

        assertThat(mission.getId()).isEqualTo(1);

        assertThat(manager.findMissionById(mission.getId()))
                .isNotNull()
                .isNotSameAs(noChinMission().build())
                .isEqualToComparingFieldByField(noChinMission().id(1L).build());
    }

    @Test
    public void createMissionWithoutDescription() {
        expectedException.expect(ValidationException.class);
        Mission mission = prankMission().description("").build();
        manager.createMission(mission);
    }

    @Test
    public void updateMission() throws Exception {
        Mission missionToBeUpdated = noChinMission().build();
        Mission missionToBeUnchanged = noChinMission().build();

        manager.createMission(missionToBeUpdated);
        manager.createMission(missionToBeUnchanged);

        assertThat(manager.findAllMissions().size()).isEqualTo(2);

        missionToBeUpdated.setDescription("Hey thats pretty good!");
        manager.updateMission(missionToBeUpdated);

        assertThat(missionToBeUpdated)
                .isEqualToComparingFieldByField(manager.findMissionById(missionToBeUpdated.getId()));

        missionToBeUpdated.setPlace("Afghanistan");
        manager.updateMission(missionToBeUpdated);

        assertThat(missionToBeUpdated)
                .isEqualToComparingFieldByField(manager.findMissionById(missionToBeUpdated.getId()));
        assertThat(missionToBeUnchanged)
                .isEqualToComparingFieldByField(manager.findMissionById(missionToBeUnchanged.getId()))
                .isEqualToComparingFieldByField(noChinMission().id(missionToBeUnchanged.getId()).build());
    }

    @Test
    public void deleteMission() throws Exception {
        Mission missionToBeDeleted = prankMission().build();
        Mission missionToBeUnchanged = noChinMission().build();

        manager.createMission(missionToBeDeleted);
        manager.createMission(missionToBeUnchanged);

        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(missionToBeDeleted, missionToBeUnchanged);
        assertThat(manager.findAllMissions().size()).isEqualTo(2);

        manager.deleteMission(missionToBeDeleted);

        assertThat(manager.findAllMissions())
                .usingFieldByFieldElementComparator()
                .containsOnly(missionToBeUnchanged);
        assertThat(manager.findAllMissions().size()).isEqualTo(1);
    }
}