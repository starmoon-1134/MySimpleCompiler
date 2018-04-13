package com.compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

public class SyntaxAnalyzer {
  private String[][] productions = {
      { "SS", "S" },
      { "S", "L", "=", "R" },
      { "S", "R" },
      { "L", "*", "R" },
      { "L", "i" },
      { "L", "@null" },
      { "R", "L" } };
  // private String[][] productions = {
  // { "S", "E" },
  // { "E", "E", "+", "E" },
  // { "E", "T" },
  // { "T", "T", "*", "F" },
  // { "T", "F" },
  // { "F", "(", "E", ")" },
  // { "F", "id" } };
  private ArrayList<String> syntaxVariables;
  private ArrayList<String> terminals;/* 定义："@end"为结束符号，"@null"为空符号 */
  private ArrayList<ArrayList<String>> actionTable;
  private ArrayList<ArrayList<Integer>> gotoTable;
  private ArrayList<HashSet<Item>> closureOfItemSets;

  public SyntaxAnalyzer() throws IOException
  {
    // PrintStream printStream = new PrintStream(new FileOutputStream(
    // new File(MainClass.class.getResource("/").getFile() + "SyntaxResult.txt"), true));
    // System.setOut(printStream);
    // System.setErr(printStream);

    getProductions(MainClass.class.getResource("/").getFile() + "production.txt");
    /* 登记语法变量 */
    int i, j;
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
          if (!terminals.contains(productions[i][j])) {// bug6.未检查重复
            terminals.add(productions[i][j]);
          }
        }
      }
    }
    terminals.add("@end");
    terminals.remove("@null");
    // if (!terminals.contains("@null")) {
    // terminals.add("@null");
    // }
    // for (String ssss : first("StartS")) {
    // System.out.println(ssss);
    // }

    /* 构造项目集闭包、action表和goto表 */
    // 初始化
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
    HashSet<Item> curClosure = new HashSet<Item>();
    curClosure.add(new Item(0, 1, "@end"));// "@end"表示输入结束符号

    // 算法5.8 LR1分析表的构造p187 ?5.7项目集规范族构造
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
        if (!isReducItem(curItem, curProduction)
            && !allSymbols.contains(curProduction[curItem.nextSymbol_index])) {
          allSymbols.add(curProduction[curItem.nextSymbol_index]);
        }
      }
      for (String X : allSymbols) {
        HashSet<Item> tmpClosure = go(i, X);
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
      for (Item curItem : curClosure) {
        String[] curProduction = productions[curItem.pro_index];
        if (isReducItem(curItem, curProduction)) {
          // 1.规约项目
          actionTable.get(i).set(terminals.indexOf(curItem.lookahead), "r:" + curItem.pro_index);
          if (curItem.equals(new Item(0, 2, "@end"))) {
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

    int repeat = 0;
    for (i = 0; i < closureOfItemSets.size(); i++) {
      for (j = 0; j < closureOfItemSets.size(); j++) {
        if (i == j)
          continue;
        HashSet<Item> ci = closureOfItemSets.get(i);
        HashSet<Item> cj = closureOfItemSets.get(j);
        if (ci.size() > cj.size()) {
          continue;
        }
        int ttttt = 1;
        for (Item item : ci) {
          if (!cj.contains(item)) {
            ttttt = 0;
            break;
          }
        }
        if (ttttt == 1) {
          repeat++;
        }
      }
    }
    // for (i = 0; i < closureOfItemSets.size(); i++) {
    // repeat += closureOfItemSets.get(i).size();
    // }
    System.out.println("repeat:" + repeat);

    // printProductions();
    // printTableAndSet();

  }

  public void startAnalyse(String tokenFileName) throws IOException {
    boolean[] testnum = new boolean[416];
    FileReader FileReader = new FileReader(tokenFileName);
    BufferedReader fin = new BufferedReader(FileReader);
    Stack<Integer> stateSt = new Stack<>();
    Stack<String> symbolSt = new Stack<>();
    stateSt.push(0);
    symbolSt.push("@end");
    String curLine;
    while ((curLine = fin.readLine()) != null) {
      testnum[stateSt.peek()] = true;
      String[] tmpStrings = curLine.split(":");
      String nextSymbol = tmpStrings[0];
      while (true) {// bug5.规约之后nextSymbol并没有入栈，不应该再去读下一行
        String curActionString = actionTable.get(stateSt.peek()).get(terminals.indexOf(nextSymbol));
        if (curActionString == null) {
          // 1.未定义，出错
          System.err.println("规约出错:" + nextSymbol);
          break;
        } else if (curActionString.equals("@acc")) {
          // 2.接受
          System.out.println("acc");
          break;
        } else {
          // 3.移进或规约
          String[] action = curActionString.split(":");
          if (action[0].equals("s")) {
            // 3.1移进
            // System.out.println("移进： " + nextSymbol);
            stateSt.push(Integer.valueOf(action[1]));
            symbolSt.push(nextSymbol);
            break;
          } else {
            // 3.2规约
            String[] curProduction = productions[Integer.valueOf(action[1])];
            System.out.print(curProduction[0] + "->");
            for (int i = 1; i < curProduction.length; i++) {
              if (!curProduction[1].equals("@null")) {
                stateSt.pop();
                symbolSt.pop();
              }
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
    int used = 0;
    for (int i = 0; i < 416; i++) {
      if (testnum[i])
        used++;
    }
    System.out.println("used:" + used);
    fin.close();
    FileReader.close();
    // System.setOut(System.out);
    // System.setErr(System.err);
  }

  private void printTableAndSet() throws FileNotFoundException {
    // 打印项目集规范族
    int i, j;
    for (i = 0; i < closureOfItemSets.size(); i++) {
      HashSet<Item> curClosure = closureOfItemSets.get(i);
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

  private void printProductions() {
    System.out.println("产生式：");
    for (int i = 0; i < productions.length; i++) {
      String[] curPro = productions[i];
      System.out.print(curPro[0] + "->");
      for (int j = 1; j < curPro.length; j++)
        System.out.print(curPro[j] + "  ");
      System.out.println();
    }
    System.out.println("----------------------------------------------");
  }

  private void expandClosure(HashSet<Item> closure) {
    /* 计算LR1项目集I（closure）的闭包-》 书上的Closure函数 */
    int i, j;
    String[] curProduction;
    Item tmpItem;
    int ttt = 1;
    while (ttt == 1) {
      ttt = 0;
      Item[] myArray = closure.toArray(new Item[] {});
      for (Item curItem : myArray) {
        // 遍历闭包中的所有项目，对每个项目进行"扩张"
        curProduction = productions[curItem.getPro_index()];
        if (isReducItem(curItem, curProduction)) {
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
                  ttt = 1;
                }
              }
            }
          }
        } else {
          // 3移进项目跳过什么都不做
        }
      }
    }
  }

  private boolean isReducItem(Item curItem, String[] curProduction) {
    if (curItem.nextSymbol_index >= curProduction.length || curProduction[1].equals("@null")) {
      return true;
    }
    return false;
  }

  public void getProductions(String productionPath) throws IOException {
    ArrayList<ArrayList<String>> ProArrayList = new ArrayList<>();
    BufferedReader productionReader = new BufferedReader(new FileReader(productionPath));
    String production;
    while ((production = productionReader.readLine()) != null) {
      String productionLeft = production.substring(0, production.indexOf('-'));
      String productionRight = production.substring(production.indexOf('>') + 1,
          production.length());
      ArrayList<String> onePro = new ArrayList<>();
      onePro.add(productionLeft);
      for (String sym : productionRight.split(" ")) {
        onePro.add(sym);
      }
      ProArrayList.add(onePro);
    }
    int n = ProArrayList.size();
    String[][] myArray = new String[n][]; // 定义二维数组
    for (int i = 0; i < n; i++) // 构造二维数组
    {
      ArrayList<String> tempArray = (ArrayList<String>) ProArrayList.get(i);
      myArray[i] = (String[]) tempArray.toArray(new String[0]); // 注意此处的写法
    }
    productions = myArray;
    productionReader.close();
  }

  private HashSet<Item> go(int i, String X) {
    HashSet<Item> ret = new HashSet<>();
    for (Item curItem : closureOfItemSets.get(i)) {// bug2.未检查X是否为圆点后的符号
      if (!isReducItem(curItem, productions[curItem.pro_index])
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
          if (productions[i][0].equals(productions[i][1])) {
            // 3如果左递归，跳过
            continue;
          }
          // 2如果是非空产生式X->Y1_Y2_Y3...Yn
          for (j = 1; j < productions[i].length; j++) {
            if (terminals.contains(productions[i][j])) {
              // 2.1遇到终结符,添加终结符该并跳过该产生式
              ret.add(productions[i][j]);
              break;
            } else {
              // 2.2遇到非终结符,递归计算其first集
              if (productions[i][0].equals(productions[i][j])) {
                // 如果左递归，跳过
                continue;
              }
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
