package cz.muni.fi.Swing;

import cz.muni.fi.Base.Agent;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matúš on 20.5.2017.
 */
public class AgentTableModel extends AbstractTableModel {
    private List<Agent> agents = new ArrayList<Agent>();

    @Override
    public int getRowCount() {
        return agents.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    public void agentAdd(Agent agent) {
        agents.add(agent);
        int lastRow = agents.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void agentDelete(Agent agent) {
        int index = agents.indexOf(agent);
        agents.remove(agent);
        fireTableRowsDeleted(index, index);
    }

    public void agentUpdate(Agent agent) {
        int index = agents.indexOf(agent);
        if (index != -1) {
            agents.remove(index);
            agents.add(index, agent);
            fireTableRowsInserted(index, index);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return agent.getName();
            case 1:
                return agent.getAge();
            case 2:
                return agent.getGender();
            case 3:
                return agent.getPhoneNumber();
            case 4:
                return agent.isAlive();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public Agent getAgentAt(int rowIndex) {
        return agents.get(rowIndex);
    }
}
