package bean;
/**
 * 歌曲实体
 * @author wxf
 *
 */
public class Song {
	/**歌曲id*/
	private Integer id ;
	
	/**歌曲名称*/
	private String name ;
	
	/**歌曲播放次数*/
	private int num ;
	
	/**演唱者名称，该属性为了避免多人演唱，导致遗漏*/
	private String singerName;
	
	/**歌手*/
	private Artist artist ;
	
	/**所属专辑*/
	private Album album;
	
	/**歌曲链接*/
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
		return this.getName() + "  链接："+this.getLink() + "    " +getId();
	}
	
	

}
