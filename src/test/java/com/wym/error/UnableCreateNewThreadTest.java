package com.wym.error;


/**
 * 导致原因：
 * 1.应用创建线程太多（超过硬件限制，操作系统句柄数）
 * 2.服务器不允许你的应用创建这么多线程，linux默认限制一个应用最多可以创建1024个线程
 *
 * 解决方法：
 * 1.想办法减少线程数
 * 2.修改linux服务器配置，扩大linux默认限制
 *
 * Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
 * 	at java.lang.Thread.start0(Native Method)
 *
 * 	查看当前用户打开的应用可以申请的线程总数：
 * 	ulimit -u ,普通用户默认4096
 * 	cat /etc/security/limits.d/20-nproc.conf
 *
 * 	20-nproc.conf内容如下：
 *
 * 	# Default limit for number of user's processes to prevent
 * # accidental fork bombs.
 * # See rhbz #432903 for reasoning.
 *
 * *          soft    nproc     4096
 * root       soft    nproc     unlimited
 *
 */
public class UnableCreateNewThreadTest {

    public static void main(String[] args) {

        int i = 0;
        while (true) {
            System.out.println("线程个数：" + ++i);
            new Thread(() -> {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
