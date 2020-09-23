# 双指针

## 合并有序数组

```java
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int[] res = new int[m + n];
    int mIndex = 0, nIndex = 0;
    for (int i = 0; i < res.length; i++) {
        if (mIndex == m ) {
            res[i] = nums2[nIndex++];
        } else if (nIndex == n) {
            res[i] = nums1[mIndex++];
        } else {
            if (nums1[mIndex] >= nums2[nIndex]) {
                res[i] = nums2[nIndex++];
            } else {
                res[i] = nums1[mIndex++];
            }
        }
    }
    System.arraycopy(res, 0, nums1, 0, nums1.length);
}
```

## 二维数组的查找

利用数组的特点进行查找, 从一个角落进行查找

```java
public boolean findNumberIn2DArray(int[][] matrix, int target) {
    if (matrix.length == 0 || matrix[0].length == 0) {
        return false;
    }
    int i = 0, j = matrix[0].length - 1;
    int row = matrix.length;
    while (i < row && j >= 0) {
        if (matrix[i][j] == target) {
            return true;
        } else if (matrix[i][j] > target) {
            j--;
        } else {
            i++;
        }
    }
    return false;
}
```

## 回文字符串

```java
public boolean isPalindrome(String s) {
    if (s == null || s.length() == 0) {
        return true;
    }
    char[] array = s.toLowerCase().toCharArray();

    int left = 0;
    int right = array.length - 1;
    while (left <= right) {
        if (!valid(array, left)) {
            left++;
        } else if (!valid(array, right)) {
            right--;
        } else {
            if (array[left] != array[right]) {
                return false;
            }
            left++;
            right--;
        }
    }

    return true;
}

private boolean valid(char[] array, int index) {
    char c = array[index];
    if (c >= '0' && c <= '9') {
        return true;
    } else {
        return c >= 'a' && c <= 'z';
    }
}
```

## 两数之和排序版本

```java
public int[] twoSum(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        if (nums[left] + nums[right] == target) {
            return new int[]{nums[left], nums[right]};
        }else if(nums[left] + nums[right] < target){
            left++;
        }else {
            right--;
        }
    }
    return new int[]{nums[left], nums[right]};
}
```

## 和为S的连续序列

```java
public int[][] findContinuousSequence(int target) {
    int left = 1;
    int right = 2;
    int sum = left + right;
    LinkedList<LinkedList<Integer>> listRes = new LinkedList<>();
    while (right <= (target / 2 + 1) && (left < right)) {
        if (sum == target) {
            LinkedList<Integer> list = new LinkedList<>();
            for (int i = left; i <= right; i++) {
                list.add(i);
            }
            listRes.add(list);
            sum -= (left++);
            sum += (++right);

        } else if (sum > target) {
            sum -= left++;
        } else {
            sum += (++right);
        }

    }
    int[][] res = new int[listRes.size()][];

    for (int i = 0; i < listRes.size(); i++) {
            res[i] = listRes.get(i).stream().mapToInt(Integer::intValue).toArray();
    }
    return res;
}
```

## 调整顺序使得奇偶分开

```java
import java.util.Arrays;

// 这道题并不要求保持原有的顺序
class Solution {
    public int[] exchange(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            while (nums[left] % 2 != 0 && left < right) {
                left++;
            }
            while (nums[right] % 2 == 0 && left < right) {
                right--;
            }
            if (left < right) {
                int num = nums[left];
                nums[left] = nums[right];
                nums[right] = num;
            }
        }
        return nums;
    }
}
```

## 盛水最多的容器 双指针

```java
class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int res = 0;
        int tmp = 0;
        while (left < right) {
            tmp = (right - left) * Math.min(height[left], height[right]);
            if (tmp > res) {
                res = tmp;
            }
            if (height[left] > height[right]) {
                right--;
            } else {
                left++;
            }

        }
        return res;

    }
}
```