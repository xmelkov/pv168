package cz.muni.fi;

import cz.muni.fi.Managers.AgentManager;
import cz.muni.fi.Managers.AssignmentManager;
import cz.muni.fi.Managers.MissionManager;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;

import cz.muni.fi.common.DBUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.assertj.core.util.Compatibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by xmelkov on 8.3.17.
 */
public class Main {
    public static void main(String[] args) {

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
