package com.compiler;

import java.util.ArrayList;

public class SignTable {
  private int pramOffset;// 填表的时候方便记录参数或局部变量的位置（EBP + XXXX），+为参数，-为局部变量
  private int localOffset;
  private SignTable parentTable;
  private ArrayList<Sign> table;
  private StringBuffer Sentence;

  public SignTable()
  {
    super();
    this.pramOffset = 8;
    this.localOffset = 0;
    this.setParentTable(null);
    this.table = new ArrayList<>();
    this.Sentence = new StringBuffer();
  }

  /*
   * 返回字符串的3部分依次为： 是否为局部变量(及参数); 变量类型;偏移
   * 
   * 不直接返回一个Sign的原因：Sign内部没有标记自身是全局的 还是局部的
   */
  public String getOffsetOfID(String idName) {
    String retString;
    SignTable curTable = this;
    while (curTable != null) {
      for (Sign sign : curTable.table) {
        if (sign.id.equals(idName)) {
          retString = (curTable.getParentTable() == null) ? "0" : "1";
          retString += ":" + sign.type;
          retString += ":" + sign.offset;
          return retString;
        }
      }
      curTable = curTable.getParentTable();
    }
    return "-1:-1:-1";
  }

  // public Sign getSignByIdName(String idName) {
  // String retString;
  // SignTable curTable = this;
  // while (curTable != null) {
  // for (Sign sign : curTable.table) {
  // if (sign.id.equals(idName)) {
  // return sign;
  // }
  // }
  // curTable = curTable.getParentTable();
  // }
  // return null;
  // }

  public boolean add(Sign sign) {
    return table.add(sign);
  }

  /**
   * @return parentTable
   */
  public SignTable getParentTable() {
    return parentTable;
  }

  /**
   * @param parentTable
   *          要设置的 parentTable
   */
  public void setParentTable(SignTable parentTable) {
    this.parentTable = parentTable;
  }

  /**
   * @return pramOffset
   */
  public int getPramOffset() {
    return pramOffset;
  }

  /**
   * @param pramOffset
   *          要设置的 pramOffset
   */
  public void addPramOffset(int pramOffset) {
    this.pramOffset += pramOffset;
  }

  /**
   * @return localOffset
   */
  public int getLocalOffset() {
    return localOffset;
  }

  /**
   * @param localOffset
   *          要设置的 localOffset
   */
  public void addLocalOffset(int localOffset) {
    this.localOffset -= localOffset;
  }

  /**
   * @return sentence
   */
  public String getSentence() {
    return Sentence.toString();
  }

  /**
   * @param sentence
   *          要设置的 sentence
   */
  public void addSentence(String sentence) {
    Sentence.append(sentence);
  }

}
