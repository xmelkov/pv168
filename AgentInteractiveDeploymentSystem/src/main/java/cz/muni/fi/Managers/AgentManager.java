package cz.muni.fi.Managers;

import cz.muni.fi.Base.Agent;
import java.util.List;

/**
 * Created by xmelkov on 8.3.17.
 */
public interface AgentManager {
    void createAgent(Agent agent);

    Agent findAgentById(long agentId);

    List<Agent> findAllAgents();

    void updateAgent(Agent agent);

    void deleteAgent(Agent agent);

}
