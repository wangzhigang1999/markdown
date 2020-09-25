## 除自身外数字的乘积

### 简单算法 双 O{N}

```Java
// 时间换空间
public int[] productExceptSelf(int[] nums) {
    int[] left = new int[nums.length];
    int[] right = new int[nums.length];
    left[0] = 1;
    int sum = nums[0];
    for (int i = 1; i < nums.length; i++) {
        left[i] = sum;
        sum *= nums[i];
    }
    right[nums.length - 1] = 1;
    sum = nums[nums.length - 1];
    for (int i = nums.length - 2; i >= 0; i--) {
        right[i] = sum;
        sum *= nums[i];
    }
    int[] ints = new int[nums.length];
    for (int i = 0; i < ints.length; i++) {
        ints[i] = left[i] * right[i];
    }
    return ints;
}
```

### 空间 O{N}

```java
public int[] productExceptSelf(int[] nums) {

    int[] res = new int[nums.length];
    res[0] = 1;

    int sum = 1;
    for (int i = 0; i < nums.length; i++) {
        res[i] = sum;
        sum *= nums[i];
    }
    
    sum = 1;
    for (int i = res.length - 1; i >= 0; i--) {
        res[i] *= sum;
        sum *= nums[i];
    }
    return res;
}
```

## 左旋字符串



```java
public String reverseLeftWords(String s, int n) {

    return s.substring(n ) + s.substring(0, n);
}
```

## 顺子

```java
public boolean isStraight(int[] nums) {
    Set<Integer> repeat = new HashSet<>();
    int max = 0, min = 14;
    for (int num : nums) {
        if (num == 0) {
            continue; // 跳过大小王
        }
        max = Math.max(max, num); // 最大牌
        min = Math.min(min, num); // 最小牌
        if (repeat.contains(num)) {
            return false; // 若有重复，提前返回 false
        }
        repeat.add(num); // 添加此牌至 Set
    }
    // 最大牌 - 最小牌 < 5 则可构成顺子
    return max - min < 5;
}
```

## 复杂链表的复制 HashMap

```java
// 空间占用较大
public Node copyRandomList(Node head) {
    HashMap<Node, Node> map = new HashMap<>();
    Node node = head;
    while (node != null) {
        map.put(node, new Node(node.val));
        node = node.next;
    }

    node = head;
    while (node != null) {
        map.get(node).next = map.get(node.next);
        map.get(node).random = map.get(node.random);
        node=node.next;
    }
    return map.get(head);
}
```

## 两数之和 HashMap

```java

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            // 假设存在两个相同的值,那么在将其添加到map的时候后边的值会将前边的值覆盖
            // 而 for 循环是从前往后迭代的,因此两个相同的值互不影响
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[]{i, map.get(complement)};
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] a = {3, 3};
        System.out.println(Arrays.toString(new Solution().twoSum(a, 6)));
    }
}
```

## 三数之和

```java
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> lists = new ArrayList<>();
    //排序
    Arrays.sort(nums);
    //双指针
    int len = nums.length;
    for (int i = 0; i < len; ++i) {
        // 所有的数字都大于0,则结果必然不会等于零
        if (nums[i] > 0) {
            return lists;
        }

        // 遇到了两个相同的数字
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }

        int now = nums[i];

        int left = i + 1, right = len - 1;
        while (left < right) {
            int tmp = now + nums[left] + nums[right];
            if (tmp == 0) {
                List<Integer> list = new ArrayList<>();
                list.add(now);
                list.add(nums[left]);
                list.add(nums[right]);
                lists.add(list);
                // 这两个while循环是为了保证不重复
                while (left < right && nums[left + 1] == nums[left]) {
                    left++;
                }
                while (left < right && nums[right - 1] == nums[right]) {
                    right--;
                }
                left++;
                right--;
            } else if (tmp < 0) {
                left++;
            } else {
                right--;
            }
        }
    }
    return lists;
}
```

## 四数之和 



## 移动零

```java
public void moveZeroes(int[] nums) {
    int lastNonZeroFoundAt = 0;
   
    // 把所有的不为零的数组移动到数组的前边.
    // 剩余的位置自然是零了.
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] != 0) {
            nums[lastNonZeroFoundAt++] = nums[i];
        }
    }
    for (int i = lastNonZeroFoundAt; i < nums.length; i++) {
        nums[i] = 0;
    }


}
```

## 和为S的连续正数序列 双指针

 双指针的思想

