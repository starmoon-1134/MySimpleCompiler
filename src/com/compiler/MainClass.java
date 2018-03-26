package com.compiler;

import java.io.IOException;

public class MainClass {
  static String sourceFileNameString = null;

  public static void main(String[] args) throws IOException {
    // TODO 自动生成的方法存根
    // System.out.println(MainClass.class.getResource("/"));
    sourceFileNameString = MainClass.class.getResource("/").getFile() + "myCpp.c";
    LexicalAnalyzer.getToken(sourceFileNameString);
  }

}
