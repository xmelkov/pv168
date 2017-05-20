package cz.muni.fi.Swing.AgentSwing;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.Swing.AgentTableModel;

import javax.swing.*;

/**
 * Created by Matúš on 20.5.2017.
 */
public class AgentUpdate extends SwingWorker<Void, Void>{
    private AgentTableModel table;
    private AgentManagerImpl agentManager;
    private Agent agent;

    public AgentUpdate(AgentTableModel table, AgentManagerImpl agentManager, Agent agent) {
        this.table = table;
        this.agentManager = agentManager;
        this.agent = agent;
    }

    @Override
    protected Void doInBackground() throws Exception {
        agentManager.updateAgent(agent);
        return null;
    }

    @Override
    protected void done() {
        table.agentUpdate(agent);
        super.done();
    }
}
