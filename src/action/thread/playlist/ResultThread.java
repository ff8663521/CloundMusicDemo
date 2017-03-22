package action.thread.playlist;

import java.util.concurrent.CountDownLatch;

//结果通报线程
public class ResultThread implements Runnable {

	private final CountDownLatch latch;
	

	public ResultThread(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			// 阻塞当前线程，直至 latch 为 0
			latch.await();
			System.out.println("歌单抓取结束");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.out.println("异常中断！latch: " + latch.getCount());
		}
	}

}
