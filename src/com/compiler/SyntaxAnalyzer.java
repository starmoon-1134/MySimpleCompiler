package com.compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

public class SyntaxAnalyzer {
  // private String[][] productions = {
  // { "SS", "S" },
  // { "S", "L", "=", "R" },
  // { "S", "R" },
  // { "L", "*", "R" },
  // { "L", "i" },
  // { "R", "L" } };
  private String[][] productions = {
      { "S", "E" },
      { "E", "E", "+", "E" },
      { "E", "T" },
      { "T", "T", "*", "F" },
      { "T", "F" },
      { "F", "(", "E", ")" },
      { "F", "id" } };
  private ArrayList<String> syntaxVariables;
  private ArrayList<String> terminals;/* 定义："@end"为结束符号，"@null"为空符号 */
  private ArrayList<ArrayList<String>> actionTable;
  private ArrayList<ArrayList<Integer>> gotoTable;
  private ArrayList<ArrayList<Item>> closureOfItemSets;

  public SyntaxAnalyzer()
  {
    /* 登记语法变量 */
    int i, j, closureIndex;
    syntaxVariables = new ArrayList<>();
    for (i = 0; i < productions.length; i++) {
      if (!syntaxVariables.contains(productions[i][0])) {
        syntaxVariables.add(productions[i][0]);
      }
    }
    /* 登记终结符 */
    terminals = new ArrayList<>();
    for (i = 0; i < productions.length; i++) {
      for (j = 1; j < productions[i].length; j++) {
        if (!syntaxVariables.contains(productions[i][j])) {
          terminals.add(productions[i][j]);
        }
      }
    }
    terminals.add("@end");
    // if (!terminals.contains("@null")) {
    // terminals.add("@null");
    // }

    /* 构造项目集闭包、action表和goto表 */
    // 初始化
    closureIndex = 0;
    actionTable = new ArrayList<>();
    gotoTable = new ArrayList<>();
    closureOfItemSets = new ArrayList<>();
    ArrayList<String> blankLineOfActionTable = new ArrayList<>();
    for (i = 0; i < terminals.size(); i++) {
      blankLineOfActionTable.add(null);
    }
    ArrayList<Integer> blankLineOfGotoTable = new ArrayList<>();
    for (i = 0; i < syntaxVariables.size(); i++) {
      blankLineOfGotoTable.add(-1);
    }

    // 初始化I0
    ArrayList<Item> curClosure = new ArrayList<Item>();
    curClosure.add(new Item(0, 1, "@end"));// "@end"表示输入结束符号
    /*
     * for (i = 0; i < curClosure.size(); i++) { // 遍历闭包中的所有项目，对每个项目进行"扩张" curItem =
     * curClosure.get(i); curProduction = productions[curItem.getPro_index()]; if
     * (curItem.nextSymbol >= curProduction.length) { // 1规约项目,但I0中不会有规约项目不做处理 } else if
     * (syntaxVariables.contains(curProduction[curItem.nextSymbol])) { // 2待约项目 A->α.Xβ,a //
     * 计算first(βa) ArrayList<String> beta_aArrayList = new ArrayList<>(); for (j =
     * curItem.nextSymbol + 1; j < curProduction.length; j++) {
     * beta_aArrayList.add(curProduction[j]); } beta_aArrayList.add(curItem.lookahead);
     * HashSet<String> beta_aFirst = first((String[]) beta_aArrayList.toArray()); // 遍历所有产生式 for (j
     * = 0; j < productions.length; j++) { if
     * (productions[j][0].equals(curProduction[curItem.nextSymbol])) { // 求闭包：
     * 找到了以B开头的产生式,与beta_aFirst的符号组合，产生新项目 for (String tmpLookahead : beta_aArrayList) { tmpItem =
     * new Item(j, 1, tmpLookahead); if (!curClosure.contains(tmpItem)) { curClosure.add(tmpItem); }
     * } } } } else { // 3移进项目跳过什么都不做 } }
     */
    // 算法5.8 LR1分析表的构造p187
    expandClosure(curClosure);
    actionTable.add((ArrayList<String>) blankLineOfActionTable.clone());// bug4.没有未I0添加action、goto对应的行
    gotoTable.add((ArrayList<Integer>) blankLineOfGotoTable.clone());
    closureOfItemSets.add(curClosure);
    ArrayList<String> allSymbols = new ArrayList<>();
    Hashtable<String, Integer> transfer = new Hashtable<>();// ("i:X",j")表示i状态读一个符号X要转移到j状态
    /* 原有版本是遍历所有的终结符、非终结符，但这样与手工产生的项目集顺序不一致 */
    // allSymbols.addAll(syntaxVariables);
    // allSymbols.addAll(terminals);
    // for (i = 0; i < closureOfItemSets.size(); i++) {
    // for (String X : allSymbols) {
    /* 新版本，按当前项目中的项目顺序，对X进行排列 */
    for (i = 0; i < closureOfItemSets.size(); i++) {
      allSymbols.clear();
      for (Item curItem : closureOfItemSets.get(i)) {
        String[] curProduction = productions[curItem.pro_index];
        if (curItem.nextSymbol_index < curProduction.length
            && !allSymbols.contains(curProduction[curItem.nextSymbol_index])) {
          allSymbols.add(curProduction[curItem.nextSymbol_index]);
        }
      }
      for (String X : allSymbols) {
        ArrayList<Item> tmpClosure = go(i, X);
        if (!tmpClosure.isEmpty()) {
          int goIX_state;
          if ((goIX_state = closureOfItemSets.indexOf(tmpClosure)) < 0) { // !closureOfItemSets.contains(tmpClosure)
            closureOfItemSets.add(tmpClosure);
            transfer.put(i + ":" + X, closureOfItemSets.size() - 1);
            actionTable.add((ArrayList<String>) blankLineOfActionTable.clone());
            gotoTable.add((ArrayList<Integer>) blankLineOfGotoTable.clone());
          } else {// bug3.i状态读入X将要转移的状态已经存在，但仍要添加一条边
            transfer.put(i + ":" + X, goIX_state);
          }
        }
      }
    }
    /* 构造action表和goto表 */
    for (i = 0; i < closureOfItemSets.size(); i++) {
      curClosure = closureOfItemSets.get(i);
      for (

      Item curItem : curClosure) {
        String[] curProduction = productions[curItem.pro_index];
        if (curItem.nextSymbol_index >= curProduction.length) {
          // 1.规约项目
          actionTable.get(i).set(terminals.indexOf(curItem.lookahead), "r:" + curItem.pro_index);
          if (curItem.equals(closureOfItemSets.get(1).get(0))) {
            actionTable.get(i).set(terminals.indexOf(curItem.lookahead), "@acc");
          }
        } else if (terminals.contains(curProduction[curItem.nextSymbol_index])) {
          // 2.移进项目
          int nextState = transfer.get(i + ":" + curProduction[curItem.nextSymbol_index]);
          actionTable.get(i).set(terminals.indexOf(curProduction[curItem.nextSymbol_index]),
              "s:" + nextState);
        } else {
          // 3.待约项目
          int nextState = transfer.get(i + ":" + curProduction[curItem.nextSymbol_index]);
          gotoTable.get(i).set(syntaxVariables.indexOf(curProduction[curItem.nextSymbol_index]),
              nextState);
        }
      }
    }
    printTableAndSet();
    // System.out.println(closureOfItemSets);
    // System.out.println(actionTable);
    // System.out.println(gotoTable);

    // actionTable.add((ArrayList<String>) blankLineOfActionTable.clone());
    // gotoTable.add((ArrayList<Integer>) blankLineOfGotoTable.clone());
    // Queue<ArrayList<Item>> queue = new LinkedList<ArrayList<Item>>();
    // queue.add(curClosure);
    // while (!queue.isEmpty()) {
    // curClosure = queue.remove();
    // expandClosure(curClosure);
    // int curState = closureOfItemSets.indexOf(curClosure);
    // Hashtable<String, Integer> go = new Hashtable<>();// ("i:X",j")表示i状态读一个符号X要转移到j状态
    // for (Item curItem : curClosure) {
    // String[] curProduction = productions[curItem.pro_index];
    // if (curItem.pro_index >= curProduction.length) {
    // // 1规约项目
    // if (curItem.lookahead.equals("@end")) {
    // // 1.1接受
    // actionTable.get(curState).set(terminals.size() - 1, "@acc");
    // } else {
    // // 1.2选相应的产生式规约
    // actionTable.get(curState).set(terminals.indexOf(curItem.lookahead),
    // "r:" + curItem.pro_index);
    // }
    // } else {// 2移进项目&&待约项目
    // String nextSymbol = curProduction[curItem.nextSymbol];
    // if (go.containsKey(curState + ":" + nextSymbol)) {
    // // 2.1后继项目所属的项目集已经存在
    // int indexOfSuccessiveItem = go.get(curState + ":" + nextSymbol);
    // ArrayList<Item> successiveItems = closureOfItemSets.get(indexOfSuccessiveItem);
    // successiveItems.add(curItem.getSuccesiveItem());
    // } else {
    // // 2.2后继项目所属的项目集不存在
    // ArrayList<Item> successiveItems = new ArrayList<>();
    // go.put(curState + ":" + nextSymbol, closureOfItemSets.size());
    // closureOfItemSets.add(successiveItems);
    // successiveItems.add(curItem.getSuccesiveItem());
    // }
    // }
    // }
    // }
  }

