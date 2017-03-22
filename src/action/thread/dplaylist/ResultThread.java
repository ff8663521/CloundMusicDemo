package action.thread.dplaylist;

import java.util.concurrent.CountDownLatch;

//结果通报线程
public class ResultThread implements Runnable {

	private final CountDownLatch latch;

	private final int page;
	
	public ResultThread(CountDownLatch latch ,int page) {
		this.latch = latch;
		this.page = page;
	}

	@Override
	public void run() {
		try {
			// 阻塞当前线程，直至 latch 为 0
			latch.await();
			System.out.println("第"+page+"页，歌曲抓取结束");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.out.println("异常中断！latch: " + latch.getCount());
		}
	}

}
