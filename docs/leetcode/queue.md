# 队列

## 队列的最大值

```java
// 与最小栈的思路完全一致,
class MaxQueue {
    Queue<Integer> queue;
    Deque<Integer> max;

    public MaxQueue() {
        queue = new LinkedList<>();
        max = new LinkedList<>();
    }

    public int max_value() {
        if (max.isEmpty()) {
            return -1;
        }
        return max.peekFirst();
    }

    public void push_back(int value) {
        queue.add(value);
        //辅助队列保持从大到小, 把所有小于当前值的元素全部去掉, 保证辅助队列的单调性
        while (!max.isEmpty() && value > max.peekLast()) {
            max.removeLast();
        }
        max.addLast(value);
    }

    public int pop_front() {
        if (queue.isEmpty()) {
            return -1;
        }
        int e = queue.remove();
        if (e == max.peekFirst()) {
            max.removeFirst();
        }
        return e;
    }
}
```

## 滑动窗口的最大值

### 队列

```Java
public int[] maxSlidingWindow(int[] nums, int k) {
    if (nums == null || nums.length <= 0) {
        return new int[0];
    }

    LinkedList<Integer> res = new LinkedList<>();

    Deque<Integer> max = new LinkedList<>();
	// 预处理一下队列, 题干保证不会越界
    for (int i = 0; i < k; i++) {
        // 只要 max 不为空且最后一个元素小于当前的值, 就将最后的元素弹出 , 然后将当前的值加入到队尾
        while (!max.isEmpty() && max.peekLast() < nums[i]) {
            max.removeLast();
        }
        max.addLast(nums[i]);
    }
    res.add(max.peek());

  // i 是窗口后边的第一个元素, 也是即将进入滑动窗口的元素, 那么即将从窗口出去的元素就是 nums[(i-k)]
    for (int i = k; i < nums.length; i++) {
        if (nums[i - k] == max.peekFirst()) {
            max.removeFirst();
        }
        while (!max.isEmpty() && max.peekLast() < nums[i]) {
            max.removeLast();
        }
        max.addLast(nums[i]);
        res.add(max.peek());
    }
    return res.stream().mapToInt(Integer::intValue).toArray();
}
```

## 数据流的中位数

```java
import java.util.PriorityQueue;
import java.util.Queue;
// 采用两个堆, 一个大顶堆,一个小顶堆,中间的元素就是中位数
class MedianFinder {
    Queue<Integer> small, big;

    public MedianFinder() {
        small = new PriorityQueue<>();
        big = new PriorityQueue<>((x, y) -> (y - x));
    }

    public void addNum(int num) {
        if (small.size() != big.size()) {
            small.add(num);
            big.add(small.poll());
        } else {
            big.add(num);
            small.add(big.poll());
        }
    }

    public double findMedian() {
        if (small.size() != big.size()) {
            return small.peek();
        } else {
            return (small.peek() + big.peek()) / 2.0;
        }
    }
}
```