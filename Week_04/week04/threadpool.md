# 线程池
1. Executor: 执行者-顶层接口
2. ExecutorService:接口api
3. ThreadFactory:线程工厂
4. Executors:工具类，创建线程

Executor <- ExecutorService <- AbstractExecutorService <- ThreadPoolExecutor
## Executor 执行者
void execute(Runnable runnable) 

## ExecutorService 接口API
submit 异常能捕获到，有返回值，使用了Future封装

execute 异常捕获不到，无返回值

#3 ThreadFactory 线程工厂

cpu密集型 n/n+1

io密集型 2n/2n+2

## Callable 接口/Runnable 接口
有返回值，没返回值

使用Future 来接返回值

## 锁

synchronized 加锁，不够灵活
wait/notify 看作解锁，加锁

Lock 更灵活的锁

可重入锁：同一线程持有的锁，可以重复进入，不需要等待
公平锁，排队获取锁，非公平则可插队