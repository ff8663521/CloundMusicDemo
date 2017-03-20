package bean;
/**
 * 专辑实体
 * @author wxf
 *
 */
public class Album {
	/**专辑编号*/
	private Integer id ;
	/**专辑名称*/
	private String name ;
	/**专辑歌手*/
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