  public void startAnalyse(String tokenFileName) throws IOException {
    FileReader FileReader = new FileReader(tokenFileName);
    BufferedReader fin = new BufferedReader(FileReader);
    Stack<Integer> stateSt = new Stack<>();
    Stack<String> symbolSt = new Stack<>();
    stateSt.push(0);
    symbolSt.push("@end");
    String curLine;
    while ((curLine = fin.readLine()) != null) {
      String[] tmpStrings = curLine.split(":");
      String nextSymbol = tmpStrings[0];
      while (true) {// bug5.规约之后nextSymbol并没有入栈，不应该再去读下一行
        String curActionString = actionTable.get(stateSt.peek()).get(terminals.indexOf(nextSymbol));
        if (curActionString == null) {
          // 1.未定义，出错
          break;
        } else if (curActionString.equals("@acc")) {
          // 2.接受

          return;
        } else {
          // 3.移进或规约
          String[] action = curActionString.split(":");
          if (action[0].equals("s")) {
            // 3.1移进
            stateSt.push(Integer.valueOf(action[1]));
            symbolSt.push(nextSymbol);
            break;
          } else {
            // 3.2规约
            String[] curProduction = productions[Integer.valueOf(action[1])];
            System.out.print(curProduction[0] + "->");
            for (int i = 1; i < curProduction.length; i++) {
              stateSt.pop();
              symbolSt.pop();
              System.out.print(curProduction[i]);
            }
            System.out.println();
            symbolSt.push(curProduction[0]);
            stateSt
                .push(gotoTable.get(stateSt.peek()).get(syntaxVariables.indexOf(symbolSt.peek())));

          }
        }
      } // while(true)
    } // while(curline!=null)
    fin.close();
    FileReader.close();
  }

