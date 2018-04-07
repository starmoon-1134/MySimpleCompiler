package com.compiler;

import java.io.IOException;

public class MainClass {
  static String sourceFileNameString = null;

  public static void main(String[] args) throws IOException {
    // TODO 自动生成的方法存根
    // System.out.println(MainClass.class.getResource("/"));
    // sourceFileNameString = MainClass.class.getResource("/").getFile() + "myCpp.c";
    SyntaxAnalyzer test = new SyntaxAnalyzer();
    test.startAnalyse(
        MainClass.class.getResource("/").getFile() + "test_token_ArithmeticExpression.txt");
    // ArrayList<Item> a1 = new ArrayList<>();
    // ArrayList<Item> a2 = new ArrayList<>();
    // Item t1 = new Item(0, 1, "dji");
    // Item t2 = new Item(0, 1, "dji");
    // a1.add(t1);
    // a2.add(t2);
    // System.out.println(t1.equals(t2));
    // System.out.println(t1.hashCode());
    // System.out.println(t2.hashCode());
    // System.out.println(a1.equals(a1));
    // System.out.println(a1.hashCode());
    // System.out.println(a1.hashCode());
    // String[] s1 = { "s1", "s2" };
    // String[] s2 = { "s1", "s2" };
    // System.out.println(s1.equals(s2));
    // System.out.println(s1.hashCode());
    // System.out.println(s2.hashCode());
    // System.out.println(Arrays.hashCode(s1));
    // System.out.println(Arrays.hashCode(s2));
  }

}
