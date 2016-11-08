package main;

import java.io.PrintWriter;
import java.util.Random;

public class Main {
  public static void main(String[] args) throws Exception {
    // Initialization
    StringBuilder str = new StringBuilder();
    int n = (int) Math.pow(2, 4);
    float range1 = 500000;
    float range2 = 100;
    
    // Specs
    str.append("MACHINE FEATURES\n\n");
    str.append("Processor: Intel Core i3-4005U 1.70 GHz\n");
    str.append("Operative system: Windows 10 Home Single Language\n");
    str.append("Programming language: Java\n");
    str.append("Compiler: Eclipse Compiler for Java\n");
    str.append("Environment: jre1.8.0_66\n");
    str.append("RAM: 4.00 GB\n");
    
    str.append("\n\nPARAMETERS OF THE EXPERIMENT\n\n");
    str.append("Number of samples: " + n + "\n");
    str.append("Randomly generated rectangles with:\n");
    str.append("  (xmin, ymin) in range [0, " + range1 + "]\n");
    str.append("  (width, height) in range [1, " +  range2 + "]\n");

    // Testing parameters
    long initTime, finalTime;
    double diskUsage;
    String[] splitType = {"LINEAR", "GREENE"};

    for (int i = 0; i < 2; i++) {
      // insertion
      RTree tree = new RTree(i);
      initTime = System.currentTimeMillis();
      for (int j = 0; j < n; j++) {
        System.out.println("inserting");
        tree.insert(randomRectangle(range1, range2));
      }
      finalTime = System.currentTimeMillis();
      diskUsage = Math.floor(10000*realDiskUsage(tree.root))/100;
      
      // write results
      str.append("\n\nRESULTS: " + splitType[i] + "SPLIT\n\n");
      str.append("Construction time: ");
      str.append(String.valueOf(finalTime-initTime) + " ms\n");
      str.append("Disk Memory Size: " + 4096*tree.size + " bytes\n");
      str.append("Real Disk Usage: " + diskUsage + "%\n");
      
      // searching
      int k = 0;
      double averageAccesses = 0;
      long averageTime = 0;
      for (int j = 0; j < n/10; j++) {
        System.out.println("searching");
        initTime = System.currentTimeMillis();
        tree.search(randomRectangle(range1, range2));
        finalTime = System.currentTimeMillis();
        averageAccesses += tree.accesses;
        averageTime += finalTime - initTime;
        k++;
      }
      averageAccesses /= k;
      averageTime /= k;
      
      // write results
      str.append("Search average time: " + averageTime + " ms\n");
      str.append("Search average acceses: " + (int) averageAccesses + "\n");
    }
    
    
    // Write results into a text file
    try{
      PrintWriter writer = new PrintWriter("results.txt", "UTF-8");
      String[] lines = str.toString().split("\n");
      for (String s: lines) {
        writer.println(s);
      }
      writer.close();
    } catch (Exception e) {
    }

  }
  
  public static double realDiskUsage(Node node) {
    if (!node.isLeaf()) {
      double sum = (1.0*node.n)/node.M;
      int k = 1;
      for (int i = 0; i < node.n; i++) {
        Node ch = node.children[i];
        if (!ch.isLeaf()) {
          sum += realDiskUsage(node.children[i]);
          k += 1;
        }
      }
      return sum/k;
    }
    return 1;
  }
  
  public static Rectangle randomRectangle(float range1, float range2) {
    Random rnd = new Random();
    float xmin = rnd.nextFloat()*range1;
    float ymin = rnd.nextFloat()*range1; 
    float width = rnd.nextFloat()*(range2-1) + 1;
    float height = rnd.nextFloat()*(range2-1) + 1;
    float xmax = xmin + width;
    float ymax = ymin + height;
    return new Rectangle(new Object(), xmin, ymin, xmax, ymax);
  }
}
