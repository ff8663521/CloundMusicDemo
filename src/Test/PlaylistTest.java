package Test;

import java.util.ArrayList;
import java.util.List;

import bean.Playlist;
import dao.IPlaylistDao;
import dao.impl.PlaylistDao;

public class PlaylistTest {
	
	private static IPlaylistDao playlistDao;
	
	static {
		playlistDao = new PlaylistDao();;
	}
	
	public static void saveTest(){
		
		Playlist p = new Playlist();
		
		p.setId(1);
		p.setName("1");
		p.setLabel("1,2");
		p.setLink("1.2.com");
		p.setNum(20);
		
		playlistDao.save(p);
		
	}
	
	public static void saveBatchTest(){
		List<Playlist> playlist = new ArrayList<Playlist>();
		
		for (int i = 0; i < 12; i++) {
			Playlist p = new Playlist();
			p.setId(i);
			p.setName(""+i);
			p.setLabel("1,2---"+i);
			p.setLink("1.2.com---"+i);
			p.setNum(i*i);
			playlist.add(p);
		}
		
		playlistDao.batchSave(playlist);
		
		for (Playlist playlist2 : playlist) {
			System.out.println(playlist2);
		}
	}
	
	public static void countTest(){
		System.out.println(playlistDao.Count());
	}
	
	public static void updateTest(){
		Playlist p = new Playlist();
		
		p.setId(1);
		p.setName("1");
		p.setLabel("1,2");
		p.setLink("1.2.com");
		p.setNum(10);
		
		playlistDao.update(p);
	}
	
	public static void pageTest(){
		List<Playlist> list = null;
		
		for (int i = 1; i < 4; i++) {
			list = playlistDao.getAllPlaylistByPage(i, 10);
			
			for (Playlist playlist : list) {
				System.out.println(playlist);
			}
			
			System.out.println("========================");
		}
		
	}
	
	public static void main(String[] args) {
		//updateTest();
		//saveTest();
		//saveBatchTest();
		//countTest();
		//pageTest();
	}
}
