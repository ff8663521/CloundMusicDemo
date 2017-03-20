package dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import bean.Comment;
import dao.ICommentDao;
import dao.utils.Hibernate4Util;

public class CommentDao implements ICommentDao {

	@Override
	public void batchSave(List<Comment> list) {
		Session session = Hibernate4Util.getCurrentSession();
		Transaction ts = session.beginTransaction();

		try {
			Comment comment = null;
			for (int i = 0; i < list.size(); i++) {
				comment = (Comment) list.get(i); 
				session.saveOrUpdate(comment); 
			}
			session.getTransaction().commit(); // 提交事物
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
		}finally{
			Hibernate4Util.closeSession(session);
		}

	}

}
