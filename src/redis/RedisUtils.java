package redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Playlist;
import bean.Song;
import redis.clients.jedis.Jedis;

public class RedisUtils {

	/**
	 * 将数据库中的歌曲存入redis id为key，name为value
	 * 
	 * @param list
	 */
	public static void copySong(List<Song> list) {
		Jedis jedis = RedisPoolUtil.getJedis();

		// -----添加数据----------
		Map<String, String> map = new HashMap<String, String>();

		for (Song song : list) {
			map.put(String.valueOf(song.getId()), song.getName());
		}

		jedis.hmset("song", map);

		RedisPoolUtil.returnResource(jedis);
	}

	/**
	 * 存储歌曲，去除已存在的song
	 */
	public static List<Song> dealHavenSong(List<Song> list) {

		Jedis jedis = RedisPoolUtil.getJedis();

		List<Song> cleanlist = new ArrayList<Song>();

		int count = 0;

		for (Song song : list) {
			boolean flag = jedis.hexists("song", String.valueOf(song.getId()));

			if (!flag) {
				count++;
				cleanlist.add(song);
			}
		}

		RedisPoolUtil.returnResource(jedis);

		System.out.println("共计：" + list.size() + "首，去重：" + (list.size() - count) + "首，剩余：" + count + "首！");

		return cleanlist;
	};

	/**
	 * 数据库存储完毕后，将新增歌曲添加至redis
	 * 
	 * @param list
	 */
	public static void saveSongs(List<Song> list) {
		Jedis jedis = RedisPoolUtil.getJedis();

		for (Song song : list) {
			jedis.hset("song", String.valueOf(song.getId()), song.getName());
		}

		RedisPoolUtil.returnResource(jedis);
	}

	/**
	 * 将数据库中的歌单存入redis id为key，name为value
	 * 
	 * @param list
	 */
	public static void copyPlaylist(List<Playlist> list) {
		Jedis jedis = RedisPoolUtil.getJedis();

		Map<String, String> map = new HashMap<String, String>();

		for (Playlist pl : list) {
			map.put(String.valueOf(pl.getId()), pl.getName());
		}

		jedis.hmset("playlist", map);

		RedisPoolUtil.returnResource(jedis);
	}

	/**
	 * 存储歌单，去除已存在的歌单
	 */
	public static List<Playlist> dealHavenPlayList(List<Playlist> list) {
		Jedis jedis = RedisPoolUtil.getJedis();
		List<Playlist> cleanlist = new ArrayList<Playlist>();

		int count = 0;

		for (Playlist pl : list) {
			boolean flag = jedis.hexists("playlist", String.valueOf(pl.getId()));

			if (!flag) {
				count++;
				cleanlist.add(pl);
			}
		}

		RedisPoolUtil.returnResource(jedis);
		System.out.println("共计：" + list.size() + "，去重：" + (list.size() - count) + "，剩余：" + count + "！");
		return cleanlist;
	}

	/**
	 * 数据库存储完毕后，将新增歌单添加至redis
	 * 
	 * @param list
	 */
	public static void savePlaylists(List<Playlist> list) {
		Jedis jedis = RedisPoolUtil.getJedis();
		for (Playlist playlist : list) {
			jedis.hset("playlist", String.valueOf(playlist.getId()), playlist.getName());
		}
		RedisPoolUtil.returnResource(jedis);
	}

}
