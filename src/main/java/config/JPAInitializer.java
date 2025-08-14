package config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JPAInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JPAConfig.getEntityManager().close();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAConfig.close();
    }
}
