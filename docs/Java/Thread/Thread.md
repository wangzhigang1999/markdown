# 多线程

## 线程

线程是操作系统调度的最小单位,每个线程都有自己的计数器,栈和局部变量等属性.

## 多线程一定快吗

多线程会有`上下文的切换`,创建线程会有额外的开销.

不同类型的任务场景也会有不同的结果, CPU 密集型的任务 就不如 IO密集型任务的效率高

同时也受制于硬件,单核CPU使用多线程就不如多核CPU

因此结论就是 `不一定`

## 线程的状态

Java中的线程主要有六种状态

![img](Thread.assets/1771072-20200209204908012-1070559737.png)

### New

当一个线程被 `new ` 出来的时候,就进入这种状态

```Java
Thread thread = new Thread(() -> System.out.println("hello"));
```

### Runnable

调用 Thread.start 方法后进入这个状态. 但实际上仍需等待CPU进行调度,获得时间片之后真正的运行

```Java
thread.start();
```

### Blocked

尝试获取锁时阻塞,获取到锁进入 Runnable 状态. 注意  **只有Synchronized获取锁会阻塞,Lock相关会进入Waiting状态**

### Waiting

调用 wait/join 方法后进入这个状态. 只能等待被唤醒.唤醒后进入 Runnable 状态

### Time_Wating

调用 wait(Time)/sleep/join(Time) 方法进入,在超时时间到或者被唤醒进入 Runnable 状态

### Terminated

终止

## Java内存模型

顺序一致性模型

JMM 屏蔽了不同的处理器的细节

![img](Thread.assets/1102674-20180815143324915-2024156794.png)

	线程之间的通信模型有两种: 共享内存和消息传递. Java采用的是共享内存的方式. 线程之间的共享变量存储在共享内存中,每个线程拥有自己的私有内存(工作内存),私有内存里保存了共享变量的副本.

线程私有内存是一个抽象概念,并不是真正的物理内存.

## 线程间通信

### volatile

### synchronized

### 等待通知机制

### 管道输入/输出流

这是一种比较神奇的想法

