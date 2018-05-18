package com.compiler;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class SemanticAnalyzer {
  SignTable globalSignTable;
  SignTable curTable;

  Stack<SignTable> tableSt;
  Stack<String> typeSt;
  Stack<String> valueSt;
  Stack<Integer> paraSize;// 一个函数对应2个值，下边是最大值，上边是当前值

  StringBuffer dataSecBuff;
  StringBuffer codeSecBuff;

  int constCtn = 0;
  boolean isPrintf = false;// 当前调用函数是否为printf
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
    // symbolReader = new BufferedReader(new FileReader(MainClass.symbolFileNameString));
    dataSecBuff = new StringBuffer(".section .data\n");
    codeSecBuff = new StringBuffer(".section .text\n");
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
      openExe("gcc " + MainClass.asmFileNameString + " -o " + MainClass.exeFileNameString);
      Desktop.getDesktop().open(new File(MainClass.exeFileNameString));
      // openExe("cmd /c start" + MainClass.exeFileNameString);

    } else if (curString.equals("Start->MainDef")) {

    } else if (curString.equals("Start->FuncList MainDef")) {

    } else if (curString.equals("Start->DeclareS Start")) {

    } else if (curString.equals("MainDef->int main ( M0 ParaList ) { Sen return Expr ; }")) {
      codeSecBuff.append("\n.globl _main\n");
      codeSecBuff.append("_main:\n");
      codeSecBuff.append("    pushl %ebx\n");
      codeSecBuff.append("    movl %esp, %ebp\n");
      codeSecBuff
          .append("    sub $" + (-curTable.getLocalOffset() + curTable.maxParaSize) + ",%esp\n");

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
      codeSecBuff.append("    pushl %ebx\n");
      codeSecBuff.append("    movl %esp, %ebp\n");
      codeSecBuff
          .append("    sub $" + (-curTable.getLocalOffset() + curTable.maxParaSize) + ",%esp\n");

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

    } else if (curString.equals("Sen->AssignS Sen")) {

    } else if (curString.equals("Sen->ifS Sen")) {

    } else if (curString.equals("Sen->whileS Sen")) {

    } else if (curString.equals("Sen->CallS ; Sen")) {

    } else if (curString.equals("Sen->@null")) {

    } else if (curString.equals("CallParaList->@null")) {

    } else if (curString.equals("CallParaList->CallExpr")) {

    } else if (curString.equals("CallParaList->CallExpr , CallParaList1")) {

    } else if (curString.equals("CallParaList1->CallExpr")) {

    } else if (curString.equals("CallParaList1->CallExpr , CallParaList1")) {

    } else if (curString.equals("CallExpr->Expr")) {
      String paraID = valueSt.pop();
      Sign paraSign = curTable.getSignByIdName(paraID);
      if (paraSign == null) {
        System.err.println("使用未定义变量:" + paraID);
        return;
      }
      if (isPrintf && paraSign.type.equals("float")) {
        if (paraSign.isGlobal) {
          curTable.addSentence("    flds " + paraSign.id + "\n");
        } else {
          curTable.addSentence("    flds " + paraSign.offset + "(%ebp)\n");
        }
        curTable.addSentence("    fstpl " + paraSize.peek() + "(%esp)\n");
        paraSize.push(paraSize.pop() + 8);
      } else {
        if (paraSign.isGlobal) {
          curTable.addSentence("    movl $" + paraSign.id + ",%ebx\n");
        } else {
          curTable.addSentence("    movl " + paraSign.offset + "(%ebp),%ebx\n");
        }
        if (paraSign.type.equals("stringConst")) {/////////////////////////////////////////////
          curTable.addSentence("    movl %ebx," + paraSize.peek() + "(%esp)\n");
        } else {
          curTable.addSentence("    movl (%ebx),%eax\n");
          curTable.addSentence("    movl %eax," + paraSize.peek() + "(%esp)\n");
        }
        paraSize.push(paraSize.pop() + 4);
      }

    } else if (curString.equals("DeclareS->Type IDlist ;")) {
      typeSt.pop();// 511
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
      int sizeOFid = typeSt.peek().equals("char") ? 1 : 4;

      if (curTable == globalSignTable) {// 全局变量
        int offset = globalSignTable.getPramOffset();
        if (sizeOFid == 4 && offset % 4 != 0) {
          // 如果之前变量是char，进行4字节对齐
          int chip = 4 - offset % 4;
          globalSignTable.addPramOffset(chip);
          dataSecBuff.append("     .byte '\\0'");
          for (int i = 1; i < chip; i++) {
            dataSecBuff.append(",'\\0'");
          }
          dataSecBuff.append("\n");
        }
        Sign tmpSign = new Sign("_" + valueSt.pop(), typeSt.peek(), globalSignTable.getPramOffset(),
            -1, null, true);
        globalSignTable.addPramOffset(sizeOFid);
        globalSignTable.add(tmpSign);
        switch (tmpSign.type) {
          case "char":
            dataSecBuff.append(tmpSign.id + ": .byte '\\0'\n");
            break;
          case "int":
            dataSecBuff.append(tmpSign.id + ": .int 0\n");
            break;
          case "float":
            dataSecBuff.append(tmpSign.id + ": .float 0.0\n");
          default:
            break;
        }
      } else {// 局部变量
        int offset = curTable.getLocalOffset();
        if (sizeOFid == 4 && offset % 4 != 0) {
          // 如果之前变量是char，进行4字节对齐
          curTable.addLocalOffset(4 - offset % 4);
        }
        Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getLocalOffset(), -1, null,
            false);
        curTable.addLocalOffset(sizeOFid);
        curTable.add(tmpSign);
      }
    } else if (curString.equals("ID2->id = Expr")) {

    } else if (curString.equals("ID3->id [ intConst ]")) {

    } else if (curString.equals("AssignS->id = Expr ;")) {
      String Expr = valueSt.pop();
      String variable = valueSt.pop();
      movii(Expr, variable);
    } else if (curString.equals("Expr->Expr + Expr1")) {

    } else if (curString.equals("Expr->Expr - Expr1")) {

    } else if (curString.equals("Expr->Expr1")) {

    } else if (curString.equals("Expr1->Expr1 * Expr2")) {

    } else if (curString.equals("Expr1->Expr1 / Expr2")) {

    } else if (curString.equals("Expr1->Expr2")) {

    } else if (curString.equals("Expr2->( Expr )")) {

    } else if (curString.equals("Expr2->id")) {

    } else if (curString.equals("Expr2->Const")) {

    } else if (curString.equals("Expr2->id [ Expr ]")) {

    } else if (curString.equals("Expr2->id ( M2 CallParaList )")) {

    } else if (curString.equals("BoolE->BoolE1")) {

    } else if (curString.equals("BoolE->BoolE || BoolE1")) {

    } else if (curString.equals("BoolE1->BoolE2")) {

    } else if (curString.equals("BoolE1->BoolE1 && BoolE2")) {

    } else if (curString.equals("BoolE2->BoolE3")) {

    } else if (curString.equals("BoolE2->! BoolE3")) {

    } else if (curString.equals("BoolE3->( BoolE )")) {

    } else if (curString.equals("BoolE3->Expr Condop Expr")) {

    } else if (curString.equals("ifS->if ( BoolE ) { ifSen }")) {

    } else if (curString.equals("ifS->if ( BoolE ) { ifSen } else { ifSen }")) {

    } else if (curString.equals("ifS->if ( BoolE ) { ifSen } else ifS")) {

    } else if (curString.equals("ifSen->Sen")) {

    } else if (curString.equals("ifSen->Sen return Expr ;")) {

    } else if (curString.equals("whileS->while ( BoolE ) { CirSen }")) {

    } else if (curString.equals("CallS->id ( M2 CallParaList ) ;")) {
      curTable.addSentence("    call _" + valueSt.pop() + "\n");
      if (curTable.maxParaSize < paraSize.peek()) {
        curTable.maxParaSize = paraSize.peek();
      }
      paraSize.pop();

    } else if (curString.equals("CirSen->DeclareS CirSen")) {

    } else if (curString.equals("CirSen->AssignS CirSen")) {

    } else if (curString.equals("CirSen->CirifS CirSen")) {

    } else if (curString.equals("CirSen->whileS CirSen")) {

    } else if (curString.equals("CirSen->return Expr ;")) {

    } else if (curString.equals("CirSen->break ;")) {

    } else if (curString.equals("CirSen->continue ;")) {

    } else if (curString.equals("CirSen->CallS CirSen")) {

    } else if (curString.equals("CirSen->@null")) {

    } else if (curString.equals("CirifS->if ( BoolE ) { CirSen }")) {

    } else if (curString.equals("CirifS->if ( BoolE ) { CirSen } else { CirSen }")) {

    } else if (curString.equals("CirifS->if ( BoolE ) { CirSen } else CirifS")) {

    } else if (curString.equals("Condop->>")) {

    } else if (curString.equals("Condop->>=")) {

    } else if (curString.equals("Condop-><")) {

    } else if (curString.equals("Condop-><=")) {

    } else if (curString.equals("Condop->=")) {

    } else if (curString.equals("Condop->==")) {

    } else if (curString.equals("Condop->!=")) {

    } else if (curString.equals("Const->intConst")) {
      int offset = globalSignTable.getPramOffset();
      if (offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        int align = 4 - offset % 4;
        globalSignTable.addPramOffset(align);
        dataSecBuff.append("   .byte '\\0'");
        for (int i = 1; i < align; i++)
          dataSecBuff.append(",'\\0'");
        dataSecBuff.append("\n");
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
      Sign sign = new Sign(tmpID, "stringConst", globalSignTable.getPramOffset(), -1, null, true);
      globalSignTable.addPramOffset(valueSt.peek().length() + 1);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .asciz \"" + valueSt.pop() + "\"\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->floatConst")) {
      int offset = globalSignTable.getPramOffset();
      if (offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        int align = 4 - offset % 4;
        globalSignTable.addPramOffset(align);
        dataSecBuff.append("   .byte '\\0'");
        for (int i = 1; i < align; i++)
          dataSecBuff.append(",'\\0'");
        dataSecBuff.append("\n");
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
      if (valueSt.peek().equals("printf")) {
        isPrintf = true;
      }
    } else if (curString.equals("M3->@null")) {

    } else if (curString.equals("M4->@null")) {

    } else if (curString.equals("M5->@null")) {

    } else if (curString.equals("M6->@null")) {

    } else if (curString.equals("M7->@null")) {

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
      curTable.addSentence("    movl $" + srcSign.id + ",%ebx\n");
      switch (srcSign.type) {
        case "char":
        case "charConst":
          curTable.addSentence("    movb (%ebx),%al\n");
          break;
        default:
          curTable.addSentence("    movl (%ebx),%eax\n");
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
      curTable.addSentence("    movl $" + desSign.id + ",%ebx\n");
      switch (desSign.type) {
        case "char":
          curTable.addSentence("    movb %al,(%ebx)\n");
          break;
        default:
          curTable.addSentence("    movl %eax,(%ebx)\n");
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

  private void movi(String srcE, String reg) {

  };

  private void movrm(String reg, String desE) {

  };

  private void openExe(String param) {
    final Runtime runtime = Runtime.getRuntime();
    Process process = null;
    System.out.println(param);
    try {
      process = runtime.exec(param);
      process.waitFor();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
