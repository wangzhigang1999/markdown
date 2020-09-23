# 回溯算法

## 字符串的全排列

### 朴素回溯

```java
import java.util.Arrays;
import java.util.HashSet;

class Solution {
    HashSet<String> res = new HashSet<>();
    StringBuilder str = new StringBuilder();

    public String[] permutation(String s) {
        boolean[] visited = new boolean[s.length()];
        find(s, visited);
        return res.toArray(String[]::new);
    }

    private void find(String s, boolean[] visited) {
        if (str.length() == s.length()) {
            res.add(new String(str));
        } else {
            // 因为是全排列 , 因此每一轮都要从头开始进行遍历
            for (int i = 0; i < s.length(); i++) {

                if (visited[i]) {
                    continue;
                }
                str.append(s.charAt(i));
                visited[i] = true;
                find(s, visited);
                str.deleteCharAt(str.length() - 1);
                visited[i] = false;
            }
        }
    }

}
```

### 剪枝优化

```java
class Solution {
    List<String> res = new ArrayList<>();
    StringBuilder str = new StringBuilder();

    public String[] permutation(String S) {
        char[] chars = S.toCharArray();
        Arrays.sort(chars);
        boolean[] used = new boolean[chars.length];
        bachtrack(chars, used);
        return res.toArray(String[]::new);
    }

    private void bachtrack(char[] chars, boolean[] used) {
        if (str.length() == chars.length) {
            res.add(new String(str));
        } else {
            for (int i = 0; i < chars.length; i++) {
                if (i > 0 && chars[i] == chars[i - 1] && !used[i - 1]) {
                    continue;
                }
                if (!used[i]) {
                    str.append(chars[i]);
                    used[i] = true;
                    bachtrack(chars, used);
                    str.deleteCharAt(str.length() - 1);
                    used[i] = false;
                }
            }
        }
    }
}
```

## 无重复字符串的全排列

与上边的字符串的全排列类似,只是不需要使用 hashset 去重,也不需要剪枝优化.可以直接使用上边的代码

```java
import java.util.LinkedList;
import java.util.List;

class Solution {
    List<String> res = new LinkedList<>();

    StringBuilder str = new StringBuilder();

    public String[] permutation(String S) {
        char[] array = S.toCharArray();
        boolean[] used = new boolean[array.length];
        back(array, used);

        return res.toArray(String[]::new);
    }

    private void back(char[] array, boolean[] used) {
        if (str.length() == array.length) {
            res.add(new String(str));
        } else {
            for (int i = 0; i < array.length; i++) {
                if (!used[i]) {
                    str.append(array[i]);
                    used[i] = true;

                    back(array, used);

                    str.deleteCharAt(str.length() - 1);
                    used[i] = false;
                }
            }
        }

    }
}
```

## 全排列

```java
class Solution {
    List<List<Integer>> res = new ArrayList<>();

    List<Integer> cur = new ArrayList<>();

    public List<List<Integer>> permute(int[] nums) {
        boolean[] used = new boolean[nums.length];

        backtrack(nums, used);

        return  (res);
    }

    private void backtrack(int[] nums, boolean[] used) {
        if (cur.size() == nums.length) {
            res.add(new ArrayList<>(cur));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (!used[i]) {
                    cur.add(nums[i]);
                    used[i] = true;

                    backtrack(nums, used);

                    cur.remove(cur.size() - 1);
                    used[i] = false;
                }
            }
        }
    }
}
```



## 全排列II

### 朴素思想

与全排列I不同的地方在于数组中有重复元素,因此需要进行去重操作,可以使用hashset

```java
class Solution {
    HashSet<List<Integer>> res = new HashSet<>();

    List<Integer> cur = new LinkedList<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
        boolean[] used = new boolean[nums.length];

        backtrack(nums, used);

        return new LinkedList<>(res);
    }

    private void backtrack(int[] nums, boolean[] used) {
        if (cur.size() == nums.length) {
            res.add(new LinkedList<>(cur));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (!used[i]) {
                    cur.add(nums[i]);
                    used[i] = true;

                    backtrack(nums, used);

                    cur.remove(cur.size() - 1);
                    used[i] = false;
                }
            }
        }
    }
}
```

