package com.zskx.pemsystem.threadpool;

import java.util.Date;

public abstract class Task implements Runnable {
	/* 任务产生时间 */
	private Date generateTime;
	/* 任务提交到线程池时间 */
	private Date submitTime;
	/* 开始执行时间 */
	private Date beginExcTime;
	/* 执行完成时间 */
	private Date finishTime;
	
	private long taskId;

	private String info;
	
	
	
	public Task(String info) {
		this.info = info;
		generateTime = new Date();
	}
	public String info(){
		return this.info;
	}
	public Date getGenerateTime() {
		return generateTime;
	}


	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getBeginExcTime() {
		return beginExcTime;
	}

	public void setBeginExcTime(Date beginExcTime) {
		this.beginExcTime = beginExcTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	

}
