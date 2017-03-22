package action.thread.dplaylist;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import action.DetailsPlaylist;
import action.SearchPlaylist;
import bean.Playlist;


public class ExcuteThread implements Runnable {
	
	private final CountDownLatch latch;
	
	private static int counter = 0;
	
	private final int index = counter++;

	private List<Playlist> list ;
	
	public ExcuteThread(CountDownLatch latch,List<Playlist> list){
		this.latch = latch;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			doWork();
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("“Ï≥£÷–∂œ£°latch: " + latch.getCount() + "size:" + index);
		}
	}
	
	public void doWork() throws InterruptedException {
		Playlist pl = list.get(index);
		DetailsPlaylist.getSongsByPlaylist(pl);
		System.out.println(pl.getName() + "=====finish");
	}
	
	public static void resetCount(){
		counter = 0;
	}
}
