package cz.muni.fi.ManagersImpl;

import cz.muni.fi.Base.Mission;
import cz.muni.fi.Managers.MissionManager;
import cz.muni.fi.common.DBUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Samuel on 08.03.2017.
 */
public class MissionManagerImpl implements MissionManager {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Mission> missionMapper = new RowMapper<Mission>() {
        @Override
        public Mission mapRow(ResultSet resultSet, int i) throws SQLException {
            Mission mission = new Mission();
            mission.setId(resultSet.getLong("id"));
            mission.setDescription(resultSet.getString("description"));
            mission.setNumberOfRequiredAgents(resultSet.getShort("numberOfRequiredAgents"));
            mission.setDifficulty(resultSet.getInt("difficulty"));
            mission.setPlace(resultSet.getString("place"));
            mission.setSuccessful(resultSet.getBoolean("successful"));
            return mission;
        }
    };

    @Override
    public void createMission(Mission mission) {
        DBUtils.validate(mission);
        SimpleJdbcInsert insertMission = new SimpleJdbcInsert(jdbcTemplate).withTableName("missions").
                usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("description", mission.getDescription())
                .addValue("numberOfRequiredAgents", mission.getNumberOfRequiredAgents())
                .addValue("difficulty", mission.getDifficulty())
                .addValue("place" , mission.getPlace())
                .addValue("successful", mission.isSuccessful());

        Number id = insertMission.executeAndReturnKey(parameters);
        mission.setId(id.longValue());
    }

    @Override
    public Mission findMissionById(Long missionId) {

        if (missionId == null) { throw new IllegalArgumentException("missionId is null"); }
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM missions WHERE id=?", missionMapper, missionId);
        } catch (org.springframework.dao.DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Mission> findAllMissions() {
        return jdbcTemplate.query("SELECT * FROM missions", missionMapper);
    }

    @Override
    public void updateMission(Mission mission) {
        DBUtils.validate(mission);
        jdbcTemplate.update("UPDATE missions SET description=?,numberOfRequiredAgents=?,difficulty=?,place=?," +
                "successful=? WHERE id=?", mission.getDescription(),mission.getNumberOfRequiredAgents(),
                mission.getDifficulty(),mission.getPlace(), mission.isSuccessful(),mission.getId());
    }

    @Override
    public void deleteMission(Mission mission) {
        DBUtils.validate(mission);
        jdbcTemplate.update("DELETE FROM missions where id=?",mission.getId());
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
