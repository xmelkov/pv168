package cz.muni.fi.Swing;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Managers.AgentManager;
import cz.muni.fi.Managers.AssignmentManager;
import cz.muni.fi.Managers.MissionManager;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;
import cz.muni.fi.Swing.AgentSwing.AgentAdd;
import cz.muni.fi.Swing.AgentSwing.AgentDelete;
import cz.muni.fi.Swing.AgentSwing.AgentUpdate;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Matúš on 20.5.2017.
 */
public class AgentInteractiveDeploymentSwing extends JFrame{
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JPanel AgentPanel;
    private JTable table1;
    private JButton EDITButton;
    private JButton DELETEButton;
    private JButton DONEButton;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox comboBox1;
    private JButton CANCELButton;
    private JTextField textField4;
    private JPanel MissionPanel;
    private JCheckBox aliveCheckBox;

    private AgentManagerImpl agentManager;
    private MissionManagerImpl missionManager;
    private AssignmentManagerImpl assignmentManager;

    private DataSource db;

    private enum AgentOperation {
        ADD,
        UPDATE
    }

    private AgentOperation operation = AgentOperation.ADD;

    public AgentInteractiveDeploymentSwing() throws HeadlessException {
        table1.setModel(new AgentTableModel());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel1);
        setPreferredSize(new Dimension(800,600));

        db = SetupDB.createDB();

        agentManager = new AgentManagerImpl();
        agentManager.setDataSource(db);

        missionManager = new MissionManagerImpl();
        missionManager.setDataSource(db);

        assignmentManager = new AssignmentManagerImpl();
        assignmentManager.setDataSource(db);

        comboBox1.addItem("KO");
        comboBox1.addItem("KOT");

        DONEButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (operation) {
                    case ADD:
                        addAgent();
                        break;
                    case UPDATE:
                        updateAgent();
                        break;
                }
                operation = AgentOperation.ADD;
            }
        });

        EDITButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                operation = AgentOperation.ADD;
            }
        });
        CANCELButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                textField1.setText("");
                textField2.setText("");
                comboBox1.setSelectedIndex(0);
                textField4.setText("");
                aliveCheckBox.setSelected(false);
            }
        });
        DELETEButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                deleteAgent();
            }
        });
    }

    private void addAgent() {
        String agentName = textField1.getText();
        Short agentAge = Short.parseShort(textField2.getText());
        Short agentGender = (short) comboBox1.getSelectedIndex();
        String agentPhone = textField4.getText();
        Boolean agentAlive = aliveCheckBox.isSelected();

        Agent agent = new Agent();

        agent.setName(agentName);
        agent.setAge(agentAge);
        agent.setGender(agentGender);
        agent.setPhoneNumber(agentPhone);
        agent.setAlive(agentAlive);

        new AgentAdd((AgentTableModel)table1.getModel(), agentManager, agent).execute();
    }

    private void updateAgent() {
        if (table1.getSelectedRow() != - 1) {
            int agentIndex = table1.getSelectedRow();
            Agent agent = ((AgentTableModel) table1.getModel()).getAgentAt(agentIndex);

            String agentName = textField1.getText();
            Short agentAge = Short.parseShort(textField2.getText());
            Short agentGender = (short) comboBox1.getSelectedIndex();
            String agentPhone = textField4.getText();
            Boolean agentAlive = aliveCheckBox.isSelected();

            agent.setName(agentName);
            agent.setAge(agentAge);
            agent.setGender(agentGender);
            agent.setPhoneNumber(agentPhone);
            agent.setAlive(agentAlive);

            new AgentUpdate((AgentTableModel)table1.getModel(), agentManager, agent).execute();
        }
    }

    private void deleteAgent() {
        if (table1.getSelectedRow() != - 1) {
            int agentIndex = table1.getSelectedRow();
            Agent agent = ((AgentTableModel) table1.getModel()).getAgentAt(agentIndex);

            new AgentDelete((AgentTableModel)table1.getModel(), agentManager, agent).execute();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new AgentInteractiveDeploymentSwing();
            frame.setName("AIDS");
            frame.pack();
            frame.setVisible(true);
        });
    }
}
