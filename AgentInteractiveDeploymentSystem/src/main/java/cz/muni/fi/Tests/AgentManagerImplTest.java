package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.common.ValidationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;


/**
 * Created by Samuel on 14.03.2017.
 */
public class AgentManagerImplTest {

    private EmbeddedDatabase db;
    private AgentManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder().setType(DERBY).addScript("my-schema.sql").build();
        manager = new AgentManagerImpl();
        manager.setDataSource(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private AgentBuilder agentKeemstar() {
        return new AgentBuilder()
                .id(null)
                .name("Keemstar")
                .gender((short) 6)
                .age((short) 36)
                .phoneNumber("+421 999 888 777")
                .alive(true);
    }

    private AgentBuilder agentShrek() {
        return new AgentBuilder()
                .id(null)
                .name("Shrek")
                .gender((short) 4)
                .age((short) 19)
                .phoneNumber("0123456788")
                .alive(true);
    }

    @Test
    public void createAgent() throws Exception {
        assertThat(manager.findAllAgents()).isEmpty();
        Agent agent = agentKeemstar().build();
        manager.createAgent(agent);


        assertThat(agent.getId()).isEqualTo(1);

        Agent foundAgent = manager.findAgentById(1L);

        assertThat(agent).isEqualTo(foundAgent)
                .isNotSameAs(foundAgent)
                .isEqualToComparingFieldByField(foundAgent);
    }

    @Test
    public void createAgentWithEmptyName() {
        Agent agent = agentKeemstar().name("").build();
        expectedException.expect(ValidationException.class);
        manager.createAgent(agent);
    }

    @Test
    public void createAgentWithoutPhoneNumber() {
        Agent agent = agentShrek().phoneNumber("").build();
        expectedException.expect(ValidationException.class);
        manager.createAgent(agent);
    }


    @Test
    public void createAgentUnderAge() {
        Agent agent = agentShrek().age((short) 3).build();
        expectedException.expect(ValidationException.class);
        manager.createAgent(agent);
    }


    @Test
    public void createNullAgent() {
        expectedException.expect(IllegalArgumentException.class);
        manager.createAgent(null);
    }


    @Test
    public void findAllAgents() throws Exception {
        assertThat(manager.findAllAgents()).isEmpty();

        Agent agentA = agentKeemstar().build();
        manager.createAgent(agentA);

        assertThat(manager.findAllAgents()).isNotEmpty();

        Agent agentB = agentShrek().build();
        manager.createAgent(agentB);

        assertThat(manager.findAllAgents().size()).isEqualTo(2);

        assertThat(manager.findAllAgents()).usingFieldByFieldElementComparator().containsOnly(agentA, agentB);
    }

    @Test
    public void updateAgent() throws Exception {
        Agent agent = agentShrek().build();
        Agent unchangedAgent = agentKeemstar().build();
        manager.createAgent(agent);
        manager.createAgent(unchangedAgent);

        agent.setName("nameUpdated");
        manager.updateAgent(agent);
        assertThat(agent).isEqualToComparingFieldByField(manager.findAgentById(agent.getId()));

        agent.setAlive(!agent.isAlive());
        manager.updateAgent(agent);
        assertThat(agent).isEqualToComparingFieldByField(manager.findAgentById(agent.getId()));

        agent.setAge((short) 20);
        manager.updateAgent(agent);
        assertThat(agent).isEqualToComparingFieldByField(manager.findAgentById(agent.getId()));

        assertThat(unchangedAgent).isEqualToComparingFieldByField(manager.findAgentById(unchangedAgent.getId()));
    }

    @Test
    public void updateNullAgent() {
        expectedException.expect(IllegalArgumentException.class);
        manager.updateAgent(null);
    }

    @Test
    public void deleteAgent() throws Exception {
        Agent agentToBeDeleted = agentKeemstar().build();
        Agent agentUnchanged = agentShrek().build();

        manager.createAgent(agentToBeDeleted);
        manager.createAgent(agentUnchanged);

        assertThat(manager.findAllAgents().size()).isEqualTo(2);
        assertThat(manager.findAllAgents())
                .usingFieldByFieldElementComparator()
                .containsOnly(agentToBeDeleted,agentUnchanged);

        manager.deleteAgent(agentToBeDeleted);

        assertThat(manager.findAllAgents().size()).isEqualTo(1);
        assertThat(manager.findAllAgents())
                .usingFieldByFieldElementComparator()
                .containsOnly(agentUnchanged);
    }
}