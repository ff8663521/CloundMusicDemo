package bean;
/**
 * ����ʵ��
 * @author wxf
 *
 */
public class Song {
	/**����id*/
	private Integer id ;
	
	/**��������*/
	private String name ;
	
	/**�������Ŵ���*/
	private int num ;
	
	/**�ݳ������ƣ�������Ϊ�˱�������ݳ���������©*/
	private String singerName;
	
	/**����*/
	private Artist artist ;
	
	/**����ר��*/
	private Album album;
	
	/**��������*/
	private String link;

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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
	public String toString() {
		return this.getName() + "  ���ӣ�"+this.getLink() + "    " +getId();
	}
	
	

}
