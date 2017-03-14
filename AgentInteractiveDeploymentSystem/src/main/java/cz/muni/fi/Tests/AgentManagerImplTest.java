package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Samuel on 14.03.2017.
 */
public class AgentManagerImplTest {

    private AgentManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        manager = new AgentManagerImpl();
    }

    @Test
    public void createAgent() throws Exception {
        assertTrue(manager.findAllAgents().isEmpty());
        Agent agent = newAgent(666,"Franku", (short) 6,(short) 22,"+421 988 666 690",true);
        manager.createAgent(agent);

        long agentId = agent.getId();
        assertFalse(agentId == 0);

        Agent foundAgent = manager.findAgentById(agentId);

        assertEquals(agent,foundAgent);
        assertNotSame(agent,foundAgent);
        assertDeepEquals(agent,foundAgent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAgentWithEmptyName() {
        Agent agent = newAgent(666,"", (short) 6,(short) 25,"+421 000 000 000",true);
        manager.createAgent(agent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAgentWithoutPhoneNumber() {
        Agent agent = newAgent(666,"Keemstar", (short) 6,(short) 36,"",true);
        manager.createAgent(agent);
    }


    @Test(expected = IllegalArgumentException.class)
    public void createAgentUnderAge() {
        Agent agent = newAgent(666,"Jacob Sartorius", (short) 6,(short) 10,
                "+421 123 456 789",true);
        manager.createAgent(agent);
    }


    @Test(expected = IllegalArgumentException.class)
    public void createNullAgent() {
        manager.createAgent(null);
    }


    @Test
    public void findAllAgents() throws Exception {
        assertTrue(manager.findAllAgents().isEmpty());
        Agent felix = newAgent(666, "Felix Kjellberg", (short) 3,(short) 25,
                "0123456789",true);
        manager.createAgent(felix);
        assertFalse(manager.findAllAgents().isEmpty());

        Agent shrek = newAgent(667, "Shrek", (short) 4,(short) 19, "0123456788",true);
        manager.createAgent(shrek);

        int expectedSize = 2;
        assertEquals(expectedSize,manager.findAllAgents().size());

        List<Agent> agentz = new ArrayList<>();
        agentz.add(felix);
        agentz.add(shrek);

        assertDeepEquals(agentz,manager.findAllAgents());
    }

    @Test
    public void updateAgent() throws Exception {
        Agent agent = newAgent(666,"Senpai", (short) 6,(short) 25,"+421 147 852 369",true);
        manager.createAgent(agent);

        Agent update1 = newAgent(agent);
        update1.setName("Yandere");
        manager.updateAgent(update1);
        assertDeepEquals(update1,manager.findAgentById(update1.getId()));

        Agent update2 = newAgent(update1);
        update2.setAge((short) 18);
        manager.updateAgent(update2);
        assertDeepEquals(update2,manager.findAgentById(update2.getId()));

        Agent update3 = newAgent(update2);
        update3.setGender((short) 1);
        manager.updateAgent(update3);
        assertDeepEquals(update3,manager.findAgentById(update3.getId()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullAgent() {
        manager.updateAgent(null);
    }

    @Test
    public void deleteAgent() throws Exception {
        assertTrue(manager.findAllAgents().isEmpty());
        Agent jackie = newAgent(1,"Jackie Chen",(short)1,(short) 120,"159 789 456",true);

        manager.createAgent(jackie);
        assertFalse(manager.findAllAgents().isEmpty());
        assertDeepEquals(jackie,manager.findAgentById(jackie.getId()));
        manager.deleteAgent(jackie);
        assertTrue(manager.findAllAgents().isEmpty());
    }

    private static Agent newAgent(long id, String name, short gender, short age, String phoneNumber, boolean alive) {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setName(name);
        agent.setGender(gender);
        agent.setAge(age);
        agent.setPhoneNumber(phoneNumber);
        agent.setAlive(alive);
        return agent;
    }

    private static Agent newAgent(Agent originalAgent) {
        return newAgent(originalAgent.getId(),originalAgent.getName(),originalAgent.getGender(),originalAgent.getAge(),
                originalAgent.getPhoneNumber(),originalAgent.isAlive());
    }

    private void assertDeepEquals(Agent originalAgent,Agent expectedAgent) {
        assertNotNull(expectedAgent);
        assertEquals(originalAgent.getId(),expectedAgent.getId());
        assertEquals(originalAgent.getAge(),expectedAgent.getAge());
        assertEquals(originalAgent.getGender(),expectedAgent.getGender());
        assertEquals(originalAgent.getPhoneNumber(),expectedAgent.getPhoneNumber());
        assertEquals(originalAgent.getName(),expectedAgent.getName());
        assertEquals(originalAgent.isAlive(),expectedAgent.isAlive());
    }
    private void assertDeepEquals(List<Agent> expected, List<Agent> actual) {
        assertEquals(expected.size(),actual.size());
        for (int i = 0 ; i < expected.size() ; ++i) {
            Agent expectedAgent = expected.get(i);
            Agent actualAgent = actual.get(i);
            assertDeepEquals(expectedAgent,actualAgent);
        }
    }

}