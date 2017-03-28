package cz.muni.fi.Managers;

import cz.muni.fi.Base.Mission;

import java.util.List;

/**
 * Created by xmelkov on 8.3.17.
 */
public interface MissionManager {
    void createMission(Mission mission);

    Mission findMissionById(Long missionId);

    List<Mission> findAllMissions();

    void updateMission(Mission mission);

    void deleteMission(Mission mission);

}
