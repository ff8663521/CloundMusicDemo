package dao;

import java.util.List;

import bean.Song;

public interface ISongDao {
	
	public void save(Song song);
	
	public void batchSave(List<Song> list);
	
	public void update(Song song);
	
	public int Count();
	
	public List<Song> getAllSongByPage(Integer page,int rows) ;
}
