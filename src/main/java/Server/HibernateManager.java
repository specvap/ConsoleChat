package Server;

/**
 * Created by Windows on 26.02.2017.
 */
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateManager {
    private SessionFactory sessionFactory = buildSessionFactory();
    private static HibernateManager instance;

    private HibernateManager() {

    }

    public static HibernateManager getInstance() {
        if (instance == null) {
            instance = new HibernateManager();
        }
        return instance;
    }

    private SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();

        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).buildServiceRegistry();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        sessionFactory.close();
    }
}
