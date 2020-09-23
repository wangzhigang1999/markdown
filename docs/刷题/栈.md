# 栈

## 最小栈

```java
class MinStack {

    Stack<Integer> stack = new Stack<>();
    Stack<Integer> min = new Stack<>();
    

    // 在插入的时候, 如果当前的元素比最小的元素还要小, 那么将这个元素压入
    public void push(int x) {
        stack.push(x);
        if (min.isEmpty() || min.peek() >= x) {
            min.push(x);
        }
    }
// 在弹出的时候, 如果当前元素等于最小的元素,则将最小的元素同时弹出
    public void pop() {
        if (stack.pop().equals(min.peek())) {
            min.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int min() {
        return min.peek();
    }
}
```

## 栈排序

```java
private static void stackSort(Stack<Integer> stack) {

    Stack<Integer> help = new Stack<>();
    while (!stack.isEmpty()) {
        Integer curr = stack.pop();
            while (!help.isEmpty() && curr > help.peek()) {
                stack.push(help.pop());
            }
        help.push(curr);
    }

    while (!help.isEmpty()) {
        stack.push(help.pop());
    }

}
```

## 合法栈序列

```java
public boolean validateStackSequences(int[] pushed, int[] popped) {
    if (pushed.length != popped.length) {
        return false;
    }
    Stack<Integer> stack = new Stack<>();

    int index = 0;

    for (int i : pushed) {
        stack.push(i);

        while (!stack.isEmpty() && stack.peek() == popped[index]) {
            stack.pop();
            index++;
        }
    }

    return stack.isEmpty();
}
```

## 用两个栈实现队列

### 朴素想法

```java
class CQueue {
    Stack<Integer> stackA = new Stack<>();
    Stack<Integer> stackB = new Stack<>();

    public CQueue() {

    }

    public void appendTail(int value) {
        stackA.push(value);
    }

    public int deleteHead() {
        if (stackA.isEmpty()) {
            return -1;
        }
        while (!stackA.isEmpty()) {
            stackB.push(stackA.pop());
        }
        Integer res = stackB.pop();
        while (!stackB.isEmpty()) {
            stackA.push(stackB.pop());
        }
        return res;
    }
}
```

### 优化解法

```java
class CQueue {
    Stack<Integer> stackA,stackB;
    public CQueue() {
        stackA = new Stack<>();
        stackB = new Stack<>();
    }
    public void appendTail(int value) {
        stackA.push(value);
    }

    public int deleteHead() {
        if(stackA.empty() && stackB.empty()){
            return -1;
        }
        if(stackB.empty()){
            while(!stackA.empty()){
                stackB.push(stackA.pop());
            }
        }
        return stackB.pop();
    }
```

## 有效的括号

// 很常规的思路, 辅助栈法

```java
import java.util.Stack;

class Solution {
    public boolean isValid(String s) {
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (char aChar : chars) {
            if (aChar == ')' || aChar == '}' || aChar == ']') {
                if (!stack.isEmpty() && (aChar - stack.peek() <= 2) && (aChar - stack.peek() > 0)) {
                    stack.pop();
                } else {
                    stack.push(aChar);
                }
            } else {
                stack.push(aChar);
            }
        }
        return stack.isEmpty();
    }
}
```