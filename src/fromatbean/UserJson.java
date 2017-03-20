package fromatbean;
/**
 * 网易云用户格式
 * 根据评论json 获取
 * @author wxf
 *
 */
public class UserJson {

	private String locationInfo;

	private String expertTags;
	
	// vip 等级
	private String vipType;
	
	// 昵称
	private String nickname;
	
	// 用户类型？
	private String userType;
	
	// 用户id
	private int userId;
	
	// 头像地址
	private String avatarUrl;
	
	// 状态？
	private String authStatus;
	
	// ？
	private String remarkName;

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public String getExpertTags() {
		return expertTags;
	}

	public void setExpertTags(String expertTags) {
		this.expertTags = expertTags;
	}

	public String getVipType() {
		return vipType;
	}

	public void setVipType(String vipType) {
		this.vipType = vipType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	@Override
	public String toString() {
		return "UserJson [vipType=" + vipType + ", nickname=" + nickname + ", userType=" + userType + ", userId="
				+ userId + ", avatarUrl=" + avatarUrl + "]";
	}
	
	

}
