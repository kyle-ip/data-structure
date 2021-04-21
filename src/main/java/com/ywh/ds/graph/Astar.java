package com.ywh.ds.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author ywh
 * @since 4/21/2021
 */
public class Astar {

    /**
     * 顶点个数
     */
    private int v;

    /**
     * 邻接表
     */
    private LinkedList<Edge>[] adj;

    /**
     * 顶点列表
     */
    private Vertex[] vertexes;

    public Astar(int v) {
        this.v = v;
        this.vertexes = new Vertex[v];
        this.adj = new LinkedList[v];
    }

    /**
     * @param id
     * @param x
     * @param y
     */
    public void addVetex(int id, int x, int y) {
        vertexes[id] = new Vertex(id, x, y);
    }

    /**
     *
     * @param sid
     * @param tid
     * @param w
     */
    public void addEdge(int sid, int tid, int w) {
        this.adj[sid].add(new Edge(sid, tid, w));
    }

    private static class Vertex {

        /**
         * 顶点编号 id
         */
        public int id;

        /**
         * 从起始顶点到该顶点的距离
         */
        public int dist;

        /**
         * 曼哈顿距离，f(i)=g(i)+h(i)
         */
        public int f;

        /**
         * 横纵坐标
         */
        public int x, y;

        public Vertex(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.f = Integer.MAX_VALUE;
            this.dist = Integer.MAX_VALUE;
        }
    }

    /**
     *
     */
    private static class Edge {

        /**
         * 边的起始顶点编号
         */
        public int sid;

        /**
         * 边的终止顶点编号
         */
        public int tid;

        /**
         * 权重
         */
        public int w;

        /**
         * @param sid
         * @param tid
         * @param w
         */
        public Edge(int sid, int tid, int w) {
            this.sid = sid;
            this.tid = tid;
            this.w = w;
        }
    }

    /**
     * 从点 s 到 点 t 的路径
     *
     * @param s
     * @param t
     */
    public List<Integer> astar(int s, int t) {
        // 用来还原路径
        int[] predecessor = new int[this.v];
        // 按照vertex的f值构建的小顶堆，而不是按照dist
        PriorityQueue<Vertex> queue = new PriorityQueue<>(this.v);

        // 标记是否进入过队列
        boolean[] inqueue = new boolean[this.v];
        vertexes[s].dist = vertexes[s].f = 0;
        queue.add(vertexes[s]);
        inqueue[s] = true;

        while (!queue.isEmpty()) {
            // 取堆顶元素并删除
            Vertex minVertex = queue.poll();
            for (int i = 0; i < adj[minVertex.id].size(); ++i) {
                // 取出一条 minVertex 相连的边
                Edge e = adj[minVertex.id].get(i);

                // minVertex --> nextVertex
                Vertex nextVertex = vertexes[e.tid];

                // 更新 next 的 dist、f
                if (minVertex.dist + e.w < nextVertex.dist) {
                    nextVertex.dist = minVertex.dist + e.w;
                    nextVertex.f = nextVertex.dist + hManhattan(nextVertex, vertexes[t]);
                    predecessor[nextVertex.id] = minVertex.id;
                    if (inqueue[nextVertex.id]) {
                        // FIXME
                        // queue.update(nextVertex);
                    } else {
                        queue.add(nextVertex);
                        inqueue[nextVertex.id] = true;
                    }
                }
                // 只要到达t就可以结束while了
                if (nextVertex.id == t) {
                    // 清空queue，才能推出while循环
                    queue.clear();
                    break;
                }
            }
        }
        return path(s, t, predecessor, new ArrayList<>());
    }


    /**
     * 曼哈顿距离
     *
     * @param v1
     * @param v2
     * @return
     */
    private int hManhattan(Vertex v1, Vertex v2) {
        return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
    }

    /**
     * @param s
     * @param t
     * @param predecessor
     * @param ret
     * @return
     */
    private List<Integer> path(int s, int t, int[] predecessor, List<Integer> ret) {
        if (s != t) {
            path(s, predecessor[t], predecessor, ret);
            ret.add(t);
        }
        return ret;
    }
}
