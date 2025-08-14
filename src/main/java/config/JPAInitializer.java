package config;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JPAInitializer implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(JPAInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            EntityManager em = JPAConfig.getEntityManager();
            em.close();
            logger.info("JPA initialized");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            JPAConfig.close();
            logger.info("JPA finalized");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        cleanUpThreads();

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.info("Driver JDBC desregistrado: " + driver);
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erro ao desregistrar o driver JDBC: " + driver, e);
            }
        }
    }

    private void cleanUpThreads() {
        try {
            Class<?> threadPoolClass = Class.forName("org.hibernate.internal.util.ThreadPoolManager");
            Object instance = threadPoolClass.getMethod("getInstance").invoke(null);
            threadPoolClass.getMethod("destroy").invoke(instance);
        } catch (Exception e) {
            logger.log(Level.FINE, "ThreadPoolManager não encontrado", e);
        }
    }
}
