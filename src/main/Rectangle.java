package main;

public class Rectangle {

  public Object data;
  public float minx; public float miny; // min coordinate
  public float maxx; public float maxy; // max coordinate

  public Rectangle(Object obj, float minx, float miny, float maxx, float maxy) {
    data = obj;
    this.minx = minx; this.miny = miny;
    this.maxx = maxx; this.maxy = maxy;
  }
  
  public float height() {
    return this.maxy - this.miny;
  }
  
  public float width() {
    return this.maxx - this.minx;
  }
  
  public Object getData() {
    return data;
  }

  public boolean intersect(Rectangle other) {
    return 
        this.minx > other.minx - this.width() &&
        this.miny > other.miny - this.height() &&
        this.minx < other.maxx &&
        this.miny < other.maxy;
  }
  
  public float area() {
    return width()*height();
  }
  
  public static Rectangle computeMBR(Rectangle... list) {
    Rectangle mbr = new Rectangle(null,
        list[0].minx, list[0].miny,
        list[0].maxx, list[0].maxy);

    for (Rectangle r: list) {
      if (r.minx < mbr.minx) mbr.minx = r.minx;
      if (r.miny < mbr.miny) mbr.miny = r.miny;
      if (r.maxx > mbr.maxx) mbr.maxx = r.maxx;
      if (r.maxy > mbr.maxy) mbr.maxy = r.maxy;
    }
    return mbr;
  }
  
  public String toString() {

    return 
        "Rectangle(" +
        this.minx + ", " + this.miny + ", " +
        this.maxx + ", " + this.maxy + ")";
  }
}