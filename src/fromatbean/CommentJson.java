package fromatbean;

import java.util.Date;

import bean.Song;

/**
 * 网易云单条评论格式
 * 根据评论json 获取
 * @author wxf
 *
 */
public class CommentJson {
	//用户账号信息，待二次 format
	private String user;
	
	private String beReplied;
	
	//获赞数目
	private int likedCount;
	
	//评论id
	private String commentId;
	
	//是否喜欢？可能和是否登陆，是否赞过有关？和用户行为有关？
	private boolean liked;
	
	//发布时间
	private Date time;
	
	//评论内容
	private String content;
	
	//所属歌曲
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
