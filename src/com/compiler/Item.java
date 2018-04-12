package com.compiler;

import java.util.Objects;

//class Item {
//  String[] production;
//  char lookahead;// 搜索符
//  int nextSymbol;// 相当于圆点位置
//
//  public Item(String[] prodution, char lookahead, int nextSymbol)
//  {
//    this.production = prodution;
//    this.lookahead = lookahead;
//    this.nextSymbol = nextSymbol;// 用-1表示结束
//  }
//
//  public String[] getProduction() {
//    return this.production;
//  }
//
//  public char getLookahead() {
//    return this.lookahead;
//  }
//
//  public int getNextSymbol() {
//    return this.nextSymbol;
//  }
//
//  @Override
//  public boolean equals(Object ob) {
//    if (ob instanceof Item) {
//      Item item = (Item) ob;
//      /*------------------production认为非null----------------------*/
//      if (item.getProduction().length != production.length) {
//        return false;
//      } else {
//        for (int i = 0; i < production.length; i++) {
//          if (!production[i].equals(item.getProduction()[i])) {
//            return false;
//          }
//        }
//      }
//      if (item.getLookahead() != lookahead) {
//        return false;
//      }
//      if (item.getNextSymbol() != nextSymbol) {
//        return false;
//      }
//      return true;
//    }
//    return false;
//
//  }
//
//  @Override
//  public int hashCode() {
//    return Arrays.hashCode(production) + Objects.hash(lookahead, nextSymbol);
//  }
//}

class Item implements Comparable<Item> {
  int pro_index;// 产生式在SyntaxAnlyzer中的productions中的索引
  int nextSymbol_index;// 相当于圆点位置
  String lookahead;// 搜索符

  public Item(int pro_index, int nextSymbol_index, String lookahead)
  {
    this.pro_index = pro_index;
    this.nextSymbol_index = nextSymbol_index;// 用-1表示结束
    this.lookahead = lookahead;
  }

  public int getPro_index() {
    return this.pro_index;
  }

  public String getLookahead() {
    return this.lookahead;
  }

  public int getNextSymbol() {
    return this.nextSymbol_index;
  }

  public Item getSuccesiveItem() {
    return new Item(this.pro_index, this.nextSymbol_index + 1, this.lookahead);
  }

  @Override
  public boolean equals(Object ob) {
    if (ob instanceof Item) {
      Item item = (Item) ob;
      /*------------------production认为非null----------------------*/
      if (item.getPro_index() != pro_index) {
        return false;
      }
      if (item.getLookahead() != lookahead) {
        return false;
      }
      if (item.getNextSymbol() != nextSymbol_index) {
        return false;
      }
      return true;
    }
    return false;

  }

  @Override
  public int hashCode() {
    return Objects.hash(pro_index, nextSymbol_index, lookahead);
  }

  @Override
  public int compareTo(Item item) {
    if (this.hashCode() > item.hashCode()) {
      return 1;
    } else if (this.hashCode() < item.hashCode()) {
      return -1;
    } else {
      return 0;
    }
    // if(this.pro_index>item.pro_index) {
    // return 1;
    // }else if(this.pro_index<item.pro_index) {
    // return -1;
    // }else {
    // if(this.nextSymbol_index>item.nextSymbol_index) {
    // return 1;
    // }else if(this.nextSymbol_index<item.nextSymbol_index) {
    // return -1;
    // }else {
    // if(this.lookahead.hash)
    // }
    // }

  }
}