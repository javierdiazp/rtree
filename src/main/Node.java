package main;

public class Node {
  public int m; // min children
  public int M; // max children
  
  public Rectangle box;
  
  public int n; // number of children
  public Node[] children;
  public boolean isRoot;
  
  public Node(Rectangle rect, int mval, int Mval) {
    m = mval; M = Mval;
    box = rect; 
    n = 0;
    children = new Node[M+1];
    isRoot = false;
  }
  
  public boolean isLeaf() {
    return M == 0;
  }
  
  public boolean overflow() {
    return n > M;
  }

  public void copy(Node other) {
    m = other.m;
    M = other.M;
    box = other.box;
    n = other.n;
    children = other.children;
    isRoot = other.isRoot;
  }
  
  public String toString() {
    return buildString(0);
  }
  
  private String buildString(int lvl) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < lvl; i++) {
      str.append("  ");
    }
    str.append(lvl + ". " + box.toString());
    for (int i = 0; i < n; i++) {
      str.append("\n" + children[i].buildString(lvl+1));
    }
    return str.toString();
  }
}
