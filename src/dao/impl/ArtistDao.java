package dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import bean.Artist;
import dao.IArtistDao;
import dao.utils.Hibernate4Util;

public class ArtistDao implements IArtistDao {

	@Override
	public void save(Artist artist) {
		Session session = Hibernate4Util.getCurrentSession();
		Transaction ts = session.beginTransaction();
		try {
			session.saveOrUpdate(artist);
			ts.commit();
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
		}finally{
			Hibernate4Util.closeSession();
		}


	}

}
