package dao;

import java.util.List;

import bean.Comment;

public interface ICommentDao {
	public void batchSave(List<Comment> list);
}
