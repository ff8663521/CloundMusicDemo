package dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import bean.Playlist;
import dao.IPlaylistDao;
import dao.utils.Hibernate4Util;

public class PlaylistDao implements IPlaylistDao {

	@Override
	public void save(Playlist p) {
		Session session = Hibernate4Util.getCurrentSession();
		Transaction ts = session.beginTransaction();
		try {
			session.saveOrUpdate(p);
			ts.commit();
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
		}finally{
			Hibernate4Util.closeSession();
		}

	}

	@Override
	public void batchSave(List<Playlist> list) {
		Session session = Hibernate4Util.getCurrentSession();
		Transaction ts = session.beginTransaction();

		try {
			Playlist p = null;
			for (int i = 0; i < list.size(); i++) {
				p = (Playlist) list.get(i); 
				session.saveOrUpdate(p); 
				// 批插入的对象立即写入数据库并释放内存
				if (i % 10 == 0) {
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit(); // 提交事物
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
		}finally{
			Hibernate4Util.closeSession();
		}
	}

	@Override
	public void update(Playlist p) {
		Session session = Hibernate4Util.getCurrentSession();
		Transaction ts = session.beginTransaction();
		try {
			session.update(p);
			ts.commit();
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
		}finally{
			Hibernate4Util.closeSession();
		}
		
	}

	@Override
	public int Count() {
		Session session = Hibernate4Util.getCurrentSession();
		
		int result = 0;
		StringBuilder strhql = new StringBuilder("select count(id)");
		strhql.append(" from Playlist t");
		strhql.append(" where 1=1 and label is null");
		
		try {
			Query query =session.createQuery(strhql.toString());
			List<Long> count = session.createQuery(strhql.toString()).list();
			if(count != null){
				result = count.get(0).intValue();
			}
		} catch (Exception e) {
		}finally{
			Hibernate4Util.closeSession();
		}
		
		return result;
		
		
	}

	@Override
	public List<Playlist> getAllPlaylistByPage(Integer page,int rows) {
		Session session = Hibernate4Util.getCurrentSession();
		
		List<Playlist> list = new ArrayList<Playlist>();
		
		StringBuilder strhql = new StringBuilder("from Playlist where 1=1 and label is null");
		
		try {
			Query query=session.createQuery(strhql.toString());
			query.setMaxResults(rows);
			query.setFirstResult((page-1)*rows);
			list = query.list();
		} catch (Exception e) {
		}finally{
			Hibernate4Util.closeSession();
		}
		return list;
	}

	@Override
	public List<Playlist> getPlaylistByIds(String ids) {
		Session session = Hibernate4Util.getCurrentSession();

		List<Playlist> list = new ArrayList<Playlist>();

		StringBuilder strhql = new StringBuilder("from Playlist where 1=1");
		strhql.append(" and id in (").append(ids).append(")");

		try {
			Query query = session.createQuery(strhql.toString());
			list = query.list();
		} catch (Exception e) {
		} finally {
			Hibernate4Util.closeSession();
		}
		return list;
	}

}