  private void printTableAndSet() {
    // 打印项目集规范族
    int i, j;
    for (i = 0; i < closureOfItemSets.size(); i++) {
      ArrayList<Item> curClosure = closureOfItemSets.get(i);
      System.out.println("I" + i + ":");
      for (Item curItem : curClosure) {
        String[] curProduction = productions[curItem.pro_index];
        System.out.print(curProduction[0] + "->");
        for (j = 1; j < curProduction.length; j++) {
          if (j == curItem.nextSymbol_index) {
            System.out.print(".");
          }
          System.out.print(curProduction[j]);
        }
        if (j == curItem.nextSymbol_index) {
          System.out.print(".");
        }
        System.out.println("," + curItem.lookahead);
      }
    }
    System.out.println("------------------------------------------------");
    System.out.println("actionTable:");
    Formatter formatter = new Formatter(System.out);
    formatter.format("%-6s|", "id");
    for (i = 0; i < terminals.size(); i++) {
      formatter.format("%-7s", terminals.get(i));
    }
    System.out.println();
    for (i = 0; i < actionTable.size(); i++) {
      formatter.format("%-6s|", i);
      for (j = 0; j < terminals.size(); j++) {
        formatter.format("%-7s", actionTable.get(i).get(j));
      }
      System.out.println();
    }
    System.out.println("------------------------------------------------");
    System.out.println("gotoTable:");
    formatter.format("%-6s|", "id");
    for (i = 0; i < syntaxVariables.size(); i++) {
      formatter.format("%-7s", syntaxVariables.get(i));
    }
    System.out.println();
    for (i = 0; i < gotoTable.size(); i++) {
      formatter.format("%-6s|", i);
      for (j = 0; j < syntaxVariables.size(); j++) {
        formatter.format("%-7s", gotoTable.get(i).get(j));
      }
      System.out.println();
    }
    System.out.println("------------------------------------------------");
  }

