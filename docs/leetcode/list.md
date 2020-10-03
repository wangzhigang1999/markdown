# 链表

## 有序链表合并

```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode newHead = new ListNode(0), cur = newHead;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        if (l1 != null) {
            cur.next = l1;
        } else {
            cur.next = l2;
        }
        return newHead.next;
    }
}
```

## 单链表反转

```java
public ListNode reverseList(ListNode head) {

    ListNode pre = null;
    ListNode current = head;

    while (current != null) {
        ListNode temp = current.next;
        current.next = pre;
        pre = current;
        current = temp;
    }

    return pre;

}
```

## 回文链表

###  暴力

```java
public boolean isPalindrome(ListNode head) {
    Deque<Integer> deque = new LinkedList<>();
    while (head != null) {
        deque.addFirst(head.val);
        head = head.next;
    }

    while (!deque.isEmpty()) {
        if (deque.size() == 1) {
            return true;
        }
        Integer first = deque.removeFirst();
        Integer last = deque.removeLast();
        if (!first.equals(last)) {
            return false;
        }
    }
    return true;
}
```

### 链表反转+快慢指针

## 倒数第k

### 暴力

```java
public ListNode getKthFromEnd(ListNode head, int k) {
    LinkedList<ListNode> list = new LinkedList<>();
    list.add(head);
    while (head != null) {
        ListNode node = head;
        list.add(node);
        head = head.next;
    }

        return list.get(list.size()-k);
}
```

### 统计长度

```java
public ListNode getKthFromEnd(ListNode head, int k) {
    int length = 0;
    ListNode node = head;
    while (head != null) {
        head = head.next;
        length++;
    }
    for (int i = 0; i < length - k; i++) {
        assert node != null;
        node = node.next;
    }
    return node;
}
```

### 双指针(快慢指针)

> 一个指针先向前走 $k$ 步,然后另一个指针出发. 第一个指针到达第二个指针就是倒数第 k 个数

```java
public ListNode getKthFromEnd(ListNode head, int k) {
    ListNode pre = head;
    ListNode after = head;
    for (int i = 0; i < k; i++) {
        pre=pre.next;
    }
    while (pre!=null){
        pre=pre.next;
        after=after.next;
    }
    return after;
}
```

## 相交链表

### 双指针

双指针, 思路很神奇

```Java
public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
    if (headA == null || headB == null) {
        return null;
    }

    ListNode A = headA, B = headB;
    while (A != B) {
        if (A == null) {
            A = headB;
        } else {
            A = A.next;
        }
        if (B == null) {
            B = headA;
        } else {
            B = B.next;
        }
    }

    return B;
}
```

### 精简

```java
public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
    ListNode a = headA;
    ListNode b = headB;
    
    while ((headA != headB)) {
        headA = headA == null ? b : headA.next;
        headB = headB == null ? a : headB.next;
    }
    return headA;
}
```

## 环形链表

第一种朴素的想法, 将每个节点加入到一个集合中, 如果加入的过程中发现已经加入过了,说明有环.

```java
public boolean hasCycle(ListNode head) {
    HashSet<ListNode> set = new HashSet<>();

    while (head!=null){
        if (set.contains(head)){
            return true;
        }else {
            set.add(head);
        }
        head=head.next;
    }
    return false;
}
```

快慢指针的想法, 两个速度不同绕圈则必然会追上

```java
public boolean hasCycle(ListNode head) {
    if (head==null || head.next==null){
        return false;
    }

    ListNode slow = head;
    ListNode fast = head.next;
    while (slow != fast) {
        if (fast == null || fast.next == null) {
            return false;
        }
        slow = slow.next;
        fast = fast.next.next;
    }
    return true;
    
}
```

## 链表的第k个节点

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode getKthFromEnd(ListNode head, int k) {
        ListNode node = head;
        int cnt =0;
        while (node!=null){
            cnt++;
            node=node.next;
        }
        
        cnt-=k;
        
        while (cnt!=0 && head!=null){
            head=head.next;
            cnt--;
        }
        return head;
    }
}
```

## 删除链表的节点

当删除的节点是头结点时特殊处理

```java
class Solution {

    ListNode pre;
    ListNode current;

    public ListNode deleteNode(ListNode head, int val) {
        if(head==null){
            return null;
        }
        if (head.val == val) {
            return head.next;
        }
        
        current = head;
        while (current != null) {
            if (current.val == val) {
                pre.next = current.next;
                break;
            }
            pre = current;
            current = current.next;
        }
        return head;
    }
}
```

## 链表的第一个公共节点

要求不能改变原来的链表

### HashSet

很简单直接的思路, 将一条链表上的所有节点全部加入集合中,再从第二条链表开始进行遍历,若发现节点在集合中,说明是交点.

```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
    HashSet<ListNode> set = new HashSet<>();
    while(headA!=null){
        set.add(headA);
        headA = headA.next;
    }

    while(headB!=null){
        if(set.contains(headB)){
            return headB;
        }
        headB = headB .next;
    }
    return null;
        
    }
}
```

### 双指针

类比两个人在操场上跑,即使速度不同最终一定会相遇

> 第一种思路

```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        ListNode a =headA;
        ListNode b =headB;
		// 每当一条链表遍历完成之后就从另一条链表的起始处进行遍历,这样最多只需要将两条链表都遍历一次
        //  就能找到交点
        while(headB!=headA){
            if(headB==null){
                headB=a;
            }else{
              headB =headB.next;
            }
            if(headA==null){
                headA=b;
            }else{
              headA=headA.next;
            }
        }

        return headA;
    }
}
```

> 第二种思路

```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        ListNode a =headA;
        ListNode b =headB;

        while(headB!=headA){
            if(headB==null){
                // 此处,当链表遍历完成之后,不是去另一条链表的开始处,而是回到自己的开头位置
                // 这样,需要遍历两条链表的 最小公倍数 次才能到达终点
                headB=b;
            }else{
              headB =headB.next;
            }
            if(headA==null){
                headA=a;
            }else{
              headA=headA.next;
            }
        }

        return headA;
    }
}
```

