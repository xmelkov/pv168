package cz.muni.fi;

import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;

/**
 * Created by xmelkov on 8.3.17.
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Moje první okno");
            frame.setVisible(true);
        });
    }

    public static DataSource createMemoryDatabase() {
        BasicDataSource bds = new BasicDataSource();
        //set JDBC driver and URL
        bds.setDriverClassName(EmbeddedDriver.class.getName());
        bds.setUrl("jdbc:derby:memory:agentsDB;create=true");
        //populate db with tables and data
        DatabasePopulatorUtils.execute(new ResourceDatabasePopulator(
                new ClassPathResource("my-schema.sql"),
                new ClassPathResource("test-data.sql")), bds);

        return bds;
    }

    @Configuration
    @PropertySource("classpath:conf.properties")
    public static class SpringConfig {

        @Autowired
        Environment env;

        @Bean
        public DataSource dataSource() {
            BasicDataSource ds = new BasicDataSource();

            ds.setDriverClassName(env.getProperty("jdbc.driver"));
            ds.setUrl(env.getProperty("jdbc.url"));
            ds.setUsername(env.getProperty("jdbc.user"));
            ds.setPassword(env.getProperty("jdbc.password"));
            return ds;
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        public AgentManagerImpl agentManager() {
            AgentManagerImpl agentManager = new AgentManagerImpl();
            agentManager.setDataSource(dataSource());
            return agentManager;
        }

        @Bean
        public MissionManagerImpl missionManager() {
            MissionManagerImpl missionManager = new MissionManagerImpl();
            missionManager.setDataSource(dataSource());
            return missionManager;
        }

        @Bean
        public AssignmentManagerImpl assignmentManager() {
            AssignmentManagerImpl assignmentManager = new AssignmentManagerImpl();
            assignmentManager.setDataSource(dataSource());
            assignmentManager.setAgentManager(agentManager());
            assignmentManager.setMissionManager(missionManager());
            return assignmentManager;
        }
    }
}
