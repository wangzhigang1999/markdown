# 二叉树

## 先序遍历

### 递归

- 可以从左侧递归,也可以从右侧递归

```java
public static void pre(TreeNode root){
    if (root != null) {
        System.out.println(root.val);
        pre(root.left);
        pre(root.right);
    }
}
```

### 迭代

```Java
public static void preA(TreeNode root) {
    Deque<TreeNode> deque = new ArrayDeque<>();
    deque.addFirst(root);

    while (!deque.isEmpty()) {
        TreeNode pop = deque.pop();
        doSomething(pop);
        if (pop.right != null) {
            deque.addFirst(pop.right);
        }

        if (pop.left != null) {
            deque.addFirst(pop.left);
        }

    }
}
```

## 中序遍历

### 递归

```java
public List<Integer> inorderTraversal(TreeNode root) {
    if (root != null) {
        inorderTraversal(root.left);
        res.add(root.val);
        inorderTraversal(root.right);
    }
    return res;
}
```

### 迭代

```java
public List<Integer> inorderTraversal(TreeNode root) {
    if (root == null) {
        return res;
    }
    Stack<TreeNode> nodes = new Stack<>();
    while (!nodes.isEmpty() || root != null) {
        while (root != null) {
            nodes.push(root);
            root = root.left;
        }
        root=nodes.pop();
        res.add(root.val);
        root = root.right;
    }

    return res;
}
```

### Morris

这是一个神奇的算法,不使用额外空间,但是会破坏原来的树的结构.

假设当前遍历到的节点为 xx，将 xx 的左子树中最右边的节点的右孩子指向 xx，这样在左子树遍历完成后我们通过这个指

向走回了 xx，且能通过这个指向知晓我们已经遍历完成了左子树，而不用再通过栈来维护，省去了栈的空间复杂度。



## 深度

```java
public int maxDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    if (root.left == null && root.right == null) {
        return 1;
    } else if (root.left != null && root.right == null) {
        return 1 + maxDepth(root.left);
    } else if (root.left == null) {
        return 1 + maxDepth(root.right);
    } else {
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
}
```

## 镜像二叉树

### 创建

```java
public TreeNode mirrorTree(TreeNode root) {
    if (root == null) {
        return null;
    }
    TreeNode node = new TreeNode(root.val);
    node.left=mirrorTree(root.right);
    node.right=mirrorTree(root.left);
    return node;
}
```

### 判断

```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        } else {
            return is(root.left, root.right);
        }
    }

    private boolean is(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        } else if (left != null && right != null) {
            return left.val == right.val && is(left.left, right.right) && is(left.right, right.left);
        } else {
            return false;
        }
    }
}
```

## 第K大

### 朴素

- 先获得所有的数字
- 然后排序
- 返回需要的数字

```java
public int kthLargest(TreeNode root, int k) {
    Queue<TreeNode> queue = new LinkedList<>();
    if (root != null) {
        queue.add(root);
    }
    LinkedList<Integer> list = new LinkedList<>();
    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        list.add(node.val);
        if (node.left != null) {
            queue.add(node.left);
        }
        if (node.right != null) {
            queue.add(node.right);
        }
    }
    list.sort(Integer::compareTo);

    return list.get(list.size() - k);
}
```

### 中序遍历

中序遍历获得所有的节点的值,然后返回即可.

```java
class Solution {
    static LinkedList<Integer> linkedList = new LinkedList<>();

    public int kthLargest(TreeNode root, int k) {
        fill(root);
        return linkedList.get(linkedList.size() - k);
    }

    private void fill(TreeNode root) {
        if (root != null) {
            fill(root.left);
            linkedList.add(root.val);
            fill(root.right);
        }
    }
}
```

### 无需额外空间

```java
class Solution {
    int res, k;

    public int kthLargest(TreeNode root, int k) {
        this.k = k;
        find(root);
        return res;
    }

    void find(TreeNode root) {
        if (root == null) {
            return;
        }
        find(root.right);

        if (k == 0) {
            return;
        }
        k--;
        if (k == 0) {
            res = root.val;
        }
        find(root.left);
    }
}
```

### O(1) 复杂度的另一个实现

```java
class Solution {

    int cnt = 0;
    int res = 0;

    public int kthSmallest(TreeNode root, int k) {
        if (root == null) {
            return 0;
        }
        kthSmallest(root.left, k);
        cnt++;
        if (cnt == k) {
            res = root.val;
        }
        kthSmallest(root.right, k);
        return res;
    }

}
```

## 层序打印

