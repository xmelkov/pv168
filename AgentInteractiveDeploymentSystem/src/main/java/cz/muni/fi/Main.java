package cz.muni.fi;

import cz.muni.fi.Managers.AgentManager;
import cz.muni.fi.Managers.AssignmentManager;
import cz.muni.fi.Managers.MissionManager;
import cz.muni.fi.ManagersImpl.AgentManagerImpl;
import cz.muni.fi.ManagersImpl.AssignmentManagerImpl;
import cz.muni.fi.ManagersImpl.MissionManagerImpl;

import org.apache.derby.jdbc.EmbeddedDataSource;
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
        /*Properties myconf = new Properties();
        myconf.load(Main.class.getResourceAsStream("/myconf.properties"));

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(myconf.getProperty("jdbc.url"));
        ds.setUsername(myconf.getProperty("jdbc.user"));
        ds.setPassword(myconf.getProperty("jdbc.password"));

        AgentManager bookManager = new AgentManagerImpl();
        bookManager

        List<Agent> allBooks =
        allBooks.forEach(System.out::println);*/
    }

    @Configuration
    @PropertySource("classpath:conf.properties")
    public static class SpringConfig {

        @Autowired
        Environment env;

        @Bean
        public DataSource dataSource() {
            EmbeddedDataSource ds = new EmbeddedDataSource();

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
        public AgentManager agentManager() {
            AgentManagerImpl agentManager = new AgentManagerImpl();
            agentManager.setDataSource(dataSource());
            return agentManager;
        }

        @Bean
        public MissionManager missionManager() {
            MissionManagerImpl missionManager = new MissionManagerImpl();
            missionManager.setDataSource(dataSource());
            return missionManager;
        }

        @Bean
        public AssignmentManager assignmentManager() {
            AssignmentManagerImpl assignmentManager = new AssignmentManagerImpl();
            assignmentManager.setDataSource(dataSource());
            assignmentManager.setAgentManager(agentManager());
            assignmentManager.setMissionManager(missionManager());
            return assignmentManager;
        }
    }
}
