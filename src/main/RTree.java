package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RTree {
  public static final int LINEAR = 0;
  public static final int GREENE = 1;

  private static final int m = 10; // min children per rectangle
  private static final int M = 1000; // max children per rectangle

  public Node root;
  public int size; // number of elements in the tree
  public int type;

  public RTree(int t) throws Exception {
    if (t != 0 || t != 1) throw new IllegalArgumentException();
    root = null;
    size = 0;
    type = t;
  }

  public Rectangle[] search(Rectangle c) { 
    List<Rectangle> list = doSearch(c, root);
    Rectangle[] array = new Rectangle[list.size()];
    array = list.toArray(array);
    return array;
  }

  private List<Rectangle> doSearch(Rectangle c, Node currNode) {
    List<Rectangle> ans = new ArrayList<Rectangle>();
    if (currNode.isLeaf()) {
      // a leaf
      if (c.intersect(currNode.box)) {
        ans.add(currNode.box);
      }
    }
    else {
      // an internal node
      for (int i = 0; i < currNode.n; i++) {
        Node ch = currNode.children[i];
        if (c.intersect(ch.box)) {
          ans.addAll(doSearch(c, ch));
        }
      }
    }
    return ans;
  }

  public void insert(Rectangle c) {
    // caso size = 0
    if (root == null) {
      root = new Node(c, 0, 0); // a leaf
      root.isRoot = true;
    }

    // caso size = 1
    else if (root.isLeaf()) {
      Node aux = root;
      aux.isRoot = false;
      Rectangle r = Rectangle.computeMBR(root.box, c);
      root = new Node(r, 2, M);
      root.isRoot = true;
      root.children[0] = aux;
      root.children[1] = new Node(c, 0, 0);
    }

    // caso size > 1
    else {
      doInsert(c, root);
    }
    size++;
  }

  private void doInsert(Rectangle c, Node currNode) {
    if (currNode.children[0].isLeaf()) {
      // children are leafs
      currNode.children[currNode.n++] = new Node(c, 0, 0);
    }
    else {
      // children are internal nodes
      doInsert(c, getOptimalChild(c, currNode));
    }

    // handle overflow
    handleOverflow(currNode);

    // update MBR
    currNode.box = Rectangle.computeMBR(currNode.box, c);
  }

  private Node getOptimalChild(Rectangle c, Node node) {
    Random rnd = new Random();
    Node bestNode = node.children[0];
    Rectangle bestR = Rectangle.computeMBR(bestNode.box, c);
    float bestGrowth = bestR.area() - bestNode.box.area();

    for (int i = 0; i < node.n; i++) {
      boolean swap = false;
      Node ch = node.children[i];
      Rectangle r = Rectangle.computeMBR(ch.box, c);
      float growth = r.area() - ch.box.area();

      // min growth node
      if (growth < bestGrowth) {
        swap = true;
      }
      
      else if (growth == bestGrowth) {
        // min area
        if (ch.box.area() < bestNode.box.area()) {
          swap = true;
        }

        // random choice
        else if (ch.box.area() == bestNode.box.area() && 
            rnd.nextFloat() < 0.5){
          swap = true;
        }
      }

      // swap values 
      if (swap) {
        bestNode = ch;
        bestR = r;
        bestGrowth = bestR.area() - bestNode.box.area();
      }
    }
    return bestNode;
  }

  private void handleOverflow(Node node) {
    for (int i = 0; i < node.n; i++) {
      Node ch = node.children[i];
      if (ch.overflow()) {
        Node[] newN = split(ch);
        // reuse pointer of child
        ch.copy(newN[0]);
        // append new child
        node.children[node.n++] = newN[1];
      } 
    }

    if (node.isRoot && node.overflow()) {
      Node[] newN = split(root);
      Rectangle auxRect = Rectangle.computeMBR(newN[0].box, newN[1].box);
      root = new Node(auxRect, 2, M);
      root.isRoot = true;
      root.children[0] = newN[0];
      root.children[1] = newN[1];
    }
  }

  private Node[] split(Node node) {
    Node[] ans = new Node[2];
    switch (type) {
      case LINEAR: ans = linearSplit(node);
      case GREENE: ans = greeneSplit(node);
    }
    return ans;
  }

  private Node[] linearSplit(Node node) {
    // calculate more distant points
    int left = 0, down = 0, right = 0, up = 0;
    for (int i = 0; i < node.n; i++) {
      Node ch = node.children[i];
      if (ch.box.maxx < node.children[left].box.maxx) left = i;
      if (ch.box.maxy < node.children[down].box.maxy) down = i;
      if (ch.box.minx > node.children[right].box.minx) right = i;
      if (ch.box.miny > node.children[up].box.miny) up = i;
    }
    
    // calculate normalized distances
    float minX = node.children[left].box.maxx;
    float minY = node.children[down].box.maxy;
    float maxX = node.children[right].box.minx;
    float maxY = node.children[up].box.maxy;
    
    float distX = (maxX - minX)/node.box.width();
    float distY = (maxY - minY)/node.box.height();
    
    // split nodes
    Node[] nodes = new Node[2];
    int i1, i2;
    if (distX > distY) {
      i1 = left; i2 = right;
    }
    else {
      i1 = down; i2 = up;
    }
    nodes[0] = new Node(node.children[i1].box, m, M);
    nodes[1] = new Node(node.children[i2].box, m, M);
    nodes[0].children[node.n++] = node.children[i1];
    nodes[1].children[node.n++] = node.children[i2];
    
    for (int i = 0; i < node.n; i++) {
      if (i != i1 && i != i2) {
        Node ch = node.children[i];
        Rectangle r0 = Rectangle.computeMBR(nodes[0].box, ch.box);
        Rectangle r1 = Rectangle.computeMBR(nodes[1].box, ch.box);    
        float growth0 = r0.area() - nodes[0].box.area();
        float growth1 = r1.area() - nodes[1].box.area();
        if (growth0 < growth1) {
          nodes[0].children[node.n++] = ch;
          nodes[0].box = r0;
        }
        else {
          nodes[1].children[node.n++] = ch;
          nodes[1].box = r1;
        }
      }
    }
    // at least m children per node
    while (nodes[0].n < nodes[0].m) {
      nodes[0].children[nodes[0].n++] = nodes[1].children[nodes[1].n-1];
      nodes[1].children[nodes[1].n---1] = null; 
    }
    return nodes;
  }

  private Node[] greeneSplit(Node node) {
    // calculate more distant points
    int left = 0, down = 0, right = 0, up = 0;
    for (int i = 0; i < node.n; i++) {
      Node ch = node.children[i];
      if (ch.box.maxx < node.children[left].box.maxx) left = i;
      if (ch.box.maxy < node.children[down].box.maxy) down = i;
      if (ch.box.minx > node.children[right].box.minx) right = i;
      if (ch.box.miny > node.children[up].box.miny) up = i;
    }
    
    // calculate normalized distances
    float minX = node.children[left].box.maxx;
    float minY = node.children[down].box.maxy;
    float maxX = node.children[right].box.minx;
    float maxY = node.children[up].box.maxy;
    
    float distX = (maxX - minX)/node.box.width();
    float distY = (maxY - minY)/node.box.height();
    
    // split nodes
    Node[] nodes = new Node[2];
    int i1, i2, cut; // cut -> x == 0; y == 1;
    if (distX > distY) {
      cut = 0;
      i1 = left; i2 = right;
    }
    else {
      cut = 1;
      i1 = down; i2 = up;
    }
    //TODO: ultima parte del greene split
    return nodes;
  }
} 
