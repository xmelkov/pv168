package cz.muni.fi.Tests;

import com.sun.javaws.exceptions.InvalidArgumentException;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Samuel on 14.03.2017.
 */
public class MissionManagerImplTest {
    private MissionManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new MissionManagerImpl();
    }

    @Test
    public void createMission() throws Exception {
        assertTrue(manager.findAllMissions().isEmpty());
        Mission mission = newMission(25,"Eliminate mysterious inteloper with no chin",
                (short)1,100,"Seattle",true);
        manager.createMission(mission);
        assertFalse(manager.findAllMissions().isEmpty());

        long id = mission.getId();
        assertFalse(id == 0);

        Mission created = manager.findMissionById(id);
        assertNotNull(created);
        assertNotSame(mission,created);
        assertDeepEquals(mission,created);
    }

    @Test(expected = InvalidArgumentException.class)
    public void createMissionWithoutDescription() {
        Mission mission = newMission(69,"",
                (short)1,15,"Brno",true);
        manager.createMission(mission);
    }

    @Test
    public void updateMission() throws Exception {
        Mission originalMission = newMission(44,"Prank some1 really good", (short) 3, 44,
                "Auschwitz",true);
        manager.createMission(originalMission);
        assertFalse(manager.findAllMissions().isEmpty());
        assertDeepEquals(originalMission,manager.findMissionById(originalMission.getId()));

        Mission update1 = newMission(originalMission);
        update1.setDescription("Hey thats pretty good!");
        manager.updateMission(update1);
        assertDeepEquals(update1,manager.findMissionById(update1.getId()));

        Mission update2 = newMission(update1);
        update2.setDescription("Bake some tasty hair cake");
        update2.setPlace("Afghanistan");
        assertDeepEquals(update1,manager.findMissionById(update1.getId()));
        manager.updateMission(update2);
        assertDeepEquals(update2,manager.findMissionById(update2.getId()));

    }

    @Test
    public void deleteMission() throws Exception {
        assertTrue(manager.findAllMissions().isEmpty());
        Mission mission = newMission(22,"Assasinate killer Keemstar",
                (short)3,88,"Buffalo",true);
        manager.createMission(mission);
        assertFalse(manager.findAllMissions().isEmpty());
        assertNotNull(manager.findMissionById(mission.getId()));
        manager.deleteMission(mission);
        assertTrue(manager.findAllMissions().isEmpty());
    }

    private static Mission newMission(long id,String description, short requiredAgents,
                              int difficulty,String place,boolean successful) {
        Mission mission = new Mission();
        mission.setId(id);
        mission.setDescription(description);
        mission.setNumberOfRequiredAgents(requiredAgents);
        mission.setDifficulty(difficulty);
        mission.setPlace(place);
        mission.setSuccessful(successful);
        return mission;
    }

    private static Mission newMission(Mission originalMission) {
        return newMission(originalMission.getId(),originalMission.getDescription(),
                originalMission.getNumberOfRequiredAgents(),originalMission.getDifficulty(),originalMission.getPlace(),
                originalMission.isSuccessful());
    }

    private void assertDeepEquals(Mission originalMission, Mission expectedMission) {
        assertNotNull(expectedMission);
        assertEquals(originalMission.getId(),expectedMission.getId());
        assertEquals(originalMission.getDescription(),expectedMission.getDescription());
        assertEquals(originalMission.getNumberOfRequiredAgents(),expectedMission.getNumberOfRequiredAgents());
        assertEquals(originalMission.getDifficulty(),expectedMission.getDifficulty());
        assertEquals(originalMission.getPlace(),expectedMission.getPlace());
        assertEquals(originalMission.isSuccessful(),expectedMission.isSuccessful());
    }

    private void assertDeepEquals(List<Mission> expectedList, List<Mission> originalList) {
        assertEquals(expectedList.size(),originalList.size());
        for (int i = 0 ; i < originalList.size() ; ++i) {
            assertDeepEquals(expectedList.get(i),originalList.get(i));
        }
    }
}