package com.ke.idcard;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *线程管理类
 */
public class ThreadManager {

    private volatile static ThreadPool mThreadPool;

    public static ThreadPool getThreadPool() {
        if (mThreadPool == null) {
            synchronized (ThreadManager.class) {
                if (mThreadPool == null) {
                    int cpuCount = Runtime.getRuntime().availableProcessors();// 获取cpu数量
                    int threadCount = 1;
                    mThreadPool = new ThreadPool(threadCount, threadCount, 1L);
                }
            }
        }

        return mThreadPool;
    }

    // 线程池
    public static class ThreadPool {

        private int corePoolSize;// 核心线程数
        private int maximumPoolSize;// 最大线程数
        private long keepAliveTime;// 休息时间

        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize, int maximumPoolSize,
                           long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }


        public void execute(Runnable r) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize,
                        maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
                // 参1:核心线程数，除非allowCoreThreadTimeOut被设置为true，否则它闲着也不会死
                // 参2:最大线程数,活动线程数量超过它，后续任务就会排队  ;
                // 参3:超时时长，作用于非核心线程（allowCoreThreadTimeOut被设置为true时也会同时作用于核心线程），闲置超时便被回收 ;
                // 参4:枚举类型，设置keepAliveTime的单位;
                // 参5:缓冲任务队列，线程池的execute方法会将Runnable对象存储起来  ;
                // 参6:线程工厂接口，只有一个new Thread(Runnable r)方法，可为线程池创建新线程  ;
                // 参7:线程异常处理策略
            }

            // 线程池执行一个Runnable对象, 具体运行时机线程池说了算
            executor.execute(r);
        }

        // 取消任务
        public void cancel(Runnable r) {
            if (executor != null) {
                // 从线程队列中移除对象
                executor.getQueue().remove(r);
            }
        }
    }

}
