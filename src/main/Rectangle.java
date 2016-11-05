package main;

public class Rectangle {
  private int m; // min children
  private int M; // max children
  
  private int minx; private int miny; // min coordinate
  private int maxx; private int maxy; // max coordinate
  private Object data;
  
  protected int nc; // number of children
  protected Rectangle[] children = new Rectangle[M];

  public Rectangle(Object obj, int[] coords, int mval, int Mval) {
    data = obj;
    minx = coords[0]; miny = coords[1];
    maxx = coords[2]; maxy = coords[3];
    m = mval; M = Mval;
  }
  
  private int height() {
    return this.maxy - this.miny;
  }
  
  private int width() {
    return this.maxx - this.minx;
  }

  public boolean intersect(Rectangle other) {
    return 
        this.minx > other.minx - this.width() &&
        this.miny > other.miny - this.height() &&
        this.minx < other.maxx &&
        this.miny < other.maxy;
  }
  
  public static int[] computeMBR(Rectangle[] list) {
    int[] mbr = {
        list[0].minx, list[0].miny, 
        list[0].maxx, list[0].maxy};
    
    for (Rectangle r: list) {
      if (r.minx < mbr[0]) mbr[0] = r.minx;
      if (r.miny < mbr[1]) mbr[1] = r.miny;
      if (r.maxx > mbr[2]) mbr[2] = r.maxx;
      if (r.maxy > mbr[3]) mbr[3] = r.maxy;
    }
    return mbr;
  }
  
  private void updateMBR() {
    int[] mbr = computeMBR(this.children);
    this.minx = mbr[0]; this.miny = mbr[1];
    this.maxx = mbr[2]; this.maxy = mbr[3];
  }
  
  public String show() {
    return 
        "(" + this.minx + ", " + this.miny + ") : " +
        "(" + this.maxx + ", " + this.maxy + ")";
  }
}