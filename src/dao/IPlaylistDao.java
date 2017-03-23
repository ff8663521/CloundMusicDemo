package dao;

import java.util.List;

import bean.Playlist;

public interface IPlaylistDao {
	
	public void save(Playlist p);
	
	public void batchSave(List<Playlist> list);
	
	public void update(Playlist p);
	
	public int Count();
	
	public int Count(int index);
	
	public List<Playlist> getAllPlaylistByPage(Integer page,int rows) ;
	
	public List<Playlist> getPlaylistByIds(String ids);

	public List<Playlist> getAll(Integer page,int rows);
}
