# 二叉树

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