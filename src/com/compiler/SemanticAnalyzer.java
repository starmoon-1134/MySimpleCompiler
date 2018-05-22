package com.compiler;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class SemanticAnalyzer {
  SignTable globalSignTable;
  SignTable curTable;

  Stack<SignTable> tableSt;
  Stack<String> typeSt;
  Stack<String> valueSt;
  Stack<Integer> paraSize;// 一个函数对应2个值，下边是最大值，上边是当前值
  Stack<ArrayList<Integer>> trueListST;
  Stack<ArrayList<Integer>> falseListST;
  Stack<ArrayList<Integer>> nextListST;
  HashMap<String, Integer> LabelMap;
  Stack<String> labelSt;

  StringBuffer dataSecBuff;
  StringBuffer codeSecBuff;

  int LabelCtn = 0;
  int constCtn = 0;
  String curCallFunc;
  boolean isEBX0 = false;// EBX是否为0
  boolean isEAX0 = false;// EAX是否为0

  private String[][] productions;

  // BufferedReader symbolReader;

  public SemanticAnalyzer(String[][] productions) throws FileNotFoundException
  {
    // TODO 自动生成的构造函数存根
    this.productions = productions;
    globalSignTable = new SignTable();
    globalSignTable.addPramOffset(-8);// 初始置0
    curTable = globalSignTable;
    // curTable = globalSignTable;
    tableSt = new Stack<>();
    typeSt = new Stack<>();
    valueSt = new Stack<>();
    paraSize = new Stack<>();
    trueListST = new Stack<>();
    falseListST = new Stack<>();
    nextListST = new Stack<>();
    LabelMap = new HashMap<>();
    labelSt = new Stack<>();

    // symbolReader = new BufferedReader(new FileReader(MainClass.symbolFileNameString));
    dataSecBuff = new StringBuffer(".section .data\n");
    codeSecBuff = new StringBuffer(".section .text\n");
    curTable.add(new Sign("printf", "int", -1, -1, null, true));
    curTable.add(new Sign("scanf", "int", -1, -1, null, true));
  }

  public void executeAction(int proID) throws IOException {
    String[] curProduction = productions[proID];
    String curString = curProduction[0] + "->" + curProduction[1];
    for (int i = 2; i < curProduction.length; i++) {
      curString += " " + curProduction[i];
    }
    if (curString.equals("S->Start")) {
      FileWriter asmFile = new FileWriter(MainClass.asmFileNameString);
      asmFile.write(dataSecBuff.toString());
      asmFile.write("\n");
      asmFile.write(codeSecBuff.toString());
      asmFile.close();
      // openExe("gcc " + MainClass.asmFileNameString.substring(1) + " -o "
      // + MainClass.exeFileNameString.substring(1));
      // Desktop.getDesktop().open(new File(MainClass.genexeFileNameString));
      // try {
      // Thread.sleep(1000);
      // } catch (InterruptedException e) {
      // // TODO 自动生成的 catch 块
      // e.printStackTrace();
      // }
      Desktop.getDesktop().open(new File(MainClass.batFileNameString));
      // openExe("cmd /c start" + MainClass.exeFileNameString);

    } else if (curString.equals("Start->MainDef")) {

    } else if (curString.equals("Start->FuncList MainDef")) {

    } else if (curString.equals("Start->DeclareS Start")) {

    } else if (curString.equals("MainDef->int main ( M0 ParaList ) { Sen return Expr ; }")) {
      codeSecBuff.append("\n.globl _main\n");
      codeSecBuff.append("_main:\n");
      codeSecBuff.append("    pushl %ebp\n");
      codeSecBuff.append("    movl %esp, %ebp\n");
      codeSecBuff.append("    sub $" + curTable.getReserveSize() + ",%esp\n");

      String Expr = valueSt.pop();
      Sign retSign = curTable.getSignByIdName(Expr);
      if (retSign == null) {
        System.out.println("使用未定义变量：" + Expr);
      } else if (!retSign.type.equals("int") && !retSign.type.equals("intConst")) {
        System.out.println("main函数返回类型不匹配：int<-" + retSign.type);
      } else if (retSign.isGlobal) {// 全局变量
        curTable.addSentence("    movl $" + retSign.id + ",%ebx\n");
        switch (retSign.type) {
          case "char":
            curTable.addSentence("    movb (%ebx),%al\n");
            break;
          default:
            curTable.addSentence("    movl (%ebx),%eax\n");
            break;
        }
      } else {// 局部变量
        switch (retSign.type) {
          case "char":
            curTable.addSentence("    movb " + retSign.offset + "(%ebp),%al\n");
            break;
          default:
            curTable.addSentence("    movl " + retSign.offset + "(%ebp),%eax\n");
            break;
        }
      }
      curTable.addSentence("    leave\n    ret\n");
      codeSecBuff.append(curTable.getSentence());
      // codeSecBuff.append()
      // codeSecBuff.append();

    } else if (curString.equals("FuncList->FuncList FuncDef")) {

    } else if (curString.equals("FuncList->FuncDef")) {

    } else if (curString.equals("FuncDef->Type id ( M1 ParaList ) { Sen return Expr ; }")) {
      String Expr = valueSt.pop();
      String funcName = valueSt.pop();
      String retType = typeSt.pop();
      Sign retSign = curTable.getSignByIdName(Expr);

      codeSecBuff.append("\n.globl _" + funcName + "\n");
      codeSecBuff.append("_" + funcName + ":\n");
      codeSecBuff.append("    pushl %ebp\n");
      codeSecBuff.append("    movl %esp, %ebp\n");
      codeSecBuff.append("    sub $" + curTable.getReserveSize() + ",%esp\n");

      if (retSign == null) {
        System.out.println("使用未定义变量：" + Expr);
        // } else if (!retSign.type.equals("int") && !retSign.type.equals("intConst")) {
        // System.out.println(funcName+"函数返回类型不匹配："+retType+"<-" + retSign.type);
      } else if (retSign.isGlobal) {// 全局变量
        curTable.addSentence("    movl $" + retSign.id + ",%ebx\n");
        switch (retSign.type) {
          case "char":
            curTable.addSentence("    movb (%ebx),%al\n");
            break;
          default:
            curTable.addSentence("    movl (%ebx),%eax\n");
            break;
        }
      } else {// 局部变量
        switch (retSign.type) {
          case "char":
            curTable.addSentence("    movb " + retSign.offset + "(%ebp),%al\n");
            break;
          default:
            curTable.addSentence("    movl " + retSign.offset + "(%ebp),%eax\n");
            break;
        }
      }
      curTable.addSentence("    leave\n    ret\n");
      codeSecBuff.append(curTable.getSentence());
    } else if (curString.equals("ParaList->@null")) {

    } else if (curString.equals("ParaList->Type ParaID")) {

    } else if (curString.equals("ParaList->Type ParaID , ParaList1")) {

    } else if (curString.equals("ParaList1->Type ParaID")) {

    } else if (curString.equals("ParaList1->Type ParaID , ParaList1")) {

    } else if (curString.equals("ParaID->id")) {
      Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getPramOffset(), -1, null,
          false);
      curTable.addPramOffset(4);
      curTable.add(tmpSign);
      typeSt.pop();
    } else if (curString.equals("Sen->DeclareS Sen")) {

    } else if (curString.equals("Sen->AssignS M3 Sen")) {
      nextListST.push(new ArrayList<>());
    } else if (curString.equals("Sen->ifS M3 Sen")) {
      ArrayList<Integer> s2NextList = nextListST.pop();
      curTable.backpatch(nextListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      nextListST.push(s2NextList);
    } else if (curString.equals("Sen->whileS M3 Sen")) {
      ArrayList<Integer> s2NextList = nextListST.pop();
      curTable.backpatch(nextListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      nextListST.push(s2NextList);
    } else if (curString.equals("Sen->CallS ; Sen")) {

    } else if (curString.equals("Sen->@null")) {

    } else if (curString.equals("CallParaList->@null")) {

    } else if (curString.equals("CallParaList->CallExpr")) {

    } else if (curString.equals("CallParaList->CallExpr , CallParaList1")) {

    } else if (curString.equals("CallParaList1->CallExpr")) {

    } else if (curString.equals("CallParaList1->CallExpr , CallParaList1")) {

    } else if (curString.equals("CallExpr->Expr")) {// 在这里需要检查参数信息
      String paraID = valueSt.pop();
      Sign paraSign = curTable.getSignByIdName(paraID);
      if (paraSign == null) {
        System.err.println("使用未定义变量:" + paraID);
        return;
      }
      if (curCallFunc.equals("printf") && paraSign.type.equals("float")) {
        if (paraSign.isGlobal) {
          curTable.addSentence("    flds " + paraSign.id + "\n");
        } else {
          curTable.addSentence("    flds " + paraSign.offset + "(%ebp)\n");
        }
        curTable.addSentence("    fstpl " + paraSize.peek() + "(%esp)\n");
        paraSize.push(paraSize.pop() + 8);
      } else if (curCallFunc.equals("printf") && paraSign.type.equals("char")) {
        if (paraSign.isGlobal) {
          curTable.addSentence("    movsbl " + paraSign.id + ",%eax\n");
        } else {
          curTable.addSentence("    movsbl " + paraSign.offset + "(%ebp),%eax\n");
        }
        curTable.addSentence("    movl %eax," + paraSize.peek() + "(%esp)\n");
        paraSize.push(paraSize.pop() + 4);
      } else if (curCallFunc.equals("scanf")) {
        if (paraSign.isGlobal) {
          curTable.addSentence("    movl $" + paraSign.id + ",%ebx\n");
        } else {
          curTable.addSentence("    movl " + paraSign.offset + "(%ebp),%ebx\n");
        }
        curTable.addSentence("    movl %ebx," + paraSize.peek() + "(%esp)\n");
        paraSize.push(paraSize.pop() + 4);
      } else {
        // if (paraSign.isGlobal) {
        // curTable.addSentence(" movl $" + paraSign.id + ",%ebx\n");
        // } else {
        // curTable.addSentence(" movl " + paraSign.offset + "(%ebp),%ebx\n");
        // }
        // if (paraSign.type.equals("charArray")) {/////////////////////////////////////////////
        // curTable.addSentence(" movl %ebx," + paraSize.peek() + "(%esp)\n");
        // } else {
        // curTable.addSentence(" movl (%ebx),%eax\n");
        // curTable.addSentence(" movl %eax," + paraSize.peek() + "(%esp)\n");
        // }
        movir(paraID, paraSize.peek() + "(%esp)");
        paraSize.push(paraSize.pop() + 4);
      }

    } else if (curString.equals("DeclareS->Type IDlist ;")) {
      typeSt.pop();// 511
      curTable.clearInnerVarCtn();
    } else if (curString.equals("IDlist->ID1")) {

    } else if (curString.equals("IDlist->ID2")) {

    } else if (curString.equals("IDlist->ID3")) {

    } else if (curString.equals("IDlist->ID1 , IDlist")) {
      // typeSt.push(typeSt.peek()); 511
    } else if (curString.equals("IDlist->ID2 , IDlist")) {
      // typeSt.push(typeSt.peek()); 511
    } else if (curString.equals("IDlist->ID3 , IDlist")) {
      // typeSt.push(typeSt.peek()); 511
    } else if (curString.equals("ID1->id")) {
      declareSimVar();
      valueSt.pop();
    } else if (curString.equals("ID2->id = Expr")) {
      String Expr = valueSt.pop();
      declareSimVar();
      String variable = valueSt.pop();
      movii(Expr, variable);
    } else if (curString.equals("ID3->id [ intConst ]")) {
      declareArr();// 内部已将intConst pop掉
      valueSt.pop();
    } else if (curString.equals("AssignS->id = Expr ;")) {
      String Expr = valueSt.pop();
      String variable = valueSt.pop();
      movii(Expr, variable);
      curTable.clearInnerVarCtn();

    } else if (curString.equals("AssignS->id [ Expr ] = Expr ;")) {
      leftArrAsign();// 内部pop3次
      curTable.clearInnerVarCtn();
    } else if (curString.equals("Expr->Expr + Expr1")) {
      String Expr1 = valueSt.pop();
      String Expr = valueSt.pop();
      arrithOp(Expr, '+', Expr1);

    } else if (curString.equals("Expr->Expr - Expr1")) {
      String Expr1 = valueSt.pop();
      String Expr = valueSt.pop();
      arrithOp(Expr, '-', Expr1);

    } else if (curString.equals("Expr->Expr1")) {

    } else if (curString.equals("Expr1->Expr1 * Expr2")) {
      String Expr1 = valueSt.pop();
      String Expr = valueSt.pop();
      arrithOp(Expr, '*', Expr1);

    } else if (curString.equals("Expr1->Expr1 / Expr2")) {
      String Expr1 = valueSt.pop();
      String Expr = valueSt.pop();
      arrithOp(Expr, '/', Expr1);

    } else if (curString.equals("Expr1->Expr2")) {

    } else if (curString.equals("Expr2->( Expr )")) {

    } else if (curString.equals("Expr2->id")) {

    } else if (curString.equals("Expr2->Const")) {

    } else if (curString.equals("Expr2->id [ Expr ]")) {
      Expr2_Arr();
    } else if (curString.equals("Expr2->id ( M2 CallParaList )")) {
      Expr2_Func();
    } else if (curString.equals("BoolE->BoolE1")) {

    } else if (curString.equals("BoolE->BoolE || M3 BoolE1")) {
      ArrayList<Integer> b2FalseList = trueListST.pop();
      ArrayList<Integer> b1FalseList = trueListST.pop();
      curTable.backpatch(b1FalseList, labelSt.peek(), LabelMap.get(labelSt.pop()));
      trueListST.push(merge(trueListST.pop(), trueListST.pop()));
      falseListST.push(b2FalseList);

    } else if (curString.equals("BoolE1->BoolE2")) {

    } else if (curString.equals("BoolE1->BoolE1 && M3 BoolE2")) {
      ArrayList<Integer> b2TrueList = trueListST.pop();
      ArrayList<Integer> b1TrueList = trueListST.pop();
      curTable.backpatch(b1TrueList, labelSt.peek(), LabelMap.get(labelSt.pop()));
      trueListST.push(b2TrueList);
      falseListST.push(merge(falseListST.pop(), falseListST.pop()));
    } else if (curString.equals("BoolE2->BoolE3")) {

    } else if (curString.equals("BoolE2->! BoolE3")) {
      ArrayList<Integer> trueList = trueListST.pop();
      trueListST.push(falseListST.pop());
      falseListST.push(trueList);
    } else if (curString.equals("BoolE3->( BoolE )")) {

    } else if (curString.equals("BoolE3->Expr Condop Expr")) {
      BEopE();
    } else if (curString.equals("ifS->if ( BoolE ) { M3 ifSen }")) {
      curTable.backpatch(trueListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      nextListST.push(merge(nextListST.pop(), falseListST.pop()));
    } else if (curString.equals("ifS->if ( BoolE ) { M3 ifSen N0 } else { M3 ifSen }")) {
      String mLastLabel = labelSt.pop();
      ArrayList<Integer> s2nextList = nextListST.pop();
      ArrayList<Integer> nnextList = nextListST.pop();
      curTable.backpatch(trueListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      curTable.backpatch(falseListST.pop(), mLastLabel, LabelMap.get(mLastLabel));
      nextListST.push(merge(nextListST.pop(), merge(nnextList, s2nextList)));

    } else if (curString.equals("ifS->if ( BoolE ) { M3 ifSen N0 } else M3 ifS")) {
      String mLastLabel = labelSt.pop();
      ArrayList<Integer> s2nextList = nextListST.pop();
      ArrayList<Integer> nnextList = nextListST.pop();
      curTable.backpatch(trueListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      curTable.backpatch(falseListST.pop(), mLastLabel, LabelMap.get(mLastLabel));
      nextListST.push(merge(nextListST.pop(), merge(nnextList, s2nextList)));

    } else if (curString.equals("ifSen->Sen")) {

    } else if (curString.equals("ifSen->Sen return Expr ;")) {
      curTable.clearInnerVarCtn();
    } else if (curString.equals("whileS->while ( M3 BoolE ) { M3 CirSen }")) {
      String m2Label = labelSt.pop();
      String m1Label = labelSt.pop();
      curTable.backpatch(nextListST.pop(), m1Label, LabelMap.get(m1Label));
      curTable.backpatch(trueListST.pop(), m2Label, LabelMap.get(m2Label));
      nextListST.push(falseListST.pop());
      curTable.addSentence("    jmp " + m1Label);

    } else if (curString.equals("CallS->id ( M2 CallParaList ) ;")) {
      curTable.addSentence("    call _" + valueSt.pop() + "\n\n");
      curTable.updateMaxParaSize(paraSize.peek());
      paraSize.pop();
      curTable.clearInnerVarCtn();

    } else if (curString.equals("CirSen->DeclareS CirSen")) {

    } else if (curString.equals("CirSen->AssignS M3 CirSen")) {
      nextListST.push(new ArrayList<>());
    } else if (curString.equals("CirSen->CirifS M3 CirSen")) {
      ArrayList<Integer> s2NextList = nextListST.pop();
      curTable.backpatch(nextListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      nextListST.push(s2NextList);
    } else if (curString.equals("CirSen->whileS M3 CirSen")) {
      ArrayList<Integer> s2NextList = nextListST.pop();
      curTable.backpatch(nextListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      nextListST.push(s2NextList);
    } else if (curString.equals("CirSen->return Expr ;")) {
      curTable.clearInnerVarCtn();
    } else if (curString.equals("CirSen->break ;")) {

    } else if (curString.equals("CirSen->continue ;")) {

    } else if (curString.equals("CirSen->CallS CirSen")) {

    } else if (curString.equals("CirSen->@null")) {

    } else if (curString.equals("CirifS->if ( BoolE ) { M3 CirSen }")) {
      curTable.backpatch(trueListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      nextListST.push(merge(nextListST.pop(), falseListST.pop()));
    } else if (curString.equals("CirifS->if ( BoolE ) { M3 CirSen N0 } else { M3 CirSen }")) {
      String mLastLabel = labelSt.pop();
      ArrayList<Integer> s2nextList = nextListST.pop();
      ArrayList<Integer> nnextList = nextListST.pop();
      curTable.backpatch(trueListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      curTable.backpatch(falseListST.pop(), mLastLabel, LabelMap.get(mLastLabel));
      nextListST.push(merge(nextListST.pop(), merge(nnextList, s2nextList)));

    } else if (curString.equals("CirifS->if ( BoolE ) { M3 CirSen N0 } else M3 CirifS")) {
      String mLastLabel = labelSt.pop();
      ArrayList<Integer> s2nextList = nextListST.pop();
      ArrayList<Integer> nnextList = nextListST.pop();
      curTable.backpatch(trueListST.pop(), labelSt.peek(), LabelMap.get(labelSt.pop()));
      curTable.backpatch(falseListST.pop(), mLastLabel, LabelMap.get(mLastLabel));
      nextListST.push(merge(nextListST.pop(), merge(nnextList, s2nextList)));

    } else if (curString.equals("Condop->>")) {
      valueSt.push(">");
    } else if (curString.equals("Condop->>=")) {
      valueSt.push(">=");
    } else if (curString.equals("Condop-><")) {
      valueSt.push("<");
    } else if (curString.equals("Condop-><=")) {
      valueSt.push("<=");
    } else if (curString.equals("Condop->==")) {
      valueSt.push("==");
    } else if (curString.equals("Condop->!=")) {
      valueSt.push("!=");
    } else if (curString.equals("Const->intConst")) {
      int offset = globalSignTable.getPramOffset();
      if (offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        int align = 4 - offset % 4;
        globalSignTable.addPramOffset(align);
        dataSecBuff.append(" .space " + align + "#碎片填充  \n");
      }
      String tmpID = "__" + constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "intConst", globalSignTable.getPramOffset(), -1, null, true);
      globalSignTable.addPramOffset(4);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .int " + valueSt.pop() + "\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->stringConst")) {
      String tmpID = "__" + constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "charArray", globalSignTable.getPramOffset(), -1, null, true);
      globalSignTable.addPramOffset(valueSt.peek().length());// 原来是length+1
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .asciz \"" + valueSt.pop() + "\"\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->floatConst")) {
      int offset = globalSignTable.getPramOffset();
      if (offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        int align = 4 - offset % 4;
        globalSignTable.addPramOffset(align);
        dataSecBuff.append(" .space " + align + "#碎片填充  \n");
      }
      String tmpID = "__" + constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "floatConst", globalSignTable.getPramOffset(), -1, null, true);
      globalSignTable.addPramOffset(4);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .float " + valueSt.pop() + "\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->charConst")) {
      String tmpID = "__" + constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "charConst", globalSignTable.getPramOffset(), -1, null, true);
      globalSignTable.addPramOffset(1);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .byte '" + valueSt.pop() + "'\n");
      valueSt.push(tmpID);

    } else if (curString.equals("Type->int")) {

    } else if (curString.equals("Type->float")) {

    } else if (curString.equals("Type->char")) {

    } else if (curString.equals("M0->@null")) {
      if (globalSignTable.isFuncRepeate("main")) {
        System.err.println("重复定义main函数");
      }
      curTable = new SignTable();
      curTable.setParentTable(globalSignTable);
      Sign tmpSign = new Sign("main", "int", -1, -1, curTable, true);// 函数不需要记录offset
      globalSignTable.add(tmpSign);
      typeSt.pop();

    } else if (curString.equals("M1->@null")) {
      if (globalSignTable.isFuncRepeate(valueSt.peek())) {
        System.err.println("重复定义" + valueSt.peek() + "函数");
      }
      curTable = new SignTable();
      curTable.setParentTable(globalSignTable);
      Sign tmpSign = new Sign(valueSt.peek(), typeSt.peek(), -1, -1, curTable, true);
      globalSignTable.add(tmpSign);

    } else if (curString.equals("M2->@null")) {// 调用函数前确认是否为printf
      paraSize.push(0);
      curCallFunc = valueSt.peek();
      curTable.addSentence("#调用函数:" + curCallFunc + "\n");
    } else if (curString.equals("M3->@null")) {
      LabelMap.put("L" + LabelCtn, curTable.getIndexOfLastSen());
      labelSt.push("L" + LabelCtn);
      LabelCtn++;
    } else if (curString.equals("M4->@null")) {

    } else if (curString.equals("M5->@null")) {

    } else if (curString.equals("M6->@null")) {

    } else if (curString.equals("M7->@null")) {

    } else if (curString.equals("N0->@null")) {
      nextListST.push(new ArrayList<>(curTable.getIndexOfLastSen()));
    }
  }

  public Stack<String> getTypeSt() {
    return typeSt;
  }

  public Stack<String> getValueSt() {
    return valueSt;
  }

  /*
   * srcE和desE要在符号表中才能用
   * 
   * *并且都是简单变量
   */
  private void movii(String srcE, String desE) {
    Sign srcSign = curTable.getSignByIdName(srcE);
    Sign desSign = curTable.getSignByIdName(desE);
    if (srcSign == null) {
      System.err.println("使用未定义变量：" + srcE);

    }
    if (desSign == null) {
      System.err.println("使用未定义变量：" + desE);
      return;
    }

    // 源变量->EAX
    if (srcSign.isGlobal) {// 全局变量
      switch (srcSign.type) {
        case "char":
        case "charConst":
          curTable.addSentence("    movb " + srcSign.id + ",%al\n");
          break;
        default:
          curTable.addSentence("    movl " + srcSign.id + ",%eax\n");
          break;
      }
    } else {// 局部变量或参数
      switch (srcSign.type) {
        case "char":
          curTable.addSentence("    movb " + srcSign.offset + "(%ebp),%al\n");
          break;
        default:
          curTable.addSentence("    movl " + srcSign.offset + "(%ebp),%eax\n");
          break;
      }
    }
    // EAX->目的变量
    if (desSign.isGlobal) {
      switch (desSign.type) {
        case "char":
          curTable.addSentence("    movb %al," + desSign.id + "\n");
          break;
        default:
          curTable.addSentence("    movl %eax," + desSign.id + "\n");
          break;
      }
    } else {// 局部变量或参数
      switch (desSign.type) {
        case "char":
          curTable.addSentence("    movb %al," + desSign.offset + "(%ebp)\n");
          break;
        default:
          curTable.addSentence("    movl %eax," + desSign.offset + "(%ebp)\n");
          break;
      }
    }
  }

  /*
   * 左值是符号表中的值，右值可直接拼接的表达式
   */
  private void movir(String srcE, String desE) {
    Sign srcSign = curTable.getSignByIdName(srcE);
    if (srcSign.isGlobal) {
      switch (srcSign.type) {
        case "char":
        case "charConst":
          curTable.addSentence("    movb " + srcSign.id + ",%al\n");
          curTable.addSentence("    movb %al," + desE + "\n");
          break;
        case "int":
        case "intConst":
        case "float":
        case "floatConst":
          curTable.addSentence("    movl " + srcSign.id + ",%eax\n");
          curTable.addSentence("    movl %eax," + desE + "\n");
          break;
        case "charArray":
        case "intArray":
        case "floatArray":
          curTable.addSentence("    movl $" + srcSign.id + "," + desE + "\n");
          break;
        default:
          break;
      }
    } else {
      switch (srcSign.type) {
        case "char":
          curTable.addSentence("    movb " + srcSign.offset + "(%ebp),%al\n");
          curTable.addSentence("    movb %al," + desE + "\n");
          break;
        case "int":
        case "float":
          curTable.addSentence("    movl " + srcSign.offset + "(%ebp),%eax\n");
          curTable.addSentence("    movl %eax," + desE + "\n");
          break;
        case "charArray":
        case "intArray":
        case "floatArray":
          curTable.addSentence("    leal " + srcSign.offset + "(%ebp),%eax\n");
          curTable.addSentence("    movl %eax," + desE + "\n");
          break;
        default:
          break;
      }
    }
  };

  /*
   * 左值是直接拼接的表达式,右值是符号表中的值
   */
  private void movri(String srcE, String desE) {
    Sign desSign = curTable.getSignByIdName(desE);
    if (desSign.isGlobal) {
      switch (desSign.type) {
        case "char":
          curTable.addSentence("    movb " + srcE + ",%al\n");
          curTable.addSentence("    movb %al," + desSign.id + "\n");
          break;
        case "int":
        case "float":
          curTable.addSentence("    movl " + srcE + ",%eax\n");
          curTable.addSentence("    movl %eax," + desSign.id + "\n");
          break;
        case "intArray":
        case "floatArray":
        case "charArray":
          // des是数组，不应该被赋值
          System.err.println("数组" + desSign.id + "不应该被赋值");
          break;
        default:
          break;
      }
    } else {
      switch (desSign.type) {
        case "char":
          curTable.addSentence("    movb " + srcE + ",%al\n");
          curTable.addSentence("    movb %al," + desSign.offset + "(%ebp)\n");
          break;
        case "int":
        case "float":
          curTable.addSentence("    movl " + srcE + ",%eax\n");
          curTable.addSentence("    movl %eax," + desSign.offset + "(%ebp)\n");
          break;
        case "intArray":
        case "floatArray":
        case "charArray":
          // des是数组，不应该被赋值
          System.err.println("数组" + desSign.id + "不应该被赋值");
          break;
        default:
          break;
      }
    }
  };

  private void declareSimVar() {
    int sizeOFid = typeSt.peek().equals("char") ? 1 : 4;
    if (curTable == globalSignTable) {// 全局变量
      int offset = globalSignTable.getPramOffset();
      if (sizeOFid == 4 && offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        int chip = 4 - offset % 4;
        globalSignTable.addPramOffset(chip);
        dataSecBuff.append(" .space " + chip + "#碎片填充  \n");
      }
      Sign tmpSign = new Sign(valueSt.peek(), typeSt.peek(), globalSignTable.getPramOffset(), -1,
          null, true);
      globalSignTable.addPramOffset(sizeOFid);
      globalSignTable.add(tmpSign);
      dataSecBuff.append(tmpSign.id + ": .space " + sizeOFid + "\n");
    } else {// 局部变量
      int offset = -curTable.getLocalOffset();
      if (sizeOFid == 4 && offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        curTable.addLocalOffset(4 - offset % 4);
      }
      Sign tmpSign = new Sign(valueSt.peek(), typeSt.peek(), curTable.getLocalOffset(), -1, null,
          false);
      curTable.addLocalOffset(sizeOFid);
      curTable.add(tmpSign);
    }
  }

  private void declareArr() {
    int arrayLen = Integer.valueOf(valueSt.pop());
    int sizeOFid = typeSt.peek().equals("char") ? 1 : 4;
    if (curTable == globalSignTable) {// 全局变量
      int offset = globalSignTable.getPramOffset();
      if (sizeOFid == 4 && offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        int chip = 4 - offset % 4;
        globalSignTable.addPramOffset(chip);
        dataSecBuff.append(" .space " + chip + "#碎片填充  \n");
      }
      Sign tmpSign = new Sign(valueSt.peek(), typeSt.peek() + "Array",
          globalSignTable.getPramOffset(), -1, null, true);
      globalSignTable.addPramOffset(sizeOFid * arrayLen);
      globalSignTable.add(tmpSign);
      dataSecBuff.append(tmpSign.id + ": .space " + arrayLen * sizeOFid + "\n");
    } else {// 局部变量
      int offset = -curTable.getLocalOffset();
      if (sizeOFid == 4 && offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        curTable.addLocalOffset(4 - offset % 4);
      }
      Sign tmpSign = new Sign(valueSt.peek(), typeSt.peek() + "Array", curTable.getLocalOffset(),
          -1, null, false);
      curTable.addLocalOffset(sizeOFid * arrayLen);
      curTable.add(tmpSign);
    }
  }

  private void Expr2_Arr() {// Expr2->id [ Expr ] (右值)
    String indexName = valueSt.pop();
    String idName = valueSt.pop();
    // Sign exprSign = curTable.getSignByIdName(exprName);
    Sign arrSign = curTable.getSignByIdName(idName);
    String innerID = "__inner" + curTable.getInnerVarCtn();
    curTable.addInnerVarCtn();
    String type = arrSign.type.substring(0, arrSign.type.length() - 5);
    int offset = -curTable.getLocalOffset();
    if (offset % 4 != 0) {
      // 如果之前变量是char，进行4字节对齐
      curTable.addLocalOffset(4 - offset % 4);
    }
    Sign innerSign = new Sign(innerID, type, curTable.getLocalOffset(), 0, null, false);
    curTable.addLocalOffset(4);
    curTable.add(innerSign);
    movir(indexName, "%ebx");// ebx存索引值
    if (arrSign.isGlobal) {
      switch (arrSign.type) {
        case "charArray":
          curTable.addSentence("    movb " + arrSign.id + "(,%ebx,1),%al\n");
          movri("%al", innerID);
          break;
        case "intArray":
        case "floatArray":
          curTable.addSentence("    movl " + arrSign.id + "(,%ebx,4),%eax\n");
          movri("%eax", innerID);
          break;
        default:
          break;
      }
    } else {
      switch (arrSign.type) {
        case "charArray":
          curTable.addSentence("    leal " + arrSign.offset + "(%ebp),%edx\n");
          curTable.addSentence("    movb (%edx,%ebx,1),%al\n");
          movri("%al", innerID);
          break;
        case "intArray":
        case "floatArray":
          curTable.addSentence("    leal " + arrSign.offset + "(%ebp),%edx\n");
          curTable.addSentence("    movl (%edx,%ebx,4),%eax\n");
          movri("%eax", innerID);
          break;
        default:
          break;
      }
    }
    valueSt.push(innerID);
  }

  private void Expr2_Func() {// Expr2->id ( M2 CallParaList )
    curTable.updateMaxParaSize(paraSize.pop());
    String funcName = valueSt.pop();
    Sign funcSign = curTable.getSignByIdName(funcName);
    int offset = -curTable.getLocalOffset();
    if (offset % 4 != 0) {
      // 如果之前变量是char，进行4字节对齐
      curTable.addLocalOffset(4 - offset % 4);
    }
    curTable.addSentence("    call _" + funcName + "\n");
    curTable.addSentence("    movl %eax," + curTable.getLocalOffset() + "(%ebp)\n\n");
    String innerVar = "__inner" + curTable.getInnerVarCtn();
    curTable.addInnerVarCtn();
    Sign innerSign = new Sign(innerVar, funcSign.type, curTable.getLocalOffset(), -1, null, false);
    curTable.add(innerSign);
    curTable.addLocalOffset(4);
    valueSt.push(innerVar);
  }

  private void arrithOp(String Expr, char op, String Expr1) {
    Sign aSign = curTable.getSignByIdName(Expr);
    Sign bSign = curTable.getSignByIdName(Expr1);
    String innerType = null;
    String innerID = "__inner" + curTable.getInnerVarCtn();
    curTable.addInnerVarCtn();
    int offset = -curTable.getLocalOffset();
    if (offset % 4 != 0) {
      // 如果之前变量是char，进行4字节对齐
      curTable.addLocalOffset(4 - offset % 4);
    }
    if (op == '+') {
      if (aSign.type.indexOf("float") >= 0) {// 浮点数
        innerType = "float";
        if (bSign.type.indexOf("char") >= 0) {
          System.err.println("不支持float与char运算\n");
          return;
        } else if (bSign.type.indexOf("int") >= 0) {// float+int
          if (aSign.isGlobal) {
            curTable.addSentence("    flds " + aSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + aSign.offset + "(%ebp)\n");
          }
          if (bSign.isGlobal) {
            curTable.addSentence("    fiadd " + bSign.id + "\n");
          } else {
            curTable.addSentence("    fiadd " + bSign.offset + "(%ebp)\n");
          }
          curTable.addSentence("    fstps " + curTable.getLocalOffset() + "(%ebp)\n");
        } else {// float+float
          if (aSign.isGlobal) {
            curTable.addSentence("    flds " + aSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + aSign.offset + "(%ebp)\n");
          }
          if (bSign.isGlobal) {
            curTable.addSentence("    flds " + bSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + bSign.offset + "(%ebp)\n");
          }
          curTable.addSentence("    faddp\n");
          curTable.addSentence("    fstps " + curTable.getLocalOffset() + "(%ebp)\n");
        }
      } else if (aSign.type.indexOf("int") >= 0) {// int
        innerType = "int";
        if (bSign.type.indexOf("char") >= 0) {
          System.err.println("不支持int与char运算\n");
          return;
        } else if (bSign.type.indexOf("int") >= 0) {// int + int
          if (aSign.isGlobal) {
            curTable.addSentence("    movl " + aSign.id + ",%eax\n");
          } else {
            curTable.addSentence("    movl " + aSign.offset + "(%ebp),%eax\n");
          }
          if (bSign.isGlobal) {
            curTable.addSentence("    movl " + bSign.id + ",%ebx\n");
          } else {
            curTable.addSentence("    movl " + bSign.offset + "(%ebp),%ebx\n");
          }
          curTable.addSentence("    add %ebx,%eax\n");
          curTable.addSentence("    movl %eax," + curTable.getLocalOffset() + "(%ebp)\n");
        }
      } else {
        System.err.println("不支持char和" + bSign.type + "+运算 ");
      }

    } else if (op == '-') {
      if (aSign.type.indexOf("float") >= 0) {// 浮点数
        innerType = "float";
        if (bSign.type.indexOf("char") >= 0) {
          System.err.println("不支持float与char  -运算\n");
          return;
        } else if (bSign.type.indexOf("int") >= 0) {// float-int
          if (aSign.isGlobal) {
            curTable.addSentence("    flds " + aSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + aSign.offset + "(%ebp)\n");
          }
          if (bSign.isGlobal) {
            curTable.addSentence("    fisub " + bSign.id + "\n");
          } else {
            curTable.addSentence("    fisub " + bSign.offset + "(%ebp)\n");
          }
          curTable.addSentence("    fstps " + curTable.getLocalOffset() + "(%ebp)\n");
        } else {// float-float
          if (bSign.isGlobal) {
            curTable.addSentence("    flds " + bSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + bSign.offset + "(%ebp)\n");
          }
          if (aSign.isGlobal) {
            curTable.addSentence("    flds " + aSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + aSign.offset + "(%ebp)\n");
          }
          curTable.addSentence("    fsubp \n");
          curTable.addSentence("    fstps " + curTable.getLocalOffset() + "(%ebp)\n");
        }
      } else if (aSign.type.indexOf("int") >= 0) {// int
        innerType = "int";
        if (bSign.type.indexOf("char") >= 0) {
          System.err.println("不支持int与char运算\n");
          return;
        } else if (bSign.type.indexOf("int") >= 0) {// int - int
          if (aSign.isGlobal) {
            curTable.addSentence("    movl " + aSign.id + ",%eax\n");
          } else {
            curTable.addSentence("    movl " + aSign.offset + "(%ebp),%eax\n");
          }
          if (bSign.isGlobal) {
            curTable.addSentence("    movl " + bSign.id + ",%ebx\n");
          } else {
            curTable.addSentence("    movl " + bSign.offset + "(%ebp),%ebx\n");
          }
          curTable.addSentence("    sub %ebx,%eax\n");
          curTable.addSentence("    movl %eax," + curTable.getLocalOffset() + "(%ebp)\n");
        } else {// int - float
          innerType = "float";
          if (bSign.isGlobal) {
            curTable.addSentence("    flds " + bSign.id + "\n");
          } else {
            curTable.addSentence("    flds " + bSign.offset + "(%ebp)\n");
          }
          if (aSign.isGlobal) {
            curTable.addSentence("    fild " + aSign.id + "\n");
          } else {
            curTable.addSentence("    fild " + aSign.offset + "(%ebp)\n");
          }
          curTable.addSentence("    fsubp\n");
          curTable.addSentence("    fstps " + curTable.getLocalOffset() + "(%ebp)\n");
        }
      } else if (bSign.type.indexOf("char") >= 0) {// char-char
        innerType = "char";
        if (aSign.isGlobal) {
          curTable.addSentence("    movb " + aSign.id + ",%al\n");
        } else {
          curTable.addSentence("    movb " + aSign.offset + "(%ebp),%al\n");
        }
        if (bSign.isGlobal) {
          curTable.addSentence("    movb " + bSign.id + ",%bl\n");
        } else {
          curTable.addSentence("    movb " + bSign.offset + "(%ebp),%bl\n");
        }
        curTable.addSentence("    sub %al,%bl\n");
        curTable.addSentence("    movb %al," + curTable.getLocalOffset() + "(%ebp)\n");
      } else {
        System.err.println("不支持char和" + bSign.type + "+运算 ");
      }
    } else if (op == '*') {

    } else if (op == '/') {

    }
    Sign innerSign = new Sign(innerID, innerType, curTable.getLocalOffset(), -1, null, false);
    curTable.add(innerSign);
    curTable.addLocalOffset(4);
    valueSt.push(innerID);
  }

  private void openExe(String param) {
    final Runtime runtime = Runtime.getRuntime();
    Process process = null;
    System.out.println(param);
    try {
      process = runtime.exec(param);

    } catch (final IOException e) {
      e.printStackTrace();
    }
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    byte[] out = new byte[4096];
    OutputStream exeout = process.getOutputStream();
    try {
      exeout.write(out, 0, 4096);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.err.println(new String(out));
  }

  private void leftArrAsign() {// AssignS->id [ Expr ] = Expr ;
    String rightExpr = valueSt.pop();
    String index = valueSt.pop();
    String arrName = valueSt.pop();
    Sign rightSign = curTable.getSignByIdName(rightExpr);
    Sign indexSign = curTable.getSignByIdName(index);
    Sign arrSign = curTable.getSignByIdName(arrName);
    int sizeOFid = (arrSign.type.indexOf("char") >= 0) ? 1 : 4;
    if (indexSign.isGlobal) {
      curTable.addSentence("    movl " + indexSign.id + ",%esi\n");
    } else {
      curTable.addSentence("    movl " + indexSign.offset + "(%ebp),%esi\n");
    }
    if (arrSign.isGlobal) {
      curTable.addSentence("    leal " + arrSign.id + "(,%esi," + sizeOFid + "),%ebx\n");
    } else {
      curTable.addSentence("    leal " + arrSign.offset + "(%ebp,%esi," + sizeOFid + "),%ebx\n");
    }
    if (sizeOFid > 1) {
      movir(rightExpr, "%eax");
    } else {
      movir(rightExpr, "%al");
    }
    switch (arrSign.type) {
      case "charArray":
        curTable.addSentence("    movb %al,(%ebx)\n");
        break;
      case "intArray":
      case "floatArray":
        curTable.addSentence("    movl %eax,(%ebx)\n");
        break;
      default:
        break;
    }
  }

  private void BEopE() {// BoolE3->Expr Condop Expr
    String bExpr = valueSt.pop();
    String op = valueSt.pop();
    String aExpr = valueSt.pop();
    Sign aSign = curTable.getSignByIdName(aExpr);
    Sign bSign = curTable.getSignByIdName(bExpr);
    String opType = null;
    if (aSign.type.indexOf("float") >= 0) {
      opType = "float";
      if (bSign.type.indexOf("char") >= 0) {
        System.err.println("float 和 char不可比较！");
      } else if (bSign.type.indexOf("int") >= 0) {// float op int
        if (aSign.isGlobal) {
          curTable.addSentence("    flds " + aSign.id + "\n");
        } else {
          curTable.addSentence("    flds " + aSign.offset + "(%ebp)\n");
        }
        if (bSign.isGlobal) {
          curTable.addSentence("    ficomp " + bSign.id + "\n");
        } else {
          curTable.addSentence("    ficomp " + bSign.offset + "(%ebp)\n");
        }
        curTable.addSentence("    fnstsw %ax\n    sahf\n");
      } else {// float op float
        if (aSign.isGlobal) {
          curTable.addSentence("    flds " + aSign.id + "\n");
        } else {
          curTable.addSentence("    flds " + aSign.offset + "(%ebp)\n");
        }
        if (bSign.isGlobal) {
          curTable.addSentence("    fcomp " + bSign.id + "\n");
        } else {
          curTable.addSentence("    fcomp " + bSign.offset + "(%ebp)\n");
        }
        curTable.addSentence("    fnstsw %ax\n    sahf\n");
      }
    } else if (aSign.type.indexOf("int") >= 0) {// int
      opType = "int";
      if (bSign.type.indexOf("char") >= 0) {
        System.err.println("int 和 char不可比较！");
      } else if (bSign.type.indexOf("float") >= 0) {// int op float
        opType = "float";
        if (aSign.isGlobal) {
          curTable.addSentence("    fild " + aSign.id + "\n");
        } else {
          curTable.addSentence("    fild " + aSign.offset + "(%ebp)\n");
        }
        if (bSign.isGlobal) {
          curTable.addSentence("    fcomp " + bSign.id + "\n");
        } else {
          curTable.addSentence("    fcomp " + bSign.offset + "(%ebp)\n");
        }
        curTable.addSentence("    fnstsw %ax\n    sahf\n");
      } else {// int op int
        if (aSign.isGlobal) {
          curTable.addSentence("    movl " + aSign.id + ",%eax\n");
        } else {
          curTable.addSentence("    movl " + aSign.offset + "(%ebp),%eax\n");
        }
        if (bSign.isGlobal) {
          curTable.addSentence("    movl " + bSign.id + ",%ebx\n");
        } else {
          curTable.addSentence("    movl " + bSign.offset + "(%ebp),%ebx\n");
        }
        curTable.addSentence("    cmp %ebx,%eax\n");
      }
    } else {
      opType = "char";
      if (bSign.type.indexOf("char") < 0) {
        System.err.println("不支持char和" + bSign.type + "比较");
      } else {
        if (aSign.isGlobal) {
          curTable.addSentence("    movb " + aSign.id + ",%al\n");
        } else {
          curTable.addSentence("    movb " + aSign.offset + "(%ebp),%al\n");
        }
        if (bSign.isGlobal) {
          curTable.addSentence("    movb " + bSign.id + ",%bl\n");
        } else {
          curTable.addSentence("    movb " + bSign.offset + "(%ebp),%bl\n");
        }
        curTable.addSentence("    cmp %bl,%al\n");
      }
    }

    if (opType.equals("float")) {// float 基于无符号的跳转指令
      switch (op) {
        case ">":
          curTable.addSentence("    JA ");
          break;
        case ">=":
          curTable.addSentence("    JAE ");
          break;
        case "<":
          curTable.addSentence("    JB ");
          break;
        case "<=":
          curTable.addSentence("    JBE ");
          break;
        case "==":
          curTable.addSentence("    JE ");
          break;
        case "!=":
          curTable.addSentence("    JNE ");
          break;
        default:
          break;
      }
    } else {// int 和 char 基于有符号的比较
      switch (op) {
        case ">":
          curTable.addSentence("    JG ");
          break;
        case ">=":
          curTable.addSentence("    JGE ");
          break;
        case "<":
          curTable.addSentence("    JL ");
          break;
        case "<=":
          curTable.addSentence("    JLE ");
          break;
        case "==":
          curTable.addSentence("    JE ");
          break;
        case "!=":
          curTable.addSentence("    JNE ");
          break;
        default:
          break;
      }
    }
    ArrayList<Integer> trueList = new ArrayList<>();
    ArrayList<Integer> falseList = new ArrayList<>();
    trueList.add(curTable.getIndexOfLastSen());
    curTable.addSentence("jmp ");
    falseList.add(curTable.getIndexOfLastSen());
    trueListST.push(trueList);
    falseListST.push(falseList);
  }

  private ArrayList<Integer> merge(ArrayList<Integer> lst1, ArrayList<Integer> lst2) {
    for (int x : lst1)
      lst2.add(x);
    return lst2;
  }

}
