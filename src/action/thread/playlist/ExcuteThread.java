package action.thread.playlist;

import java.util.concurrent.CountDownLatch;

import action.SearchPlaylist;


public class ExcuteThread implements Runnable {
	
	private final CountDownLatch latch;
	
	private static int counter = 0;
	
	private final int page = counter++;
	
	private String url;
	
	public ExcuteThread(CountDownLatch latch,String url){
		this.latch = latch;
		this.url = url;
	}

	@Override
	public void run() {
		try {
			doWork();
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("“Ï≥£÷–∂œ£°latch: " + latch.getCount() + "size:" + page);
		}
	}
	
	public void doWork() throws InterruptedException {
		String item_url = url+(page*35);
		SearchPlaylist.getPlaylist(item_url);
		System.out.println(item_url +"==== finish");
	}
}