### 剪枝优化

使用 ArrayList 可以极大地提高速度

```java
class Solution {
    List<List<Integer>> res = new ArrayList<>();

    List<Integer> cur = new ArrayList<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
        boolean[] used = new boolean[nums.length];
        // 排序是必须的操作
        Arrays.sort(nums);
        backtrack(nums, used);
        return (res);
    }

    private void backtrack(int[] nums, boolean[] used) {
        if (cur.size() == nums.length) {
            res.add(new ArrayList<>(cur));
        } else {
            for (int i = 0; i < nums.length; i++) {
				// 精妙的剪枝之处, 需要先将数组进行排序,然后直接从递归树的最顶层剪枝
                if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                    continue;
                }
                
                if (!used[i]) {
                    cur.add(nums[i]);
                    used[i] = true;
                    backtrack(nums, used);
                    cur.remove(cur.size() - 1);
                    used[i] = false;
                }
            }
        }
    }
}
```

## 和为 sum 的路径

```java
public class Solution {

    LinkedList<List<Integer>> res = new LinkedList<>();

    LinkedList<Integer> nodes = new LinkedList<>();

    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        if (root != null) {

            nodes.add(root.val);

            if (nodes.stream().mapToInt(Integer::intValue).sum() == sum && root.left == null && root.right == null) {
                res.add(new LinkedList<>(nodes));
            }

            pathSum(root.left, sum);
            pathSum(root.right, sum);

            nodes.removeLast();
        }
        return res;
    }
}
```

## 所有有效的括号

### 通解

```java
// 暴力法, 当包含的括号不止一种时有用, 可以看做通解
List<String> res = new LinkedList<>();
StringBuilder sub = new StringBuilder();


public List<String> generateParenthesis(int n) {
    char[] ch = new char[2 * n];

    for (int i = 0; i < n; i++) {
        ch[i] = '(';
        ch[i + n] = ')';
    }

    boolean[] used = new boolean[2 * n];

    backtrack(ch, used, 2 * n);
    return res;
}

void backtrack(char[] ch, boolean[] used, int len) {
    if (sub.length() == len) {
        if (valid(sub)) {
            res.add(sub.toString());
            return;
        }

    }
    for (int i = 0; i < len; i++) {
        if (used[i]) {
            continue;
        }
        // 剪枝
        if (i > 0 && ch[i] == ch[i - 1] && !used[i - 1]) {
            continue;
        }
        used[i] = true;
        sub.append(ch[i]);

        backtrack(ch, used, len);

        used[i] = false;
        sub.deleteCharAt(sub.length() - 1);
    }
}

boolean valid(StringBuilder sub) {
    String s = sub.toString();
    char[] ch = s.toCharArray();
    int a = 0;
    for (char c : ch) {
        if (c == '(') {
            a += 1;
        }
        if (c == ')') {
            a -= 1;
        }
        if (a < 0) {
            return false;
        }
    }
    return a == 0;
}
```

### 优化

```java
int n;
ArrayList<String> res = new ArrayList<>();
StringBuilder str = new StringBuilder();

public List<String> generateParenthesis(int n) {
    this.n = n;
    find(0, 0);
    return res;
}

private void find(int left, int right) {
    // 递归的终止条件
    if (left == n && right == left) {
        res.add(str.toString());
    } else {
        if (left + 1 <= n) {
            str .append("(");
            find(left + 1, right);
            str.deleteCharAt(str.length()-1);
        }
        if (right + 1 <= left) {
            str .append(")");
            find(left, right + 1);
            str.deleteCharAt(str.length()-1);
        }
    }
}
```