```java
public List<List<Integer>> levelOrder(TreeNode root) {
    Queue<TreeNode> queue = new LinkedList<>();
    List<List<Integer>> res = new ArrayList<>();
    if (root != null) {
        queue.add(root);
    }
    while (!queue.isEmpty()) {
        List<Integer> tmp = new ArrayList<>();
        for (int i = queue.size(); i > 0; i--) {
            TreeNode node = queue.poll();
            assert node != null;
            tmp.add(node.val);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        res.add(tmp);
    }
    return res;
}
```

## 二叉树的最近公共祖先

```java
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) {
        return root;
    }
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left == null) {
        return right;
    }
    if (right == null) {
        return left;
    }
    return root;
}
```

## 二叉搜索树的最近公共祖先

```java
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    while (root != null) {
        /// p,q 都在 root 的右子树中
        if (root.val < p.val && root.val < q.val) {
            root = root.right; // 遍历至右子节点
            // p,q 都在 root 的左子树中
        } else if (root.val > p.val && root.val > q.val) {
            root = root.left;
            // 遍历至左子节点
        } else {
            break;
        }
    }
    return root;
}
```

## 是否平衡二叉树

### 递归

```java
public static int getDept(TreeNode root) {
    if (root == null) {
        return 1;
    } else {
        return Math.max(getDept(root.left), getDept(root.right)) + 1;
    }


}

public boolean isBalanced(TreeNode root) {
    if (root == null) {
        return true;
    } else if (Math.abs(getDept(root.left) - getDept(root.right)) > 1) {
        return false;
    }
        return isBalanced(root.left) && isBalanced(root.right);
}
```

## 对称二叉树

```java
public boolean isSymmetric(TreeNode root) {
    if (root == null) {
        return true;
    } else {
        return check(root.left, root.right);
    }

}

public boolean check(TreeNode p, TreeNode q) {
    if (p==null && q==null){
        return true;
    }else if (p==null||q==null || p.val!=q.val){
        return false;
    }
    return check(p.left,q.right)&&check(p.right,q.left);
}
```

## 反转二叉树

```Java
public TreeNode invertTree(TreeNode root) {
    if (root == null) {
        return null;
    } else {
        TreeNode left = root.left;
        root.left = root.right;
        root.right = left;

        invertTree(root.left);
        invertTree(root.right);
        
        return root;
    }
}
```

## 合并二叉树

```java
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {

    // 递归终止条件
    if (t1 == null) {
        return t2;
    } else if (t2 == null) {
        return t1;
    }
    // 选点一颗二叉树作为基础,将另一颗树合并上来
    t2.val+=t1.val;
    t2.left=mergeTrees(t1.left, t2.left);
    t2.right=mergeTrees(t1.right, t2.right);
    return t2;
}
```

## 累加二叉树

```java
 int sum = 0;

public TreeNode convertBST(TreeNode root) {

    if (root != null) {
        convertBST(root.right);
        sum = sum + root.val;
        root.val = sum;
        convertBST(root.left);
    }
    return root;
}
```

## 二叉树按顺序转链表

分析 : 其实就是按照先序遍历的顺序将节点连了起来

### 朴素思想

```java
import java.util.LinkedList;
// 按照先序遍历的顺序将节点排序,然后改一下指针
class Solution {
    LinkedList<TreeNode> list = new LinkedList<>();

    public void flatten(TreeNode root) {
        findAll(root);
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).left = null;
            list.get(i).right = list.get(i + 1);
        }
    }

    private void findAll(TreeNode root) {
        if (root != null) {
            list.add(root);
            findAll(root.left);
            findAll(root.right);
        }
    }
}
```

### 迭代

```Java
public void flatten(TreeNode root) {
    while (root != null) {
        //左子树为 null，直接考虑下一个节点
        if (root.left != null) {
            // 找左子树最右边的节点
            TreeNode tmp = root.left;
            while (tmp.right != null) {
                tmp = tmp.right;
            }
            //将原来的右子树接到左子树的最右边节点
            tmp.right = root.right;
            // 将左子树插入到右子树的地方
            root.right = root.left;
            root.left = null;
            // 考虑下一个节点
        }
        root = root.right;
    }
}
```

### 后序遍历+递归

后续遍历与先序遍历是相反的过程, 因此可以再每一层递归中保存上一个节点

```java
class Solution {
    TreeNode pre = null;

    public void flatten(TreeNode root) {
        if (root != null) {
            fla	tten(root.right);
            flatten(root.left);
            
            root.right = pre;
            root.left = null;
            pre = root;
        }

    }
}
```

## 二叉树按顺序转双向链表

```java
class Solution {
    Node head, pre;

    public Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }

        find(root);
        head.left=pre;
        pre.right=head;
        return head;
    }

    private void find(Node root) {
        if (root != null) {
            find(root.left);
            
            if (head == null) {
                head = root;
            } else {
                pre.right = root;
            }
            
            root.left = pre;
            pre = root;
            
            find(root.right);
        }
    }
}
```

