package bean;
/**
 * ר��ʵ��
 * @author wxf
 *
 */
public class Album {
	/**ר�����*/
	private Integer id ;
	/**ר������*/
	private String name ;
	/**ר������*/
	private Artist artist;

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

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	
}