  private void expandClosure(ArrayList<Item> closure) {
    /* 计算LR1项目集I（closure）的闭包-》 书上的Closure函数 */
    int i, j;
    String[] curProduction;
    Item curItem, tmpItem;
    for (i = 0; i < closure.size(); i++) {
      // 遍历闭包中的所有项目，对每个项目进行"扩张"
      curItem = closure.get(i);
      curProduction = productions[curItem.getPro_index()];
      if (curItem.nextSymbol_index >= curProduction.length) {
        // 1规约项目,跳过
      } else if (syntaxVariables.contains(curProduction[curItem.nextSymbol_index])) {
        // 2待约项目 A->α.Xβ,a
        // 计算first(βa)
        ArrayList<String> beta_aArrayList = new ArrayList<>();
        for (j = curItem.nextSymbol_index + 1; j < curProduction.length; j++) {
          beta_aArrayList.add(curProduction[j]);
        }
        beta_aArrayList.add(curItem.lookahead);

        HashSet<String> beta_aFirst = first(
            (String[]) beta_aArrayList.toArray(new String[beta_aArrayList.size()]));
        // 遍历所有产生式
        for (j = 0; j < productions.length; j++) {
          if (productions[j][0].equals(curProduction[curItem.nextSymbol_index])) {
            // 找到了以B开头的产生式,与beta_aFirst的符号组合，产生新项目
            for (String tmpLookahead : beta_aFirst) {// bug1<-beta_aArrayList
              tmpItem = new Item(j, 1, tmpLookahead);
              if (!closure.contains(tmpItem)) {
                closure.add(tmpItem);
              }
            }
          }
        }
      } else {
        // 3移进项目跳过什么都不做
      }
    }
  }

  private ArrayList<Item> go(int i, String X) {
    ArrayList<Item> ret = new ArrayList<>();
    for (Item curItem : closureOfItemSets.get(i)) {// bug2.未检查X是否为圆点后的符号
      if (curItem.nextSymbol_index < productions[curItem.pro_index].length
          && productions[curItem.pro_index][curItem.nextSymbol_index].equals(X))
        ret.add(curItem.getSuccesiveItem());
    }
    expandClosure(ret);
    return ret;
  }

  private HashSet<String> first(String[] alpha) {
    /* 求文法符号串alpha的first集 */
    HashSet<String> ret = new HashSet<>();
    for (int i = 0; i < alpha.length; i++) {
      ret.addAll(first(alpha[i]));
      if (!ret.contains("@null")) {
        // 某个文法符号非空，就不需要往下找
        return ret;
      } else {
        // 某个文法符号可空，除去空并继续找
        ret.remove("@null");
      }
    }
    // 由于是用于LR1的beta_a的first集计算，出现@null说明有问题，至少a应该是终结符
    System.err.println(alpha[alpha.length - 1] + "不是终结符");
    // 没够提前返回，说明每一个文法符号都可空
    ret.add("@null");
    return ret;
  }

  private HashSet<String> first(String X) {
    /* 求单个文法符号的first集 */
    HashSet<String> ret = new HashSet<>();
    if (terminals.contains(X)) {
      ret.add(X);
    } else {
      int i, j;
      for (i = 0; i < productions.length; i++) {
        if (productions[i][0].equals(X)) {
          if (productions[i][1].equals("@null")) {
            // 1如果这个产生式为 X->@null; 则添加@null并且跳过该产生式
            ret.add("@null");
            continue;
          }
          // 2如果是非空产生式X->Y1_Y2_Y3...Yn
          for (j = 2; j < productions[i].length; j++) {
            if (terminals.contains(productions[i][j])) {
              // 2.1遇到终结符,添加终结符该并跳过该产生式
              ret.add(productions[i][j]);
              break;
            } else {
              // 2.2遇到非终结符,递归计算其first集
              HashSet<String> y_first = first(productions[i][j]);
              if (y_first.contains("@null")) {
                // 2.2.1若有@null，合并之后需要再往下找下一个Yj所产生的first符
                y_first.remove("@null");
                ret.addAll(y_first);
              } else {
                // 2.2.2否则，合并之后跳过该产生式
                ret.addAll(y_first);
                break;
              }
            }
          }
          if (j == productions[i].length) {
            /*
             * 处理产生式X->Y1_Y2_Y3...Yn 没有提前终止 Y1,Y2,...,Yn都是非终结符且均可空，即有X可空
             */
            ret.add("@null");
          }
        }
      }
    }
    return ret;
  }
}
