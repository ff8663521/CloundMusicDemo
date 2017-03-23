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
	 * �����ݿ��еĸ�������redis idΪkey��nameΪvalue
	 * 
	 * @param list
	 */
	public static void copySong(List<Song> list) {
		Jedis jedis = RedisPoolUtil.getJedis();

		// -----�������----------
		Map<String, String> map = new HashMap<String, String>();

		for (Song song : list) {
			map.put(String.valueOf(song.getId()), song.getName());
		}

		jedis.hmset("song", map);

		RedisPoolUtil.returnResource(jedis);
	}

	/**
	 * �洢������ȥ���Ѵ��ڵ�song
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

		System.out.println("���ƣ�" + list.size() + "�ף�ȥ�أ�" + (list.size() - count) + "�ף�ʣ�ࣺ" + count + "�ף�");

		return cleanlist;
	};

	/**
	 * ���ݿ�洢��Ϻ󣬽��������������redis
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
	 * �����ݿ��еĸ赥����redis idΪkey��nameΪvalue
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
	 * �洢�赥��ȥ���Ѵ��ڵĸ赥
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
		System.out.println("���ƣ�" + list.size() + "��ȥ�أ�" + (list.size() - count) + "��ʣ�ࣺ" + count + "��");
		return cleanlist;
	}

	/**
	 * ���ݿ�洢��Ϻ󣬽������赥�����redis
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
