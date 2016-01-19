package javaApplet;

import java.awt.List;

public class TestInterface {
   public static void main(String args[]) {
      MammalInt m = new MammalInt();
      m.eat();
      m.travel();
      
      Animal ani = m;
      ani.eat();
      ani.travel();
      
//      Comparable c1 = new Person();
//      Comparable c2 = new Person();
//      if (c1.compareTo(c2) == 0) {
//    	  
//      }
//      
//      List list1 = new List();
//      list1.add(new X());
   }
}
