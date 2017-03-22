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
		// 创建Configuration,该对象用于读取hibernate.cfg.xml，并完成初始化
		 Configuration cfg = new Configuration().configure("/config/hibernate.cfg.xml"); 
		 //hibernate4使用
         ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();  
         sessionFactory = cfg.buildSessionFactory(serviceRegistry);  
	}

	/**
	 * 获取SessionFactory
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
		// 获取当前线程下的SESSION
		Session s = session.get();
        if (s != null) {
            // s.close();
        	//这里无需将Session关闭，因为该Session是保存在当前线程//中的，线程执行完毕Session自然会销毁
            session.set(null);// 将当前线程中的会话清除
        }
	}

}
