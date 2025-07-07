package javase.collection_study.tree;

// TreeNode 类表示二叉树中的一个节点
class TreeNode {
    int val;            // 节点的值
    TreeNode left;      // 左子节点
    TreeNode right;     // 右子节点

    // 构造函数
    TreeNode(int x) {
        val = x;
    }
}

public class BinaryTreeExample {
    // 二叉树的根节点
    private TreeNode root;

    // 构造函数
    public BinaryTreeExample() {
        root = null;
    }

    // 插入节点
    public void insert(int value) {
        root = insertRec(root, value);
    }

    // 递归方法插入节点
    private TreeNode insertRec(TreeNode root, int value) {
        // 如果树为空，则创建一个新节点作为根节点
        if (root == null) {
            root = new TreeNode(value);
            return root;
        }

        // 否则，向左或向右递归地插入节点
        if (value < root.val) {
            root.left = insertRec(root.left, value);
        } else if (value > root.val) {
            root.right = insertRec(root.right, value);
        }

        // 返回根节点
        return root;
    }

    // 先序遍历二叉树
    public void firstorderTraversal(TreeNode root) {
        if (root != null) {
            System.out.print(root.val + " ");
            inorderTraversal(root.left);
            inorderTraversal(root.right);
        }
    }
    // 中序遍历二叉树
    public void inorderTraversal(TreeNode root) {
        if (root != null) {
            inorderTraversal(root.left);
            System.out.print(root.val + " ");
            inorderTraversal(root.right);
        }
    }
    // 后序遍历二叉树
    public void postorderTraversal(TreeNode root) {
        if (root != null) {
            inorderTraversal(root.left);
            inorderTraversal(root.right);
            System.out.print(root.val + " ");
        }
    }

    public static void main(String[] args) {
        BinaryTreeExample tree = new BinaryTreeExample();

        // 插入节点
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        // 先序遍历二叉树
        System.out.print("二叉树的先序遍历结果：");
        tree.firstorderTraversal(tree.root);
        System.out.println();
        // 中序遍历二叉树
        System.out.print("二叉树的中序遍历结果：");
        tree.inorderTraversal(tree.root);
        System.out.println();
        // 后序遍历二叉树
        System.out.print("二叉树的后序遍历结果：");
        tree.postorderTraversal(tree.root);
        System.out.println();
    }
}
/*
————————————————

版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。

原文链接：https://blog.csdn.net/wwwwwmmn/article/details/136608330*/
