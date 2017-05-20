package cz.muni.fi.Swing.AgentSwing;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.Swing.AgentTableModel;

import javax.swing.*;

/**
 * Created by Matúš on 20.5.2017.
 */
public class AgentDelete extends SwingWorker<Void, Void> {
    private AgentTableModel table;
    private AgentManagerImpl agentManager;
    private Agent agent;

    public AgentDelete(AgentTableModel table, AgentManagerImpl agentManager, Agent agent) {
        this.table = table;
        this.agentManager = agentManager;
        this.agent = agent;
    }

    @Override
    protected Void doInBackground() throws Exception {
        agentManager.deleteAgent(agent);
        return null;
    }

    @Override
    protected void done() {
        table.agentDelete(agent);
        super.done();
    }
}
