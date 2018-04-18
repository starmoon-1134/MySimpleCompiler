package com.compiler;

import java.io.IOException;

public class MainClass {
  static String sourceFileNameString = null;

  public static void main(String[] args) throws IOException {
    // TODO 自动生成的方法存根
    // System.out.println(MainClass.class.getResource("/"));

    sourceFileNameString = MainClass.class.getResource("/").getFile() + "myCpp.c";
    LexicalAnalyzer.getToken(sourceFileNameString);
    SyntaxAnalyzer test = new SyntaxAnalyzer();
    test.startAnalyse(MainClass.class.getResource("/").getFile() + "myCpp_token.txt");

    // HashSet<Item> a1 = new HashSet<>();
    // HashSet<Item> a2 = new HashSet<>();
    // HashSet<Item> a3 = new HashSet<>();
    // Item t1 = new Item(0, 1, "dji1");
    // Item t2 = new Item(0, 1, "dji2");
    // a1.add(t1);
    // a2.add(t2);
    // System.out.println(t1.equals(t2));
    // System.out.println(t1.hashCode());
    // System.out.println(t2.hashCode());
    // System.out.println(a1.equals(a2));
    // System.out.println(a1.hashCode());
    // System.out.println(a2.hashCode());
    // a1.add(t2);
    // a2.add(t1);
    // System.out.println(a1.equals(a2));
    // System.out.println(a1.hashCode());
    // System.out.println(a2.hashCode());
    // a3.add(t2);
    // a3.addAll(a1);
    // System.out.println(a1.equals(a3));
    // String[] s1 = { "s1", "s2" };
    // String[] s2 = { "s1", "s2" };
    // System.out.println(s1.equals(s2));
    // System.out.println(s1.hashCode());
    // System.out.println(s2.hashCode());
    // System.out.println(Arrays.hashCode(s1));
    // System.out.println(Arrays.hashCode(s2));
  }

}
