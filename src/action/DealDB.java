package action;

import java.util.List;

import bean.Playlist;
import bean.Song;
import dao.IAlbumDao;
import dao.IArtistDao;
import dao.ICommentDao;
import dao.IPlaylistDao;
import dao.ISongDao;
import dao.impl.AlbumDao;
import dao.impl.ArtistDao;
import dao.impl.CommentDao;
import dao.impl.PlaylistDao;
import dao.impl.SongDao;
import redis.RedisUtils;

/**
 * 处理redis 和 mysql 之间的数据操作类
 * 
 * @author wxf
 *
 */
public class DealDB {

	private static ISongDao songDao;
	private static IAlbumDao albumDao;
	private static IArtistDao artistDao;
	private static ICommentDao commentDao;
	private static IPlaylistDao playlistDao;

	static {
		songDao = new SongDao();
		albumDao = new AlbumDao();
		artistDao = new ArtistDao();
		commentDao = new CommentDao();
		playlistDao = new PlaylistDao();
	}
	
	/**
	 * 以歌曲的ID为key，name为Value，存入redis 
	 */
	public static void copyAllSongtoRedis() {

		int count = songDao.Count(0);

		int rows = 1000;

		int page = 0;
		if (count % rows == 0) {
			page = count / rows;
		} else {
			page = count / rows + 1;
		}

		for (int i = 1; i <= page; i++) {
			List<Song> list = songDao.getAll(i, rows);
			RedisUtils.copySong(list);
			System.out.println("第"+i+"页已完成");
		}
	}
	
	/**
	 * 以专辑的ID为key，name为Value，存入redis 
	 */
	public static void copyAllplaylisttoRedis(){
		
		int count = playlistDao.Count(0);

		int rows = 1000;

		int page = 0;
		if (count % rows == 0) {
			page = count / rows;
		} else {
			page = count / rows + 1;
		}

		for (int i = 1; i <= page; i++) {
			List<Playlist> list = playlistDao.getAll(i, rows);
			RedisUtils.copyPlaylist(list);
			System.out.println("第"+i+"页已完成");
		}
	}
	
	
	public static void main(String[] args) {
		//copyAllSongtoRedis();
		//copyAllplaylisttoRedis();
		
	}
	
	
}
