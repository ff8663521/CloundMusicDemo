package bean;

/**
 * �赥ʵ��
 * @author wxf
 *
 */
public class Playlist {
	/**�赥���*/
	private Integer id ;
	
	/**�赥����*/
	private String name ;
	
	/**�赥��ǩ*/
	private String label ;
	
	/**�赥����*/
	private String link ;
	
	/**�赥���Ŵ���*/
	private int num ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return this.getName() + "*����: " + this.getNum() + "�� ,*����:"+this.getLink();
	}
	
	
	
}
