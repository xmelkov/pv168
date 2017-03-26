package cz.muni.fi.ManagersImpl;

import cz.muni.fi.Base.Mission;
import cz.muni.fi.Managers.MissionManager;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.List;

/**
 * Created by Samuel on 08.03.2017.
 */
public class MissionManagerImpl implements MissionManager {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createMission(Mission mission) {

    }

    @Override
    public Mission findMissionById(long missionId) {
        return null;
    }

    @Override
    public List<Mission> findAllMissions() {
        return null;
    }

    @Override
    public void updateMission(Mission mission) {

    }

    @Override
    public void deleteMission(Mission mission) {

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