### 常规思路

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    int n;
    ArrayList<String> res = new ArrayList<>();

    StringBuffer str = new StringBuffer();

    public List<String> generateParenthesis(int n) {
        this.n = n;
        backtrack(0, 0);
        return res;
    }

    private void backtrack(int left, int right) {
        if (left == n && right == left) {
            res.add(str.toString());
            return;
        }
        if (left + 1 <= n) {
            str.append("(");
            backtrack(left + 1, right);
            str.deleteCharAt(str.length() - 1);
        }
        if (right + 1 <= left) {
            str.append(")");
            backtrack(left, right + 1);
            str.deleteCharAt(str.length() - 1);

        }
    }
}
```

## 幂集

这道题有一个前提条件就是集合中不包含重复的数字

### 回溯法

```java
List<List<Integer>> list = new LinkedList<>();

List<Integer> cur = new ArrayList<>();


public List<List<Integer>> subsets(int[] nums) {

    backtrack( nums, 0);

    return list;
}

public void backtrack( int[] nums, int start) {
    list.add(new ArrayList<>(cur));

    for (int i = start; i < nums.length; i++) {
        cur.add(nums[i]);

        backtrack( nums, i + 1);

        cur.remove(cur.size() - 1);
    }
}
```

### 魔改动态规划

每次向之前的所有集合中添加一个字符

```java
public List<List<Integer>> subsets(int[] nums) {
    var res = new LinkedList<List<Integer>>();
    res.add(new LinkedList<>());

    for (int num : nums) {
        int size = res.size();
        for (int i = 0; i < size; i++) {
            List<Integer> curr = new LinkedList<>(res.get(i));
            curr.add(num);
            res.add(curr);
        }
    }
    return res;
}
```

## 拨号盘

这道题和朴素的回溯是完全相同的, 只是有一些变种

```java
class Solution {

    LinkedList<String> res = new LinkedList<>();
    StringBuilder buffer = new StringBuilder();
    String digit;
    int len ;
    HashMap<Character, String> map = new HashMap<>();

    {
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");
    }

    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return res;
        }
        len = digits.length();
        this.digit = digits;

        trackBack(0);

        return res;
    }

    private void trackBack(int index) {
        if (buffer.length() == len) {
            res.add(new String(buffer));
            return;
        }

        char c = digit.charAt(index);
        String s = map.get(c);
        // 回溯的具体实现
        for (int i = 0; i < s.length(); i++) {
            buffer.append(s.charAt(i));
            trackBack(index + 1);
            buffer.deleteCharAt(buffer.length() - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().letterCombinations("23"));
    }
}
```

## 分割回文串

```java
class Solution {
    List<List<String>> res = new LinkedList<>();

    List<String> path = new LinkedList<>();

    int len =0;

    public List<List<String>> partition(String s) {
        if (s == null || s.length() == 0) {
            return res;
        }
        len =s.length();

        backTrack(s);

        return res;
    }

    private void backTrack(String s) {
        if (checkPath(path)) {
            res.add(new LinkedList<>(path));
        }

        for (int i = 1; i <= s.length(); i++) {
            if (isPalindrome(s.substring(0, i))) {

                path.add(s.substring(0, i));

                backTrack(s.substring(i));

                path.remove(path.size() - 1);
            }
        }

    }

