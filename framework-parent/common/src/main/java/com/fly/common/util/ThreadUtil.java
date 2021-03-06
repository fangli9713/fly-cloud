package com.fly.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


/**
 * 
 * 线程工具
 */
public final class ThreadUtil extends ThreadPoolExecutor{
	private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);
	private static ThreadUtil executor = null;
	

	static{
		initExecutor();
	}

	/**
	 * Creates a new {@code ThreadPoolExecutor} with the given initial
	 * parameters and default thread factory and rejected execution handler.
	 * It may be more convenient to use one of the {@link Executors} factory
	 * methods instead of this general purpose constructor.
	 *
	 * @param corePoolSize    the number of threads to keep in the pool, even
	 *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
	 * @param maximumPoolSize the maximum number of threads to allow in the
	 *                        pool
	 * @param keepAliveTime   when the number of threads is greater than
	 *                        the core, this is the maximum time that excess idle threads
	 *                        will wait for new tasks before terminating.
	 * @param unit            the time unit for the {@code keepAliveTime} argument
	 * @param workQueue       the queue to use for holding tasks before they are
	 *                        executed.  This queue will hold only the {@code Runnable}
	 *                        tasks submitted by the {@code execute} method.
	 * @throws IllegalArgumentException if one of the following holds:<br>
	 *                                  {@code corePoolSize < 0}<br>
	 *                                  {@code keepAliveTime < 0}<br>
	 *                                  {@code maximumPoolSize <= 0}<br>
	 *                                  {@code maximumPoolSize < corePoolSize}
	 * @throws NullPointerException     if {@code workQueue} is null
	 */
	public ThreadUtil(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	/**
	 *	获取实例
	 */
	public static ThreadPoolExecutor getExecutorInstance(){
		return executor;
	}
	
	/**
	 * 初始化
	 */
	private static void initExecutor(){
		int  cpuCores = getCores();
		if(cpuCores  < 4){
			logger.warn("初始化线程数小于4，重置为4");
			cpuCores = 4;
		}
		
		int maxPool = ioIntesivePoolSize();
		
		if(maxPool < 10){
			logger.warn("最大线程数小于10,重置为10");
			maxPool = 10;
		}
		if (maxPool > 50){
			maxPool = 50;
		}
		
		logger.info("初始化线程池 cpu core:{} , maxPool:{}" , cpuCores , maxPool);
		executor = new ThreadUtil(cpuCores , maxPool , 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		//设置允许回收线程
		executor.allowCoreThreadTimeOut(true);
	}

	/**
	 * 重启公共线程池
	 */
	public  void restart() {
		executor.shutdownNow();
		initExecutor();
	}

	/**
	 * 新建一个固定大小的线程池
	 * @param threadSize 同时执行的线程数大小
	 * @return ExecutorService
	 */
	public  ExecutorService newExecutor(int threadSize) {
		return Executors.newFixedThreadPool(threadSize);
	}

	/**
	 * 获得一个新的Cached线程池
	 * @return ExecutorService
	 */
	public  ExecutorService newExecutor() {
		return Executors.newCachedThreadPool();
	}

	/**
	 * 获得一个新的线程池，只有单个线程
	 * 
	 * @return ExecutorService
	 */
	public  ExecutorService newSingleExecutor() {
		return Executors.newSingleThreadExecutor();
	}

	/**
	 * 执行异步方法
	 * 
	 * @param runnable 需要执行的方法体
	 * @return 执行的方法体
	 */
	public  Runnable excAsync(final Runnable runnable, boolean isDeamon) {
		Thread thread = new Thread(){
			@Override
			public void run() {
				runnable.run();
			}
		};
		thread.setDaemon(isDeamon);
		thread.start();

		return runnable;
	}

	/**
	 * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。，若未完成，则会阻塞
	 * 
	 * @return CompletionService
	 */
	public  <T> CompletionService<T> newCompletionService() {
		return new ExecutorCompletionService<T>(executor);
	}

	/**
	 * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。若未完成，则会阻塞
	 * 
	 * @return CompletionService
	 */
	public  <T> CompletionService<T> newCompletionService(ExecutorService executor) {
		return new ExecutorCompletionService<T>(executor);
	}

	/**
	 * 新建一个CountDownLatch
	 * 
	 * @param threadCount 线程数量
	 * @return CountDownLatch
	 */
	public  CountDownLatch newCountDownLatch(int threadCount) {
		return new CountDownLatch(threadCount);
	}
	
	/**
	 * 挂起当前线程
	 * 
	 * @param timeout 挂起的时长
	 * @param timeUnit 时长单位
	 * @return 被中断返回false，否则true
	 */
	public  boolean sleep(Number timeout, TimeUnit timeUnit) {
		try {
			timeUnit.sleep(timeout.longValue());
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/**
	 * 挂起当前线程
	 * 
	 * @param millis 挂起的毫秒数
	 * @return 被中断返回false，否则true
	 */
	public  boolean sleep(Number millis) {
		if (millis == null) {
			return true;
		}

		try {
			Thread.sleep(millis.longValue());
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 考虑{@link Thread#sleep(long)}方法有可能时间不足给定毫秒数，此方法保证sleep时间不小于给定的毫秒数
	 * @see ThreadUtil#sleep(Number)
	 * @param millis 给定的sleep时间
	 * @return 被中断返回false，否则true
	 */
	public  boolean safeSleep(Number millis){
		long millisLong = millis.longValue();
		long done = 0;
		while(done < millisLong){
			long before = System.currentTimeMillis();
			if(false == sleep(millisLong - done)){
				return false;
			}
			long after = System.currentTimeMillis();
			done += (after - before);
		}
		return true;
	}
	
	/**
	 * @return 获得堆栈列表
	 */
	public  StackTraceElement[] getStackTrace() {
		return Thread.currentThread().getStackTrace();
	}

	/**
	 * 获得堆栈项
	 * 
	 * @param i 第几个堆栈项
	 * @return 堆栈项
	 */
	public  StackTraceElement getStackTraceElement(int i) {
		StackTraceElement[] stackTrace = getStackTrace();
		if (i < 0) {
			i += stackTrace.length;
		}
		return stackTrace[i];
	}
	
	/**
	 * 创建本地线程对象
	 * @return 本地线程
	 */
	public  <T> ThreadLocal<T> createThreadLocal(boolean isInheritable){
		if(isInheritable){
			return new InheritableThreadLocal<>();
		}else{
			return new ThreadLocal<>();
		}
	}
	
	/**
	 * 结束线程，调用此方法后，线程将抛出 {@link InterruptedException}异常
	 * @param thread 线程
	 * @param isJoin 是否等待结束
	 */
	public  void interupt(Thread thread, boolean isJoin){
		if(null != thread && false == thread.isInterrupted()){
			thread.interrupt();
			if(isJoin){
				waitForDie(thread);
			}
		}
	}
	
	/**
	 * 等待线程结束. 调用 {@link Thread#join()} 并忽略 {@link InterruptedException}
	 * 
	 * @param thread 线程
	 */
	public  void waitForDie(Thread thread) {
		boolean dead = false;
		do {
			try {
				thread.join();
				dead = true;
			} catch (InterruptedException e) {
				//ignore
			}
		} while (!dead);
	}
	
	/**
	 * 获取JVM中与当前线程同组的所有线程<br>
	 * @return 线程对象数组
	 */
	public  Thread[] getThreads(){
		return getThreads(Thread.currentThread().getThreadGroup().getParent());
	}
	
	/**
	 * 获取JVM中与当前线程同组的所有线程<br>
	 * 使用数组二次拷贝方式，防止在线程列表获取过程中线程终止<br>
	 *
	 * @param group 线程组
	 * @return 线程对象数组
	 */
	public  Thread[] getThreads(ThreadGroup group){
		final Thread[] slackList = new Thread[group.activeCount() * 2];
		final int actualSize = group.enumerate(slackList);
		final Thread[] result = new Thread[actualSize];
		System.arraycopy(slackList, 0, result, 0, actualSize);
		return result;
	}
	
	/**
	 * 获取进程的主线程<br>
	 * from Voovan
	 * @return 进程的主线程
	 */
	public  Thread getMainThread(){
		for(Thread thread: getThreads()){
			if(thread.getId()==1){
				return thread;
			}
		}
		return null;
	}

	/**
	 * Each tasks blocks 90% of the time, and works only 10% of its lifetime.
	 * That is, I/O intensive pool
	 * @return io intesive Thread pool size
	 */
	private static int ioIntesivePoolSize() {
		double blockingCoefficient = 0.9;
		return poolSize(blockingCoefficient);
	}

	/**
	 * Number of threads = Number of Available Cores / (1 - Blocking
	 * Coefficient) where the blocking coefficient is between 0 and 1. A
	 * computation-intensive task has a blocking coefficient of 0, whereas an
	 * IO-intensive task has a value close to 1, so we don't have to worry about
	 * the value reaching 1.
	 * @param blockingCoefficient the coefficient
	 * @return Thread pool size
	 */
	private static int poolSize(double blockingCoefficient) {
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
		return poolSize;
	}

	/**
	 * 获取CPU核心数
	 */
	private static int getCores() {
		return Runtime.getRuntime().availableProcessors();
	}
}
