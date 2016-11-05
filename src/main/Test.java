package main;

public class Test {
  public static void main(String[] args) {
    int n = 6;
    
    boolean[] test = new boolean[n];
    int[] coords1 = {0, 0, 10, 10};
    Rectangle r1 = new Rectangle("info1", coords1, 0, 0);
    int[] coords2 = {15, 0, 40, 10};
    Rectangle r2 = new Rectangle("info2", coords2, 0, 0);
    int[] coords3 = {10, 0, 20, 10};
    Rectangle r3 = new Rectangle("info3", coords3, 0, 0);
    int[] coords4 = {8, 0, 20, 15};
    Rectangle r4 = new Rectangle("info4", coords4, 0, 0);
    
    Rectangle[] rlist = {r1, r2};
    int[] coords5 = Rectangle.computeMBR(rlist);
    Rectangle r5 = new Rectangle(null, coords5, 0, 0);
    
    RTree rt = new RTree();
    r5.children = rlist;
    r5.nc = 2;
    rt.root = r5;
    Rectangle[] rlist2 = rt.search(r1);
    Rectangle[] rlist3 = rt.search(r4);
    
    System.out.println("Search 1:");
    for (Rectangle r: rlist2) {
      System.out.println("\t" + r.show());
    }
    
    System.out.println("Search 2:");
    for (Rectangle r: rlist3) {
      System.out.println("\t" + r.show());
    }
    
    test[0] = ! r1.intersect(r2);
    test[1] = ! r1.intersect(r3); //Falla porque la interseccion es una arista
    test[2] = r2.intersect(r3);
    test[3] = r4.intersect(r1);
    test[4] = r4.intersect(r2);
    test[5] = r4.intersect(r3);
    
    for (int i = 0; i < n; i++) {
      if (test[i]) System.out.println("Test " + i + " passed");
      else System.out.println("Test " + i + " failed");
    }
  }
}