## 二叉树的子树

```java
public boolean isSubStructure(TreeNode A, TreeNode B) {
    return (A != null && B != null) && (similar(A, B) || isSubStructure(A.left, B) || isSubStructure(A.right, B));
}
boolean similar(TreeNode A, TreeNode B) {
    if(B == null) {
        return true;
    }
    if(A == null || A.val != B.val) {
        return false;
    }
    return similar(A.left, B.left) && similar(A.right, B.right);
}
```

## 二叉树锯齿形遍历

```java
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> res = new LinkedList<>();
    if (root == null) {
        return res;
    }
    LinkedList<TreeNode> nodes = new LinkedList<>();
    nodes.add(root);
    boolean reverse = false;
    
    while (!nodes.isEmpty()) {
        reverse = !reverse;
        LinkedList<Integer> curr = new LinkedList<>();
        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            TreeNode node = nodes.pop();
            curr.add(node.val);
            if (node.left != null) {
                nodes.add(node.left);
            }
            if (node.right != null) {
                nodes.add(node.right);
            }
        }
        if (reverse) {
            res.add(curr);
        } else {
            LinkedList<Integer> list = new LinkedList<>();
            for (Integer integer : curr) {
                list.addFirst(integer);
            }
            res.add(list);
        }
    }

    return res;
}
```

## 恢复二叉树

```java
class Solution {
    HashMap<Integer, Integer> map = new HashMap<>();
    int[] pre;
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        pre = preorder;
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return recur(0, 0, inorder.length - 1);
    }

    TreeNode recur(int preRoot, int inLeft, int inRight) {
        if (inLeft > inRight) {
            return null;
        }

        TreeNode root = new TreeNode(pre[preRoot]);
        // index 保存了当前元素在中序遍历中的位置
        int index = map.get(pre[preRoot]);
        root.left = recur(preRoot + 1, inLeft, index - 1);
        root.right = recur(preRoot + index - inLeft + 1, index + 1, inRight);
        return root;
    }
}
```

## 二叉树深度最深一层的和

```java
import java.util.LinkedList;
import java.util.Queue;

class Solution {

    public int deepestLeavesSum(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        int res = 0;

        if (root == null) {
            return 0;
        }

        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            int sum = 0;

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.remove();
                sum += node.val;
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            res =sum;
        }
        return res;
    }
}
```

## 完全二叉树层级指针

### 使用队列

```java
class Solution {
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        LinkedList<Node> list = new LinkedList<>();
        list.add(root);

        while (!list.isEmpty()) {
            int size = list.size();
            for (int i = 0; i < size - 1; i++) {
                list.get(i).next = list.get(i + 1);
            }
            for (int i = 0; i < size; i++) {
                Node node = list.removeFirst();
                if (node.left != null) {
                    list.add(node.left);
                }
                if (node.right != null) {
                    list.add(node.right);
                }
            }
        }
        return root;
    }
}
```

### 递归

```java
class Solution {
    public Node connect(Node root) {
        if (root != null) {
            fun(root.left, root.right);
        }
        return root;

    }

    private void fun(Node left, Node right) {
        if (left != null && right != null) {
            left.next = right;
            fun(left.right, right.left);
            fun(left.left, left.right);
            fun(right.left, right.right);
        }
    }
}
```

## 累加二叉树

```java
class Solution {
    int sum = 0;

    public TreeNode convertBST(TreeNode root) {
        if (root == null) {
            return null;
        } else {
            convertBST(root.right);
            root.val += sum;
            sum = root.val;
            convertBST(root.left);
            return root;
        }
    }
}
```

## 二叉树的直径

### 对每个节点进行递归

```java
class Solution {
    int sum = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            diameterOfBinaryTree(root.left);

            int left = findHeight(root.left);
            int right = findHeight(root.right);
            sum = Math.max(sum, left + right);

            diameterOfBinaryTree(root.right);
        }
        return sum;
    }

    private int findHeight(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            return 1 + Math.max(findHeight(root.left), findHeight(root.right));
        }
    }
}
```

## 二叉树每一层的平均数

```java
// 常规思路,层序遍历 BFS
class Solution {
    public List<Double> averageOfLevels(TreeNode root) {

        LinkedList<Double> res = new LinkedList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();

            double sum = 0;
            for (int i = 0; i < size; i++) {
                TreeNode remove = queue.remove();
                sum += remove.val;
                if (remove.right != null) {
                    queue.add(remove.right);
                }
                if (remove.left != null) {
                    queue.add(remove.left);
                }
            }
            res.add(sum / size);
        }
        return res;
    }
}
```

## 二叉树的后序遍历序列

按照后续遍历的特点进行判断即可

