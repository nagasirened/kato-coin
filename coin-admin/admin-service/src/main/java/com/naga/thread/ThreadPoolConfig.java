package com.naga.thread;


import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Primary
    @Bean("asyncThreadPool")
    public ThreadPoolExecutor asyncThreadPool() {
        return new ThreadPoolExecutor(
                3,
                5,
                5,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(300),
                new DefaultThreadFactory("async-thread-pool"),
                new AdminRejectAsyncThreadPoolHandler()
        );
    }

    class AdminRejectAsyncThreadPoolHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            throw new RejectedExecutionException("多路召回RecallSource线程池 Task " + task.toString() + " rejected from " + executor.toString());
        }
    }
}
