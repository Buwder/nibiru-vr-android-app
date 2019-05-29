package com.zskx.pemsystem.threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class ThreadPoolManager {

	private static final String TAG = "ThreadToolManager";

	public static boolean isDebug = true;
	/** 任务队列 **/
	private List<Task> taskQueue = Collections
			.synchronizedList(new LinkedList<Task>());

	private static long taskCount = 0;

	private static ThreadPoolManager instance = new ThreadPoolManager();

	private final static int DEFAULT_WORKERS = 5;

	private static ArrayList<PoolWorker> workers;

	private static int workNum = 0;

	private ThreadPoolManager() {
		workers = new ArrayList<ThreadPoolManager.PoolWorker>();
		for (int i = 0; i < DEFAULT_WORKERS; i++) {
			PoolWorker pw = new PoolWorker(++workNum);
			workers.add(pw);

		}
	}

	/**
	 * 得到线程池工作者个数
	 * 
	 * @return
	 */
	public int getWorkNum() {
		return workNum;
	}

	/**
	 * 添加工作者
	 * 
	 * @param num
	 */
	public void addWorkerNum(int num) {
		for (int i = 0; i < num; i++) {
			PoolWorker pw = new PoolWorker(++workNum);
			workers.add(pw);
		}
	}

	/**
	 * 添加新任务
	 * 
	 * @param newTask
	 */
	public synchronized void addTask(Task newTask) {
		if (newTask != null) {
			synchronized (taskQueue) {
				newTask.setTaskId(++taskCount);
				newTask.setSubmitTime(new Date());

				taskQueue.add(newTask);

				taskQueue.notifyAll();
			}
			Log.i(TAG,
					"Submit Task<" + newTask.getTaskId() + ">: "
							+ newTask.info());
		} else {
			Log.w(TAG, "the newTash is a null object! nothing happen!");
		}
	}

	public synchronized void destroyThreadPool(){
		for (PoolWorker worker : workers) {
			worker.stopRunning();
			worker = null;
		}
		taskQueue.clear();
		instance = null;
	}
	
	/**
	 * 线程池信息
	 * 
	 * @return
	 */
	public String getInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nTask Queue Size:" + taskQueue.size());
		for (int i = 0; i < workers.size(); i++) {
			sb.append("\nWorker " + i + " is "
					+ ((workers.get(i).isWaiting()) ? "Waiting." : "Running."));
		}
		return sb.toString();
	}

	/**
	 * 得到线程池
	 * 
	 * @return
	 */
	public static synchronized ThreadPoolManager getInstance() {
		if (instance == null) {
			instance = new ThreadPoolManager();
		}
		return instance;
	}

	/**
	 * 工作者
	 * 
	 * @author demo
	 * 
	 */
	public class PoolWorker extends Thread {
		private int workerId;
		private boolean isRunning = true;
		private boolean isWaiting = true;

		public PoolWorker(int workId) {
			this.workerId = workId;
			start();
		}

		public int getWorkerId() {
			return workerId;
		}

		public boolean isRunning() {
			return isRunning;
		}

		public void stopRunning() {
			this.isRunning = false;
		}

		public boolean isWaiting() {
			return isWaiting;
		}

		@Override
		public void run() {
			while (isRunning) {
				Log.d(TAG, "worker<" + workerId + "> is running");
				Task r = null;

				synchronized (taskQueue) {
					while (taskQueue.isEmpty()) {
						try {
//							Log.d(TAG, "worker<" + workerId + "> is waiting!");
							taskQueue.wait();
//							Log.d(TAG, "worker<" + workerId + "> stop waiting!");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Log.d(TAG, "worker<" + workerId + "> get task!");
					r = taskQueue.remove(0);

				}
				isWaiting = false;
				if (isDebug) {
					r.setBeginExcTime(new Date());
					Log.d(TAG, "Worker<" + workerId + "> start execute Task<"
							+ r.getTaskId() + ",info :" + r.info() + ">");
					Log.d(TAG, "from generate task to run task that take time:"
							+ (r.getBeginExcTime().getTime() - r
									.getSubmitTime().getTime()));
				}
				r.run();
				if (isDebug) {
					r.setFinishTime(new Date());
					Log.d(TAG, "Worker<" + workerId + "> finish execute Task<"
							+ r.getTaskId() + ",info :" + r.info() + ">");
					Log.d(TAG, "from run task to finish task that take time:"
							+ (r.getFinishTime().getTime() - r
									.getBeginExcTime().getTime()));
				}
				isWaiting = true;
				r = null;
			}
		}
	}
}