```java
    public int[][] findContinuousSequence(int target) {
        List<int[]> res = new ArrayList<>();
        int sum = 3, left = 1, right = 2;
        while ((left < right) && (right <= target)) {
            if (sum == target) {
                int[] temp = new int[right - left + 1];
                for (int i = left; i <= right; i++) {
                    temp[i - left] = i;
                }
                res.add(temp);
                sum -= left++;
            } else if (sum > target) {
                sum -= left++;
            } else {
                sum += ++right;
            }
        }
        return res.toArray(new int[res.size()][]);
    }
```

## 多数元素

### 暴力

```java
class Solution {
    public int majorityElement(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

            if (entry.getValue() > nums.length / 2) {
                return entry.getKey();
            }
        }

        return 0;
    }


}
```

### 投票算法

1. 如果候选人不是maj 
   - 则 maj会和其他非候选人一起反对 会反对候选人,所以候选人一定会下台(maj==0时发生换届选举)
2. 如果候选人是maj ,
   - 则maj 会支持自己，其他候选人会反对，同样因为maj 票数超过一半，所以maj 一定会成功当选

```java
public int majorityElement(int[] nums) {
    int count = 0;
    int candidate = 0;

    for (int num : nums) {
        if (count == 0) {
            candidate = num;
        }
        if (num == candidate) {
            count++;
        } else {
            count--;
        }
    }

    return candidate;
}
```

### 排序

```java
public int majorityElement(int[] nums) {
    Arrays.sort(nums);
    return nums[nums.length / 2];
}
```

## 找到消失的数字

### 原地

```java
public List<Integer> findDisappearedNumbers(int[] nums) {

    for (int i = 0; i < nums.length; i++) {

        int newIndex = Math.abs(nums[i]) - 1;

        nums[newIndex] =Math.abs(nums[newIndex]);

    }


    LinkedList<Integer> list = new LinkedList<>();

    for (int i = 0; i < nums.length; i++) {
        if (nums[i] > 0) {
            list.add(i + 1);
        }
    }
    return list;

}
```

### HashMap版本

```java
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        LinkedList<Integer> list = new LinkedList<>();

        HashSet<Integer> integers = new HashSet<>();
        for (int num : nums) {
            integers.add(num);
        }

        for (int i = 1; i < nums.length + 1; i++) {
            if (!integers.contains(i)){
                list.add(i);
            }
        }

        return list;

    }
}
```

## 最短无序连续子数组

### 暴力

```java
import java.util.Arrays;

class Solution {
    public int findUnsortedSubarray(int[] nums) {

        int[] copy = Arrays.copyOf(nums, nums.length);

        Arrays.sort(copy);

        int left = 0;
        while (left < nums.length) {
            if (copy[left] == nums[left]) {
                left++;
            } else {
                break;
            }
        }
        if (left == nums.length) {
            return 0;
        }

        int right = nums.length - 1;
        while (right > 0) {
            if (copy[right] == nums[right]) {
                right--;
            } else {
                break;
            }
        }

        return right - left;
    }
}
```

### 找边界

```java
class Solution {
    public int findUnsortedSubarray(int[] nums) {
        if (nums.length <= 1) {
            return 0;
        }

        int left = -1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                left = i;
                break;
            }
        }
        if (left == -1) {
            return 0;
        }
        int right = 200000;
        for (int i = nums.length - 1; i > 0; i--) {
            if (nums[i - 1] > nums[i]) {
                right = i;
                break;
            }
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = left; i <= right; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
            if (nums[i] < min) {
                min = nums[i];
            }
        }
        for (int i = 0; i < left; i++) {
            if (nums[i] > min) {
                left = i;
                break;
            }
        }
        for (int i = nums.length - 1; i >= right; i--) {
            if (nums[i] < max) {
                right = i;
                break;
            }
        }

        return right - left + 1;

    }

}
```

## N 的幂

> 3 的幂

```java
 class Solution {
    public boolean isPowerOfThree(int n) {
        // 这个神奇的数字是 int 能表示的最大的三的幂
        // 同理其余n 的幂均可以按照这个办法计算
        return n > 0 && 1162261467 % n == 0;
    }
}
```

## 合法栈序列