```java
class Solution {
    public boolean verifyPostorder(int[] postorder) {
        return recur(postorder, 0, postorder.length - 1);
    }

    boolean recur(int[] postorder, int left, int right) {
        if (left >= right) {
            return true;
        }
        int p = left;
        int root = postorder[right];
        while (postorder[p] < root) {
            p++;
        }

        int m = p;
        while (postorder[p] > root) {
            p++;
        }
        return p == right && recur(postorder, left, m - 1) && recur(postorder, m, right - 1);
    }
}
```

## 单值二叉树

```java
class Solution {
    int val;

    public boolean isUnivalTree(TreeNode root) {
        if (root == null) {
            return true;
        } else {
            val = root.val;
        }
        return judge(root);
    }

    private boolean judge(TreeNode root) {
        if (root == null) {
            return true;
        } else {
            return root.val == val && judge(root.left) && judge(root.right);
        }
    }
}
```

## 二叉树的坡度

```java
class Solution {
    int sum = 0;
    public int findTilt(TreeNode root) {
        find(root);
        return sum;
    }
    private void find(TreeNode root) {
        if (root != null) {
            sum += (Math.abs(summ(root.right) - summ(root.left)));
            find(root.right);
            find(root.left);
        }
    }
    private int summ(TreeNode root) {
        return root == null ? 0 : root.val + summ(root.left) + summ(root.right);
    }
}
```

## 修剪二叉树  

神奇的思路,暂时还无法理解

```java
class Solution {
    public TreeNode trimBST(TreeNode root, int L, int R) {
        if (root == null) return null;
        
        if (root.val > R) return trimBST(root.left, L, R);
        if (root.val < L) return trimBST(root.right, L, R);

        root.left = trimBST(root.left, L, R);
        root.right = trimBST(root.right, L, R);
        return root;
    }
}
```

## 最大二叉树

```java
import java.util.Arrays;

class Solution {
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        int max = nums[0];
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
                maxIndex = i;
            }
        }
        TreeNode root = new TreeNode(max);

        root.left = constructMaximumBinaryTree(Arrays.copyOfRange(nums,0,maxIndex));
        if(nums.length >maxIndex+1){
            root.right = constructMaximumBinaryTree(Arrays.copyOfRange(nums,maxIndex+1,nums.length));
        }

        return root;

    }

    public static void main(String[] args) {
        TreeNode treeNode = new Solution().constructMaximumBinaryTree(new int[]{3,2,1,6,0,5});
        System.out.println(treeNode);
    }
}
```

## 二叉树剪枝

```java
class Solution {
    public TreeNode pruneTree(TreeNode root) {
        if (root == null) {
            return null;
        } else {
            if(!containsOne(root)){
                return null;
            }

            if (!containsOne(root.left)) {
                root.left = null;
            } else {
                pruneTree(root.left);
            }

            if (!containsOne(root.right)) {
                root.right = null;
            } else {
                pruneTree(root.right);
            }
        }
        return root;
    }

    private boolean containsOne(TreeNode root) {
        if (root == null) {
            return false;
        } else {
            return root.val == 1 || containsOne(root.right) || containsOne(root.left);
        }
    }
}
```

## 由中序和后序遍历还原二叉树

​	这道题与 `由先序和中序还原二叉树`的思路是完全一致的, 先序遍历和后序遍历都是为了找到根节点 `root`, 结合中序遍历可以将中序遍历划分为两部分,分别是根节点的左子树和右子树. 然后递归的进行创建即可.

```Java
class Solution {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        // 递归终止条件
        if (postorder.length == 0 || inorder.length == 0) {
            return null;
        }

        // 获取将要创建的根节点的值
        int val = postorder[postorder.length - 1];
        TreeNode root = new TreeNode(val);
		
        //找到根节点在中序遍历的位置,便于划分为两部分 
        int index;
        for (index = 0; index < inorder.length; index++) {
            if (inorder[index] == val) {
                break;
            }
        }
        // 递归向下搜索
        TreeNode left = buildTree(Arrays.copyOfRange(inorder, 0, index), Arrays.copyOfRange(postorder, 0, index));
        TreeNode right = buildTree(Arrays.copyOfRange(inorder, index + 1, inorder.length), Arrays.copyOfRange(postorder, index, postorder.length - 1));
        root.left = left;
        root.right = right;
        return root;
    }
}
```

## 二叉搜索树的插入

```java
class Solution {
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if(root==null){
            return new TreeNode(val);
        }else{
            if(root.val>val){
                if(root.left==null){
                    root.left = new TreeNode(val);
                }else{
                 insertIntoBST(root.left,val);
                }
            }else{
                if(root.right==null){
                    root.right= new TreeNode(val);
                }else{
                 insertIntoBST(root.right,val);
                }
            }
            return root;
        }
    }
}
```

