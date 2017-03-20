package dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import bean.Album;
import dao.IAlbumDao;
import dao.utils.Hibernate4Util;

public class AlbumDao implements IAlbumDao {

	@Override
	public void save(Album album) {
		Session session = Hibernate4Util.getCurrentSession();
		Transaction ts = session.beginTransaction();
		try {
			session.saveOrUpdate(album);
			ts.commit();
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
		}finally{
			Hibernate4Util.closeSession(session);
		}

	}

}
