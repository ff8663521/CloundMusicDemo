package dao.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class Hibernate4Util {

	private static SessionFactory sessionFactory;
	
//	private static Session session;
	
	public static final ThreadLocal<Session> session =  new ThreadLocal<Session>();  
	
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
		Session s = session.get();
		if (s == null) {
			s = sessionFactory.openSession();
			session.set(s);
		}
		return s; 
	}

	public static void closeSession() {
		// ��ȡ��ǰ�߳��µ�SESSION
		Session s = session.get();
        if (s != null) {
            // s.close();
        	//�������轫Session�رգ���Ϊ��Session�Ǳ����ڵ�ǰ�߳�//�еģ��߳�ִ�����Session��Ȼ������
            session.set(null);// ����ǰ�߳��еĻỰ���
        }
	}

}
