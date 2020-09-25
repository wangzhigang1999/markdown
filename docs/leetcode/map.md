# 图

## 克隆图

```Java
class Solution {

    Map<Node, Node> confirmed = new HashMap<>();
    public Node cloneGraph(Node node) {
        if (node == null) {
            return null;
        }
        if (confirmed.containsKey(node)) {
            return confirmed.get(node);
        }
        Node newNode = new Node(node.val, new ArrayList<>());
        confirmed.put(node, newNode);

        for (Node n : node.neighbors) {
            Node cloneGraph = cloneGraph(n);
            newNode.neighbors.add(cloneGraph);
        }
        return newNode;
    }
}
```

## 钥匙和房间

### 广度遍历方式

```java
class Solution {

    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        HashSet<Integer> visited = new HashSet<>();

        Queue<Integer> toBeVisited = new LinkedList<>();

        toBeVisited.add(0);

        while (!toBeVisited.isEmpty()){
            Integer current = toBeVisited.poll();

            if (!visited.contains(current)) {
                List<Integer> list = rooms.get(current);
                visited.add(current);
                toBeVisited.addAll(list);
            }
        }
        return visited.size()==rooms.size();
    }
}
```

### 深度遍历方式

```java
class Solution {
    boolean[] visited;
    int cnt;

    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        cnt = 0;
        visited = new boolean[n];

        dfs(rooms, 0);

        return cnt == n;
    }

    public void dfs(List<List<Integer>> rooms, int x) {
        visited[x] = true;
        cnt++;
        for (int it : rooms.get(x)) {
            if (!visited[it]) {
                dfs(rooms, it);
            }
        }
    }
}
```