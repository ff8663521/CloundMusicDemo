package fromatbean;

/**
 * ����������json���ֽ�
 * ����hotComments��Comments�ɽ��ж��ηֽ�
 * @author wxf
 *
 */
public class CommentsJson {
	
	private boolean isMusician ;
	//��ǰ��½�û�ID��-1 δ��¼
	private String userId;
	
	private String topComments;
	
	private boolean moreHot;
	//�������ۣ�������fromat
	private String hotComments;
	
	//״̬��
	private String code;
	
	//�������ۣ�������format
	private String comments;
	
	//������
	private int total;
	
	private boolean more;

	public boolean isMusician() {
		return isMusician;
	}

	public void setMusician(boolean isMusician) {
		this.isMusician = isMusician;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTopComments() {
		return topComments;
	}

	public void setTopComments(String topComments) {
		this.topComments = topComments;
	}

	public boolean isMoreHot() {
		return moreHot;
	}

	public void setMoreHot(boolean moreHot) {
		this.moreHot = moreHot;
	}

	public String getHotComments() {
		return hotComments;
	}

	public void setHotComments(String hotComments) {
		this.hotComments = hotComments;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isMore() {
		return more;
	}

	public void setMore(boolean more) {
		this.more = more;
	}

	
	
}