```Java
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * @author wangz
 */
public class Solution {
    public static void main(String[] args) throws IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();
        writer.connect(reader);

        Thread printThread = new Thread(new Print(reader), "PrintThread");
        Thread inputThread = new Thread(new Reader(writer), "InputThread");
        inputThread.start();
        printThread.start();


    }
}

class Print implements Runnable {
    PipedReader in;

    public Print(PipedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        int rec;
        while (true) {
            try {
                if (((rec = in.read()) == -1)) {
                    break;
                }
                System.out.print((char) rec);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


class Reader implements Runnable {
    PipedWriter writer;

    public Reader(PipedWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {

        int rec;

        while (true) {
            try {
                if (((rec = System.in.read()) == -1)) {
                    break;
                }
                writer.write(rec);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### Thread.Join

### ThreadLocal

用于线程之间变量的隔离

## volatile详解

volatile 是轻量级的 synchronized, 保证了共享变量在线程之间的可见性. 简而言之,被 volatile 修饰的变量,每次读会从内存读,每次写会强制刷新CPU的缓存

### CPU术语定义

![Java并发编程的艺术](Thread.assets/2019082522511518.jpg)

### volatile 指令

​	当一个变量被声明为 volatile 时,汇编指令会多出一条 `lock addl $0x0 (%esp)`

​	lock 指令会锁住总线

​	这条指令会将当前处理器缓存行中的数据写回内存,同时使其他处理器缓存了该内存地址的数据无效. 之二是通过`缓存一致性协议`来实现的. 处理器可以嗅探其他处理器的缓存和系统内存, 使得数据总是一致的.

### 禁止指令重排

​	编译器处理器为了提高指令执行的效率,会在不影响指令执行结果的条件下进行指令重排序,改变指令执行的顺序.但是在多线程环境下,指令重排序有可能会导致``内存可见性``的问题. 因此编译器会在适当的位置插入特定类型的内存屏障来禁止指令重排.

四大内存屏障,其中 `StoreLoad`可以实现其他三个的所有功能,同时开销也比价昂贵

![img](Thread.assets/9930763-7ddc38e0eaa3aec9.png)

volatile 重排序规则:

	- 如果第二个指令是 volatile 写,第一个指令绝对不能重排序
	- 如果第一个指令是 volatile 读,第二个指令一定不能重排序
	- 当第一个操作是 volatile 写,第二个指令是 volatile 读的时候,不能重排序

JMM 内存屏障插入策略 : S=Store  L=Load

volatile 写 前 SS

volatile 写 后 SL

volatile 读 后 LL

volatile 读 后 SL

上边是最保守的策略,适用于任意的处理器.

**在X86处理器上,只会在volatile写后边插入 StoreLoad**

## synchronized详解

这里引用一张原理图,图源见水印:

![preview](Thread.assets/v2-9db4211af1be81785f6cc51a58ae6054_r.jpg)

​	synchronized 是通过进入和退出 `Monitor对象`来实现方法同步和代码块同步,但两者的实现有差异.对于代码块会在字节码中添加 monitorEnter 和 monitorExit 指令, 对于方法会声明为 ACC_Synchronized. 

​	本质上讲, synchronized 获得锁的方式就是向对象头中的特定区域放入自己的线程ID,成功则表示获取了锁,否则继续尝试.

### Java对象头

​	所有的Java对象都有Java对象头的存在,对象头中包括`MarkWord标记字` `classMetaData 类元信息` 等等,数组类型还包括数组长度.

​	与synchronized相关的锁信息主要存在于MarkWord中

​	MarkWord中的锁信息主要包含四种: 无锁 偏向锁 轻量级锁 重量级锁. 随着竞争的加剧,会实现`锁升级`的过程. 锁只能升级不能降级.

### 偏向锁

​	为什么会出现偏向锁? 因为实际运行中锁的竞争是很少的,而且总是由固定的线程去获得锁. 因此在某个线程第一次获取锁时,在对象头中记录线程ID, 将偏向锁的标志位置1. 这样在以后的访问中可以省略CAS操作.

​	形象的理解就是线程把自己的ID贴到对象头上声明这个对象已经被他独占了

> 偏向锁的撤销

​	当有线程尝试竞争锁时, Java虚拟机会继续执行到全局安全点, 然后暂停拥有偏向锁的线程.

​	 如果此时拥有偏向锁的线程不处于活动状态,则将锁状态置为无锁.

​	 如果线程仍然活动着,拥有偏向锁的栈会被执行,遍历偏向对象的锁记录,栈中的锁记录和对象头的MarkWord要么重新偏	向其他线程,要么恢复到无锁,要么标记对象不适合作为偏向锁.

​	最后唤醒暂停的线程

### 轻量级锁

只要出现线程的竞争时, 就会将偏向锁升级为轻量级锁(自旋锁). 多个线程尝试使用 CAS将自己ID放到MarkWord中.

### 重量级锁

当竞争轻量级锁的线程自旋超过 **10**次时,就会升级为重量级锁. 因为太多无意义的自旋会大量的消耗 CPU 资源. 但是当前的 JVM 有自适应自旋,不一定是 10 次.

重量级锁中有一个队列,按照队列来进行调度.

重量级锁会存在用户态和内核态的切换,开销比较大,但是不消耗 CPU

## CAS

比较并交换,在UnSafe类中有大量的相关方法,底层是通过处理器的 `CMPX-CHG`实现的

CAS的过程可以满足 volatile 的语义

### ABA问题

即有一个变量 A 被修改为了 B, 然后又被修改为了 A. 从外观上看是没有变化的,但实际上已经被修改过了.

解决这个问题的办法是使用版本号,每次修改版本号自增

## 锁

### 锁的内存语义

当线程释放锁时,JMM会将工作内存中的共享变量写回主内存中.

锁释放与 volatile 写有相同的内存语义

锁获取与 volatile 读有相同的内存语义

线程释放一个锁,实际上是线程向接下来要获取这个锁的线程发出了一个消息

线程获取一个锁,实际上是对之前消息的响应

释放与获取的过程实际上是两个线程通过主内存进行消息传递的过程

### Lock接口

![Lock](Thread.assets/Lock.svg)

Lock 接口的常用实现类只有 ReentrantLock ,虽然使用不如 synchronized 便捷,但是更加的灵活.

## AQS

抽象队列同步器 是一个**基础框架**,通过继承和重写一些方法可以实现自己的同步工具.

![AbstractQueuedSynchronizer](Thread.assets/AbstractQueuedSynchronizer-1601103071048.svg)

#### 同步队列

每个同步器中都存在一个FIFO双向队列来管理同步状态. 当当前线程获取锁失败时,会将当前线程以及一些信息包装成一个节点,然后将其加入同步队列,同时阻塞当前线程. 当锁被释放时,唤醒队列的首节点,让其尝试获取锁.

![img](Thread.assets/20160816191536361)

```Java
abstract static class Node {
    volatile Node prev;       // initially attached via casTail
    volatile Node next;       // visibly nonnull when signallable
    Thread waiter;            // visibly nonnull when enqueued
    volatile int status;      // written by owner, atomic bit ops by others
    Node nextWaiter;		 // 在共享模式下才有意义的属性
}
```

#### 获取锁

以 ReentrantLock 为例: 首先会尝试CAS直接获取锁,如果成功则结束,否则转去 acquire 方法,

```Java
final void lock() {
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}
```

acquire 方法是定义在 AQS 中的模板方法

```Java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&  acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

第一个 tryAcquire 方法最终会调用下边这个方法

```Java
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    // 再次检查当前锁的状态,无锁就获得
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    // 如果当前线程已经持有了这把锁,再获取一次,重数+1. 重数的上限是Integer的最大值
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    // 未获取到,返回 false
    return false;
}
```

当获取失败后就会执行这个方法 `acquireQueued(addWaiter(Node.EXCLUSIVE), arg))`,但是正式执行之前将当前线程包装成了一个节点采用CAS放到队列的尾巴

```Java
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    // Try the fast path of enq; backup to full enq on failure
    Node pred = tail;
    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    enq(node);
    return node;
}
```

包装完成之后就进入 acquireQueued 方法,如果当前节点的前驱结点是头结点,尝试一次CAS获取锁,若失败,则进行等待.

```Java
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        // 自旋
        for (;;) {
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

#### 锁的释放

```Java
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
            //锁释放完后唤醒当前节点的后继节点
            unparkSuccessor(h);
        return true;
    }
    return false;
}
```

```Java
protected final boolean tryRelease(int releases) {
	// 因为是可重入锁,因此每加一次锁就要释放一次锁,不能多也不能少.
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```

