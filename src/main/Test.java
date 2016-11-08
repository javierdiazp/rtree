package main;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

public class Test {
  public static void main(String[] args) throws Exception {
    Runtime.getRuntime().gc();
    Rectangle r = new Rectangle(null, 0, 0, 0, 0);
    Node node = new Node(r, 1537, 3843);
    System.out.println("hola");
    while (true) {
      TimeUnit.SECONDS.sleep(2);
    }
    /*
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(node);
    oos.close();
    System.out.println(baos.size());*/
    
    
    
    
    
    /*
    RTree tree = new RTree(RTree.LINEAR);
    System.out.println(ObjectSizeFetcher.getObjectSize(tree));
    System.out.println(ObjectSizeFetcher.getObjectSize(new String()));
    int n = 10;
    for (int i = 0; i < n; i++) {
      tree.insert(new Rectangle("hola", 16, 0, 14, 21));
    }
    */
    //System.out.println(tree.toString());
  }
}
