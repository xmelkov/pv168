package cz.muni.fi.ManagersImpl;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Managers.AgentManager;
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
public class AgentManagerImpl implements AgentManager{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Agent> agentMapper = new RowMapper<Agent>() {
        @Override
        public Agent mapRow(ResultSet resultSet, int i) throws SQLException {
            Agent agent = new Agent();
            agent.setId(resultSet.getLong("id"));
            agent.setName(resultSet.getString("name"));
            agent.setGender(resultSet.getShort("gender"));
            agent.setAge(resultSet.getShort("age"));
            agent.setPhoneNumber(resultSet.getString("phone"));
            agent.setAlive(resultSet.getBoolean("alive"));
            return agent;
        }
    };

    @Override
    public void createAgent(Agent agent) {
        SimpleJdbcInsert insertCustomer = new SimpleJdbcInsert(jdbcTemplate).withTableName("agents").
                usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", agent.getName())
                .addValue("gender", agent.getGender())
                .addValue("age", agent.getAge())
                .addValue("phone", agent.getPhoneNumber())
                .addValue("alive" , agent.isAlive());

        Number id = insertCustomer.executeAndReturnKey(parameters);
        agent.setId(id.longValue());
    }

    @Override
    public Agent findAgentById(long agentId) {
        return jdbcTemplate.queryForObject("SELECT * FROM agents WHERE id=?", agentMapper, agentId);
    }

    @Override
    public List<Agent> findAllAgents() {
        return jdbcTemplate.query("SELECT * FROM agents", agentMapper);
    }

    @Override
    public void updateAgent(Agent agent) {
        jdbcTemplate.update("UPDATE agents SET name=?,gender=?,age=?,phone=?,alive=? WHERE id=?",
                agent.getName(), agent.getGender(),agent.getAge(),agent.getPhoneNumber(),agent.isAlive());
    }

    @Override
    public void deleteAgent(Agent agent) {
        jdbcTemplate.update("DELETE FROM agents where id=?",agent.getId());
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
