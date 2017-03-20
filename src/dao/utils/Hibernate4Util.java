package dao.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class Hibernate4Util {

	private static SessionFactory sessionFactory;
	private static Session session;

	static {
		// ����Configuration,�ö������ڶ�ȡhibernate.cfg.xml������ɳ�ʼ��
		 Configuration cfg = new Configuration().configure("/config/hibernate.cfg.xml"); 
		 //hibernate4ʹ��
         ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();  
         sessionFactory = cfg.buildSessionFactory(serviceRegistry);  
	}

	/**
	 * ��ȡSessionFactory
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getCurrentSession() {
		session = sessionFactory.openSession();
		return session;
	}

	public static void closeSession(Session session) {

		if (null != session) {
			session.close();
		}
	}

}