```java
public boolean validateStackSequences(int[] pushed, int[] popped) {

    int popIndex = 0;
    int pushIndex=0;
    Stack<Integer> stack = new Stack<>();
    while (pushIndex<pushed.length) {
        if (stack.isEmpty() || stack.lastElement() != popped[popIndex]) {
            stack.push(pushed[pushIndex]);
            pushIndex++;
        } else {
            stack.pop();
            popIndex++;
        }
    }

    while (!stack.isEmpty()){
        Integer pop = stack.pop();
        if (pop!=popped[popIndex++]){
            return false;
        }
    }

    return true;
}
```

## 同构字符串

```java
class Solution {
    public boolean isIsomorphic(String s, String t) {
        if (s == null || t == null) {
            return false;
        }
        HashMap<Character, Character> pattern = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            char d = t.charAt(i);
            if (pattern.containsKey(c)) {
                Character character = pattern.get(c);
                if (character != d) {
                    return false;
                }
            } else {
                pattern.put(c, d);
            }

        }
        return pattern.size()==pattern.values().stream().distinct().count();
    }

    public static void main(String[] args) {
        System.out.println(new Solution().isIsomorphic("paper", "title"));
    }
}
```

## 出现两次的数字

```java
public int[] singleNumbers(int[] nums) {

    //用于将所有的数异或起来
    int k = 0;
    for (int num : nums) {
        k ^= num;
    }

    int mask = (k & (-k));

    // 核心就是分组异或, 但是必须要将只出现一次的两个数字分到两组
    int a = 0;
    int b = 0;
    for (int num : nums) {
        if ((num & mask) == 0) {
            a ^= num;
        } else {
            b ^= num;
        }
    }

    return new int[]{a, b};
}
```

## 二进制中1的位数

```java
public int hammingWeight(int n) {

    int cnt = 0;

    while (n != 0) {
        cnt += (n & 1);
        n >>>= 1;
    }
    return cnt;
}
```

## 计算等差数列的和

```java
// 题目要求不使用相关的关键字, 只能使用递归, 则尽量尝试去打破递归条件
public int sumNums(int n) {
    boolean x = (n > 1) && (n += sumNums(n - 1))>-1 ;
    return n;
}
```

## 约瑟夫环

### 递归

```java
public int lastRemaining(int n, int m) {
    if (n == 1) {
        return 0;
    } else {
        return (lastRemaining(n - 1, m) + m) % n;
    }
}
```

### 递推

```java
class Solution {
    public int lastRemaining(int n, int m) {
        int ans = 0;
        // 最后一轮剩下2个人，所以从2开始反推
        for (int i = 2; i <= n; i++) {
            ans = (ans + m) % i;
        }
        return ans;
    }
}
```

### 使用arrayList 模拟

```java
public int lastRemaining(int n, int m) {
    ArrayList<Integer> list = new ArrayList<>();

    for (int i = 0; i < n; i++) {
        list.add(i);
    }

    int index = 0;
    while (n > 1) {
        list.remove((index + m - 1) % n);
        index = (index + m - 1) % n;
        n--;
    }
    return list.get(0);
}
```

## 把数组排成最小的数

https://leetcode-cn.com/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/solution/

```java
import java.util.Arrays;

class Solution {
    public String minNumber(int[] nums) {
        String[] s = new String[nums.length];

        for (int i = 0; i < nums.length; i++) {
            s[i] = String.valueOf(nums[i]);
        }

        // 核心在于排序
        Arrays.sort(s, (x, y) -> (x + y).compareTo(y + x));

        StringBuilder builder = new StringBuilder();

        for (String s1 : s) {
            builder.append(s1);
        }
        return (builder.toString());
    }
}
```

## 位运算实现加法

```java
class Solution {
    // a 看做是和 , b 看做是进位, 当进位为0的时候, 和就是结果.
    public int add(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return add(a ^ b, (a & b) << 1);
        }
    }
}
```

## 剪绳子

### 朴素递归

```java
// 在数据为 31 时会超时
// 因为计算了大量的重复数据
public int cuttingRope(int n) {
    if (n == 1) {
        return 1;
    } else {
        int max = 0;
        for (int i = 1; i < n; i++) {
            //cuttingRope(n - i) * i 表示将剩下的绳子继续剪断
            // (n - i) * i 表示不剪断剩下的绳子
            max = Math.max(max, Math.max(cuttingRope(n - i) * i, (n - i) * i));
        }
        return max;
    }
}
```

### 记忆化递归

