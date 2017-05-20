package cz.muni.fi.ManagersImpl;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;
import cz.muni.fi.Managers.AgentManager;
import cz.muni.fi.Managers.AssignmentManager;
import cz.muni.fi.Managers.MissionManager;
import cz.muni.fi.common.DBUtils;
import cz.muni.fi.common.IllegalEntityException;
import cz.muni.fi.common.ValidationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            assignment.setStart(LocalDateTime.parse(resultSet.getString("startDate")));
            assignment.setEnd(LocalDateTime.parse(resultSet.getString("endDate")));
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
        DBUtils.validate(assignment);
        if (agentManager.findAgentById(assignment.getAgent().getId()) == null) {
            throw new ValidationException("agent from assignment not in DB");
        }
        if (missionManager.findMissionById(assignment.getMission().getId()) == null) {
            throw new ValidationException("mission from assignment no in DB");
        }
        SimpleJdbcInsert insertAssignment = new SimpleJdbcInsert(jdbcTemplate).withTableName("assignments").
                usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("AgentId", assignment.getAgent().getId())
                .addValue("MissionId", assignment.getMission().getId())
                .addValue("startDate", assignment.getStart().toString())
                .addValue("endDate", assignment.getEnd().toString());
        Number id = insertAssignment.executeAndReturnKey(parameters);
        assignment.setId(id.longValue());

    }

    @Override
    public Assignment findAssignmentById(Long assignmentId) {
        if (assignmentId == null) { throw new IllegalArgumentException("assignment id is null"); }
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM assignments WHERE id=?", assignmentMapper, assignmentId);
        } catch (org.springframework.dao.DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Assignment> findAllAssignments() {
        return jdbcTemplate.query("SELECT * FROM assignments", assignmentMapper);
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        DBUtils.validate(assignment);
        jdbcTemplate.update("UPDATE assignments SET startDate=?, endDate=?, AgentId=?, MissionId=? WHERE id=?",
                assignment.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                assignment.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                assignment.getAgent().getId(),
                assignment.getMission().getId(),
                assignment.getId());
    }

    @Override
    public void deleteAssignment(Assignment assignment) {
        DBUtils.validate(assignment);
        jdbcTemplate.update("DELETE FROM assignments WHERE id=?", assignment.getId());
    }

    @Override
    public List<Assignment> findAllAssignmentsForMission(Mission mission) {
        DBUtils.validate(mission);
        try {
            return jdbcTemplate.query("SELECT * FROM assignments WHERE missionId=?", assignmentMapper, mission.getId());
        } catch (org.springframework.dao.DataAccessException ex) {
            throw new IllegalEntityException("Mission" + mission + "doesn't exist in database.");
        }
    }

    @Override
    public List<Assignment> findAllAssignmentsForAgent(Agent agent) {
        DBUtils.validate(agent);
        try {
            return jdbcTemplate.query("SELECT * FROM assignments WHERE agentId=?", assignmentMapper, agent.getId());
        } catch (org.springframework.dao.DataAccessException ex) {
            throw new IllegalEntityException("Agent" + agent + "doesn't exist in database.");
        }
    }

    public void setAgentManager(AgentManager agentManager) { this.agentManager = agentManager; }

    public void setMissionManager(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }
}