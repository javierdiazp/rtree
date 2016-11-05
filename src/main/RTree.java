package main;

import java.util.ArrayList;
import java.util.List;

public class RTree {
  private static final int m = 10; // min children per rectangle
  private static final int M = 1000; // max children per rectangle

  public Rectangle root;
  private int height; // height of the tree
  private int n; // number of elements in the tree
  
  public RTree() {
    int[] initCoords = {0, 0, 0, 0};
    root = new Rectangle(null, initCoords, 2, M);
  }

  public Rectangle[] search(Rectangle c) { 
    List<Rectangle> list = doSearch(c, root);
    Rectangle[] array = new Rectangle[list.size()];
    array = list.toArray(array);
    return array;
  }

  private List<Rectangle> doSearch(Rectangle c, Rectangle currNode) {
    List<Rectangle> ans = new ArrayList<Rectangle>();
    if (currNode.nc == 0) {
      // a leaf
      if (c.intersect(currNode)) {
        ans.add(currNode);
      }
    }
    
    else {
      // an internal node
      for (Rectangle ch: currNode.children) {
        if (c.intersect(ch)) {
          ans.addAll(doSearch(c, ch));
        }
      }
    }
    return ans;
  }

  public void insert(Rectangle c) {
    doInsert(c, root);
    n++;
  }
  
  private void doInsert(Rectangle c, Rectangle currNode) {
    if (currNode.children[0].nc == 0) {
      // children are leafs
      currNode.children[currNode.nc] = c;
      currNode.nc++; 
      
    }
  }
} 
