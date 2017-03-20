package fromatbean;

import java.util.Date;

import bean.Song;

/**
 * �����Ƶ������۸�ʽ
 * ��������json ��ȡ
 * @author wxf
 *
 */
public class CommentJson {
	//�û��˺���Ϣ�������� format
	private String user;
	
	private String beReplied;
	
	//������Ŀ
	private int likedCount;
	
	//����id
	private String commentId;
	
	//�Ƿ�ϲ�������ܺ��Ƿ��½���Ƿ��޹��йأ����û���Ϊ�йأ�
	private boolean liked;
	
	//����ʱ��
	private Date time;
	
	//��������
	private String content;
	
	//��������
	private Song song;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBeReplied() {
		return beReplied;
	}

	public void setBeReplied(String beReplied) {
		this.beReplied = beReplied;
	}

	public int getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(int likedCount) {
		this.likedCount = likedCount;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentJson [likedCount=" + likedCount + ", commentId=" + commentId + ", time=" + time + ", content="
				+ content + "]";
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
	
	
	
}
