package cz.muni.fi.Tests;

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


    }

    @Test
    public void findMissionById() throws Exception {

    }

    @Test
    public void updateMission() throws Exception {

    }

    @Test
    public void deleteMission() throws Exception {

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