    private boolean isPalindrome(String s) {
        int l = 0;
        int r = s.length() - 1;
        while (l <= r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    private boolean checkPath(List<String> path) {
        int cnt = 0;
        for (String s : path) {
            cnt += s.length();
        }
        return cnt == len;
    }

    public static void main(String[] args) {
//        System.out.println(new Solution().isPalindrome("1"));
        System.out.println(new Solution().partition("aaa"));
    }
}
```

## 数组是否包含字符串

```java
class Solution {
    boolean[][] visited;

    public boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray();
        visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfs(board, words, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean dfs(char[][] board, char[] word, int i, int j, int k) {
        if (i >= board.length || i < 0 || j >= board[0].length || j < 0 || board[i][j] != word[k] || visited[i][j]) {
            return false;
        }
        if (k == word.length - 1) {
            return true;
        }

        visited[i][j]=true;
        boolean res = dfs(board, word, i + 1, j, k + 1)
                || dfs(board, word, i - 1, j, k + 1)
                || dfs(board, word, i, j + 1, k + 1)
                || dfs(board, word, i, j - 1, k + 1);
        visited[i][j]=false;
        return res;
    }
}
```

## 第k个排列

```java
class Solution {
    PriorityQueue<String> res = new PriorityQueue<>();

    StringBuilder str = new StringBuilder();

    public String getPermutation(int n, int k) {
        boolean[] visited = new boolean[n + 1];
        backtrack(n, k, visited);
        return res.toArray(String[]::new)[res.size()-1];
    }

    private void backtrack(int n, int k, boolean[] visited) {
        if (str.length() == n) {
            res.add(str.toString());
        }else {
            if(res.size()<k){
                for (int i = 1; i <= n; i++) {
                    if(!visited[i]){
                        str.append(i);
                        visited[i]=true;
                        backtrack(n, k, visited);
                        str.deleteCharAt(str.length()-1);
                        visited[i]=false;

                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().getPermutation(4,9));
    }
}
```

## 和为一个值的组合总数

```java
import java.util.*;

class Solution {
    List<List<Integer>> res = new LinkedList<>();
    List<Integer> current = new ArrayList<>();

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int candidate : candidates) {
            if (candidate <= target) {
                list.add(candidate);
            }
        }

        backtrack(list, 0,target);


        return res;
    }

    private void backtrack(LinkedList<Integer> list, int i, int target) {
        if (current.stream().mapToInt(Integer::intValue).sum() == target) {
            res.add(new ArrayList<>(current));
        } else if (current.stream().mapToInt(Integer::intValue).sum() < target) {
            for (int j = i; j < list.size(); j++) {
                if(current.stream().mapToInt(Integer::intValue).sum() > target){
                    break;
                }
                current.add(list.get(j));
                backtrack(list, j, target);
                current.remove(current.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().combinationSum(new int[]{1, 2}, 4));
    }
}
```

## 组合

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Solution {
    List<List<Integer>> res = new LinkedList<>();

    List<Integer> curr = new LinkedList<>();

    public List<List<Integer>> combine(int n, int k) {

        backtrack(n, 1, k);

        return res;

    }

    private void backtrack(int n, int start, int k) {
        if (curr.size() == k) {
            res.add(new ArrayList<>(curr));
        } else {
            for (int i = start; i <= n; i++) {
                curr.add(i);
                backtrack(n, i + 1, k);
                curr.remove(curr.size() - 1);
            }
        }
    }
}
```

## 组合总数

```java
// 非常常规的回溯法,但是要注意剪枝以及使用 ArrayList
class Solution {

    HashSet<List<Integer>> res = new HashSet<>();

    List<Integer> current = new LinkedList<>();

    int sum = 0;

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {

        Arrays.sort(candidates);

        ArrayList<Integer> list = new ArrayList<>();

        for (int candidate : candidates) {
            if (candidate <= target) {
                list.add(candidate);
            } else {
                break;
            }
        }

        backtrack(list, target, 0);

        return new ArrayList<>(res);
    }

    private void backtrack(ArrayList<Integer> list, int target, int start) {
        if (sum == target) {
            ArrayList<Integer> tmp = new ArrayList<>(current);
            tmp.sort(Integer::compareTo);
            res.add(tmp);
        } else {
            for (int i = start; i < list.size(); i++) {
                if (sum + list.get(i) <= target) {
                    current.add(list.get(i));
                    sum += list.get(i);

                    backtrack(list, target, i + 1);

                    sum -= list.get(i);
                    current.remove(current.size() - 1);
                }

            }
        }
    }

}
```