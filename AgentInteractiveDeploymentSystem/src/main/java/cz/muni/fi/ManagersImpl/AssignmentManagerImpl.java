package cz.muni.fi.ManagersImpl;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.Managers.AgentManager;
import cz.muni.fi.Managers.AssignmentManager;
import cz.muni.fi.Managers.MissionManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by xmelkov on 08.03.2017.
 */
public class AssignmentManagerImpl implements AssignmentManager {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private AgentManager agentManager;
    private MissionManager missionManager;

    private RowMapper<Assignment> assignmentMapper = new RowMapper<Assignment>() {
        @Override
        public Assignment mapRow(ResultSet resultSet, int i) throws SQLException {
            Assignment assignment = new Assignment();
            assignment.setId(resultSet.getLong("id"));
            assignment.setStart(resultSet.getTimestamp("start").toLocalDateTime());
            assignment.setEnd(resultSet.getTimestamp("end").toLocalDateTime());
            try {
                assignment.setAgent(agentManager.findAgentById(resultSet.getLong("agentId")));
            } catch (EmptyResultDataAccessException e) {
                assignment.setAgent(null);
            }
            try {
                assignment.setMission(missionManager.findMissionById(resultSet.getLong("missionId")));
            } catch (EmptyResultDataAccessException e) {
                assignment.setMission(null);
            }

            return assignment;
        }
    };

    @Override
    public void createAssignment(Assignment assignment) {
        SimpleJdbcInsert insertAssignment = new SimpleJdbcInsert(jdbcTemplate).withTableName("assignments").
                usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("agentId", assignment.getAgent().getId())
                .addValue("missionId", assignment.getMission().getId())
                .addValue("start", toSQLTimestamp(assignment.getStart()))
                .addValue("end", toSQLTimestamp(assignment.getEnd()));
        Number id = insertAssignment.executeAndReturnKey(parameters);
        assignment.setId(id.longValue());

    }

    @Override
    public Assignment findAssignmentById(long assignmentId) {
        return jdbcTemplate.queryForObject("SELECT * FROM assignments WHERE id=?", assignmentMapper, assignmentId);
    }

    @Override
    public List<Assignment> findAllAssignments() {
        return jdbcTemplate.query("SELECT * FROM assignments", assignmentMapper);
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        jdbcTemplate.update("UPDATE assignments SET start=?, end=?, agentId=?, missionId=? WHERE id=?",
                assignmentMapper, toSQLTimestamp(assignment.getStart()), toSQLTimestamp(assignment.getEnd()),
                assignment.getAgent().getId(), assignment.getMission().getId(),assignment.getId());
    }

    @Override
    public void deleteAssignment(Assignment assignment) {
        jdbcTemplate.update("DELETE FROM assignments WHERE id=?", assignmentMapper, assignment.getId());
    }

    @Override
    public List<Assignment> findAllAssignmentsForMission(Mission mission) {
        return jdbcTemplate.query("SELECT * FROM assignments WHERE missionId=?", assignmentMapper, mission.getId());
    }

    @Override
    public List<Assignment> findAllAssignmentsForAgent(Agent agent) {
        return jdbcTemplate.query("SELECT * FROM assignments WHERE agentId=?", assignmentMapper, agent.getId());
    }

    public void setAgentManager(AgentManager agentManager) {
        this.agentManager = agentManager;
    }

    public void setMissionManager(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private Timestamp toSQLTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return new Timestamp(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli());

    }
}