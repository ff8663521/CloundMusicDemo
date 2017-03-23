package dao;

import java.util.List;

import bean.Song;

public interface ISongDao {
	
	public void save(Song song);
	
	public void batchSave(List<Song> list);
	
	public void update(Song song);
	
	public int Count();
	
	public int Count(int index);
	
	public List<Song> getAllSongByPage(Integer page,int rows) ;
	
	public List<Song> getPlaylistByIds(String ids);
	
	public void delete(Song song);
	
	public List<Song> getAll();
	
	public List<Song> getAll(Integer page,int rows);
}