```java
import java.util.HashMap;
// 空间换时间
class Solution {
    HashMap<Integer, Integer> mem = new HashMap<>();

    public int cuttingRope(int n) {
        if (n == 1) {
            return 1;
            // 在递归的时候如果发现当前的值已经计算过了, 则直接返回结果
        } else if (mem.containsKey(n)) {
            return mem.get(n);
        } else {
            int max = 0;
            for (int i = 1; i < n; i++) {
                max = Math.max(max, Math.max(cuttingRope(n - i) * i, (n - i) * i));
            }
            // 在一轮递归完成时保存结果
            mem.put(n,max);
            return max;
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().cuttingRope(31));
    }
}
```

### 动态规划

```java
public int cuttingRope(int n) {
    int[] dp = new int[n + 1];

    dp[1] = 1;
    for (int i = 2; i < n + 1; i++) {
        for (int j = 0; j < i; j++) {
            dp[i] = Math.max(dp[i], Math.max((i - j) * j, j * dp[i - j]));
        }
    }
    return dp[n];
}
```

## 数字出现的次数

```java
class Solution {
    public int search(int[] nums, int target) {
        // 搜索右边界 right
        int i = 0, j = nums.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (nums[m] <= target) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        int right = i;

        // 搜索左边界 right
        i = 0;
        j = nums.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (nums[m] < target) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        int left = j;
        return right - left - 1;
    }
}
```

 ## 两数之和 返回下标

### 暴力

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (j != i && nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}  
```

## 旋转矩阵

### 转置

可以由转置矩阵获得旋转矩阵

```java
class Solution {
    public void rotate(int[][] matrix) {
        int len = matrix.length;
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        // 对每一行进行翻转
        for (int i = 0; i < len; i++) {

            // 开始翻转第 i 行
            for (int j = 0; j < len / 2; j++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[i][len - j - 1];
                matrix[i][len - j - 1] = t;
            }
        }

    }
}
```

### 非转置

```java
public void rotate(int[][] matrix) {
    int add;
    int temp;
    int pos1 = 0;
    int pos2 = matrix[0].length - 1;
    while (pos1 < pos2) {
        add = 0;
        while (add < pos2 - pos1) {
            temp = matrix[pos1][pos1 + add];
            matrix[pos1][pos1 + add] = matrix[pos2 - add][pos1];
            matrix[pos2 - add][pos1] = matrix[pos2][pos2 - add];
            matrix[pos2][pos2 - add] = matrix[pos1 + add][pos2];
            matrix[pos1 + add][pos2] = temp;
            add++;
        }
        pos1++;
        pos2--;


    }
}
```

## 旋转数组的最小数字

找道旋转数组的最小数字

### 遍历

```java
class Solution {
    public int minArray(int[] numbers) {
        int min = Integer.MAX_VALUE;
        for (int number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }
}
```

### 二分

```java
class Solution {
    public int minArray(int[] numbers) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (numbers[mid] > numbers[right]) {
                left = mid + 1;
            } else if (numbers[mid] < numbers[right]) {
                right = mid;
            } else {
                right--;
            }
        }
        return numbers[left];
    }
}
```

## 顺时针打印矩阵

```java
class Solution {
    public int[] spiralOrder(int[][] matrix) {
        if (matrix.length == 0) {
            return new int[0];
        }
        int left = 0, right = matrix[0].length - 1, top = 0, bottom = matrix.length - 1, idx = 0;
        int[] res = new int[(right + 1) * (bottom + 1)];

        while (left <= right && top <= bottom) {
            for (int i = left; i <= right; i++) {
                res[idx++] = matrix[top][i];
            }
            if (++top > bottom) {
                break;
            }
            for (int i = top; i <= bottom; i++) {
                res[idx++] = matrix[i][right];
            }
            if (left > --right) {
                break;
            }
            for (int i = right; i >= left; i--) {
                res[idx++] = matrix[bottom][i];
            }
            if (top > --bottom) {
                break;
            }
            for (int i = bottom; i >= top; i--) {
                res[idx++] = matrix[i][left];
            }
            if (++left > right) {
                break;
            }
        }
        return res;
    }
}
```

## 只出现一次的数字

```java
class Solution {
    public int singleNumber(int[] nums) {
        int[] counts = new int[32];
        for (int num : nums) {
            for (int j = 0; j < 32; j++) {
                counts[j] += num & 1;
                num >>>= 1;
            }
        }
        int res = 0, m = 3;
        for (int i = 0; i < 32; i++) {
            res <<= 1;
            res |= counts[31 - i] % m;
        }
        return res;
    }
}
```