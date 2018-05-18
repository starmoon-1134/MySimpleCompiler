package com.compiler;

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

  StringBuffer dataSecBuff;
  StringBuffer codeSecBuff;

  int constCtn = 0;

  private String[][] productions;

  // BufferedReader symbolReader;

  public SemanticAnalyzer(String[][] productions) throws FileNotFoundException
  {
    // TODO 自动生成的构造函数存根
    this.productions = productions;
    globalSignTable = new SignTable();
    curTable = globalSignTable;
    // curTable = globalSignTable;
    tableSt = new Stack<>();
    typeSt = new Stack<>();
    valueSt = new Stack<>();
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

    } else if (curString.equals("Start->MainDef")) {

    } else if (curString.equals("Start->FuncList MainDef")) {

    } else if (curString.equals("Start->DeclareS Start")) {

    } else if (curString.equals("MainDef->int main ( M0 ParaList ) { Sen return Expr ; }")) {
      codeSecBuff.append("\n.globl _main\n");
      codeSecBuff.append("_main\n");
      codeSecBuff.append("    pushl %ebx\n");
      codeSecBuff.append("    movl %esp, %ebp\n");

      String Expr = valueSt.pop();
      String[] posOfExpr = curTable.getOffsetOfID(Expr).split(":");
      if (posOfExpr[0].equals("-1")) {
        System.out.println("使用未定义变量：" + Expr);
      } else if (posOfExpr[0].equals("0")) {// 全局变量
        curTable.addSentence("    xor %ebx,%ebx\n");
        curTable.addSentence("    xor %eax,%eax\n");
        switch (posOfExpr[1]) {
          case "char":
            curTable.addSentence("    movb " + posOfExpr[2] + "(%ebx),%al\n");
            break;
          default:
            curTable.addSentence("    movl " + posOfExpr[2] + "(%ebx),%eax\n");
            break;
        }
      } else {// 局部变量
        switch (posOfExpr[1]) {
          case "char":
            curTable.addSentence("    movb " + posOfExpr[2] + "(%ebp),%al\n");
            break;
          default:
            curTable.addSentence("    movl " + posOfExpr[2] + "(%ebp),%eax\n");
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

    } else if (curString.equals("ParaList->@null")) {

    } else if (curString.equals("ParaList->Type ParaID")) {
      // Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getPramOffset(), -1, null);
      // curTable.addPramOffset(4);
      // curTable.add(tmpSign);
      // typeSt.pop();
    } else if (curString.equals("ParaList->Type ParaID , ParaList1")) {
      // Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getPramOffset(), -1, null);
      // curTable.addPramOffset(4);
      // curTable.add(tmpSign);
      // typeSt.pop();
    } else if (curString.equals("ParaList1->Type ParaID")) {
      // Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getPramOffset(), -1, null);
      // curTable.addPramOffset(4);
      // curTable.add(tmpSign);
      // typeSt.pop();
    } else if (curString.equals("ParaList1->Type ParaID , ParaList1")) {
      // Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getPramOffset(), -1, null);
      // curTable.addPramOffset(4);
      // curTable.add(tmpSign);
      // typeSt.pop();
    } else if (curString.equals("ParaID->id")) {
      Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getPramOffset(), -1, null);
      curTable.addPramOffset(4);
      curTable.add(tmpSign);
      typeSt.pop();
    } else if (curString.equals("Sen->DeclareS Sen")) {

    } else if (curString.equals("Sen->AssignS Sen")) {

    } else if (curString.equals("Sen->ifS Sen")) {

    } else if (curString.equals("Sen->whileS Sen")) {

    } else if (curString.equals("Sen->id ( CallParaList ) ; Sen")) {

    } else if (curString.equals("Sen->@null")) {

    } else if (curString.equals("CallParaList->@null")) {

    } else if (curString.equals("CallParaList->Expr")) {

    } else if (curString.equals("CallParaList->Expr , CallParaList1")) {

    } else if (curString.equals("CallParaList1->Expr")) {

    } else if (curString.equals("CallParaList1->Expr , CallParaList1")) {

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
      int offset = curTable.getLocalOffset();
      boolean isNeedAlign = false;
      if (sizeOFid == 4 && offset % 4 != 0) {
        // 如果之前变量是char，进行4字节对齐
        isNeedAlign = true;
        curTable.addLocalOffset(4 - offset % 4);
      }
      Sign tmpSign = new Sign(valueSt.pop(), typeSt.peek(), curTable.getLocalOffset(), -1, null);
      curTable.addLocalOffset(sizeOFid);
      curTable.add(tmpSign);
      // typeSt.pop(); 511
      if (curTable == globalSignTable) {
        switch (tmpSign.type) {
          case "char":
            if (!isNeedAlign)
              dataSecBuff.append(".byte '\\0','\\0','\\0','\\0'\n");
            break;
          case "int":
            dataSecBuff.append(".int 0\n");
            break;
          case "float":
            dataSecBuff.append(".float 0.0\n");
          default:
            break;
        }
      }
    } else if (curString.equals("ID2->id = Expr")) {

    } else if (curString.equals("ID3->id [ intConst ]")) {

    } else if (curString.equals("AssignS->id = Expr ;")) {// 暂把Expr当立即数
      String Expr = valueSt.pop();
      String variable = valueSt.pop();
      mov(Expr, variable);
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

    } else if (curString.equals("CirSen->DeclareS CirSen")) {

    } else if (curString.equals("CirSen->AssignS CirSen")) {

    } else if (curString.equals("CirSen->CirifS CirSen")) {

    } else if (curString.equals("CirSen->whileS CirSen")) {

    } else if (curString.equals("CirSen->return Expr ;")) {

    } else if (curString.equals("CirSen->break ;")) {

    } else if (curString.equals("CirSen->continue ;")) {

    } else if (curString.equals("CirSen->id ( CallParaList ) ; CirSen")) {

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
      String tmpID = constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "intConst", globalSignTable.getLocalOffset(), -1, null);
      globalSignTable.addLocalOffset(4);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .int " + valueSt.pop() + "\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->stringConst")) {
      String tmpID = constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "stringConst", globalSignTable.getLocalOffset(), -1, null);
      globalSignTable.addLocalOffset(valueSt.peek().length() + 1);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .asicz \"" + valueSt.pop() + "\"\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->floatConst")) {
      String tmpID = constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "floatConst", globalSignTable.getLocalOffset(), -1, null);
      globalSignTable.addLocalOffset(4);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .float " + valueSt.pop() + "\n");
      valueSt.push(tmpID);
    } else if (curString.equals("Const->charConst")) {
      String tmpID = constCtn + "_const";
      constCtn++;
      Sign sign = new Sign(tmpID, "charConst", globalSignTable.getLocalOffset(), -1, null);
      globalSignTable.addLocalOffset(1);
      globalSignTable.add(sign);
      dataSecBuff.append(tmpID + ": .byte '" + valueSt.pop() + "'\n");
      valueSt.push(tmpID);

    } else if (curString.equals("Type->int")) {
      // typeSt.push("int");
    } else if (curString.equals("Type->float")) {
      // typeSt.push("float");
    }
    // } else if (curString.equals("Type->string")) {
    // typeSt.push("string");
    else if (curString.equals("Type->char")) {
      // typeSt.push("char");
    }

    else if (curString.equals("M0->@null")) {
      curTable = new SignTable();
      curTable.setParentTable(globalSignTable);
      Sign tmpSign = new Sign("main", "int", -1, -1, curTable);// 函数不需要记录offset
      globalSignTable.add(tmpSign);
      typeSt.pop();
    } else if (curString.equals("M1->@null")) {
      curTable = new SignTable();
      curTable.setParentTable(globalSignTable);
      Sign tmpSign = new Sign(valueSt.pop(), typeSt.pop(), -1, -1, curTable);
      globalSignTable.add(tmpSign);
    } else if (curString.equals("M2->@null")) {

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
  private void mov(String srcE, String desE) {
    String[] PosofScr = curTable.getOffsetOfID(srcE).split(":");
    String[] PosofDes = curTable.getOffsetOfID(desE).split(":");
    if (PosofScr[0].equals("-1")) {
      System.out.println("使用未定义变量：" + PosofScr);

    }
    if (PosofDes[0].equals("-1")) {
      System.out.println("使用未定义变量：" + PosofDes);
      return;
    }

    boolean isBase0 = false;// ebx 做为基址寄存器，是否为0

    // 源变量->EAX
    if (PosofScr[0].equals("0")) {// 全局变量
      curTable.addSentence("    xor %ebx,%ebx\n");
      isBase0 = true;
      switch (PosofScr[1]) {
        case "char":
        case "charConst":
          curTable.addSentence("    xor %eax,%eax\n");
          curTable.addSentence("    movb " + PosofScr[2] + "(%ebx),%al\n");
          break;
        default:
          curTable.addSentence("    movl " + PosofScr[2] + "(%ebx),%eax\n");
          break;
      }
    } else {// 局部变量或参数
      switch (PosofScr[1]) {
        case "char":
          curTable.addSentence("    movb " + PosofScr[2] + "(%ebp),%al\n");
          break;
        default:
          curTable.addSentence("    movl " + PosofScr[2] + "(%ebp),%eax\n");
          break;
      }
    }
    // EAX->目的变量
    if (PosofDes[0].equals("0")) {
      if (!isBase0) {
        curTable.addSentence("    xor %ebx,%ebx\n");
      }
      switch (PosofDes[1]) {
        case "char":
          curTable.addSentence("    movb %al," + PosofDes[2] + "(%ebx)\n");
          break;
        default:
          curTable.addSentence("    movl %eax," + PosofDes[2] + "(%ebx)\n");
          break;
      }
    } else {// 局部变量或参数
      switch (PosofDes[1]) {
        case "char":
          curTable.addSentence("    movb %al," + PosofDes[2] + "(%ebp)\n");
          break;
        default:
          curTable.addSentence("    movl %eax" + PosofDes[2] + "(%ebp)\n");
          break;
      }
    }
  }

}
