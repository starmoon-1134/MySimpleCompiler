package com.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class LexicalAnalyzer {
  static private final int bufferSize = 1024;
  static private Hashtable<String, Integer> codeOfKind = null;// 种别码
  static private ArrayList<String> symbolList = null;

  static public void getToken(String sourceFileName) throws IOException {
    String tokenFileNameString = sourceFileName.substring(0, sourceFileName.length() - 2)
        + "_token.txt";
    String symbolTableFileNameString = sourceFileName.substring(0, sourceFileName.length() - 2)
        + "_sym.txt";
    // StringBuffer token = new StringBuffer();
    // StringBuffer symbol = new StringBuffer();
    StringBuffer word = new StringBuffer();
    byte[] buffer = new byte[bufferSize];// 字符缓冲区
    char ch = 0;
    int len = 0;// 一次读取到的字符数
    int cur = 0;// 用于遍历字符缓冲区
    int curLine = 1;// 记录当前行

    // 文件操作
    File sourceFile = new File(sourceFileName);
    File tokenFile = new File(tokenFileNameString);
    File symbolFile = new File(symbolTableFileNameString);
    InputStream source = null;
    FileWriter tokenFileWriter = null;
    FileWriter symbolFileWriter = null;
    try {
      source = new FileInputStream(sourceFileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (!tokenFile.exists())
      tokenFile.createNewFile();
    if (!symbolFile.exists())
      symbolFile.createNewFile();
    tokenFileWriter = new FileWriter(tokenFileNameString);
    symbolFileWriter = new FileWriter(symbolTableFileNameString);

    initCodeOfKind();
    symbolList = new ArrayList<String>();
    {
      len = source.read(buffer);
      while (len > 0) {
        // 单词首字母
        for (cur = 0; cur < len;) {
          ch = (char) buffer[cur++];
          if (Character.isLetter(ch) || ch == '_') {
            /*-------------------------------------标识符或关键字--------------------------------*/
            word.setLength(0);
            word.append(ch);
            while (true) {
              if (cur >= len) {
                // 如果缓冲区剩余字符为0，再读一批
                len = source.read(buffer);
                if (len <= 0) {
                  // 没有读到新的字符
                  break;
                } else {
                  // 读取成功，重置缓冲区指针
                  cur = 0;
                }
              }
              ch = (char) buffer[cur++];
              if (Character.isLetterOrDigit(ch) || ch == '_') {
                word.append(ch);
              } else {
                cur--;
                break;
              }
            }
            if (word.length() > 63) {
              System.out.println("error:identifier is too long at Line:" + curLine);
              word.setLength(63);
            }
            if (codeOfKind.containsKey(word.toString())) {
              tokenFileWriter.write(word.toString() + ":_\n");
            } else {
              tokenFileWriter.write("id:" + word.toString() + "\n");
              symbolList.add(word.toString());
            }

          } else if (Character.isDigit(ch)) {
            /*----------------------------------int & float---------------------------*/
            int state = 0;// 0:int; 1:出现一个小数点可能会出错;2:float
            word.setLength(0);
            word.append(ch);
            while (true) {
              if (cur >= len) {
                // 如果缓冲区剩余字符为0，再读一批
                len = source.read(buffer);
                if (len <= 0) {
                  // 没有读到新的字符
                  if (state == 0) {
                    tokenFileWriter.write("intConst:" + word.toString() + "\n");
                    symbolList.add(word.toString());
                  } else if (state == 2) {
                    tokenFileWriter.write("floatConst:" + word.toString() + "\n");
                    symbolList.add(word.toString());
                  } else {
                    System.out.println("errer:invalid float at line" + curLine);
                  }
                  break;
                } else {
                  // 读取成功，重置缓冲区指针
                  cur = 0;
                }
              }
              ch = (char) buffer[cur++];
              if (ch == '.' && state == 0) {
                state = 1;
                word.append(ch);
              } else if (Character.isDigit(ch) && state == 1) {
                state = 2;
                word.append(ch);
              } else if (Character.isDigit(ch)) {
                word.append(ch);
              } else if (state == 0) {
                tokenFileWriter.write("intConst:" + word.toString() + "\n");
                symbolList.add(word.toString());
                cur--;
                break;
              } else if (state == 2) {
                tokenFileWriter.write("floatConst:" + word.toString() + "\n");
                symbolList.add(word.toString());
                cur--;
                break;
              } else {
                System.out.println("errer:invalid float at line" + curLine);
                cur--;
                break;// 3.29第二次实验课添加
              }
            }
          } else if (ch == '\'') {
            /*--------------------------------字符常量-------------------------------*/
            int state = 0;// 0:未读字符; 1:读了一个字符; 2:读了一个转移符; -1:无效转义符; -2:超过指定长度且未封闭
            word.setLength(0);
            while (true) {
              if (cur >= len) {
                // 如果缓冲区剩余字符为0，再读一批
                len = source.read(buffer);
                if (len <= 0) {
                  // 没有读到新的字符
                  System.out.println("errer:invalid char(unclosed) at line" + curLine);
                  break;
                } else {
                  // 读取成功，重置缓冲区指针
                  cur = 0;
                }
              }
              ch = (char) buffer[cur++];
              if (ch == '\n') {
                System.out.println("errer:invalid char(unclosed) at line" + curLine);
                curLine++;
                break;
              }
              if (ch != '\\' && state == 0) {
                if (ch == '\'') {// 空字符''的处理
                  tokenFileWriter.write("charConst:" + "\\0" + "\n");
                  symbolList.add("\\0");
                  break;
                }
                state = 1;
                word.append(ch);
              } else if (ch == '\\' && state == 0) {
                state = 2;
                word.append(ch);
              } else if (ch == '\'' && state == 1) {
                tokenFileWriter.write("charConst:" + word.toString() + "\n");
                symbolList.add(word.toString());
                break;
              } else if (ch != '\'' && state == 1) {
                state = -2;
              } else if (ch == '\'' && state == -2) {
                System.out.println("error:too long Const char at Line:" + curLine);
                break;
              } else if (state == 2) {
                switch (ch) {
                  case '\\':
                  case 't':
                  case '\'':
                  case '\"':
                  case 'n':
                  case '0':
                  case 'r':
                    word.append(ch);
                    state = 1;
                    break;
                  default:
                    System.out.println("error:invalid escape character at Line:" + curLine);
                    state = -2;
                    break;
                }
              }
              // if (state == -1) {
              // break;
              // }
            }
          } else if (ch == '\"') {
            /*-----------------------------------字符串常量-------------------------------*/
            int state = 1;// 1:正常状态; 2:读了一个转移符;
            word.setLength(0);
            while (true) {
              if (cur >= len) {
                // 如果缓冲区剩余字符为0，再读一批
                len = source.read(buffer);
                if (len <= 0) {
                  // 没有读到新的字符
                  System.out.println("errer:invalid string(unclosed) at line" + curLine);
                  break;
                } else {
                  // 读取成功，重置缓冲区指针
                  cur = 0;
                }
              }
              ch = (char) buffer[cur++];
              if (ch == '\n') {
                System.out.println("errer:invalid string(unclosed) at line" + curLine);
                curLine++;
                break;
              }
              if (state == 1) {
                if (ch == '\\') {
                  state = 2;
                } else if (ch == '\"') {
                  if (word.length() > 0) {
                    tokenFileWriter.write("stringConst:" + word.toString() + "\n");
                    symbolList.add(word.toString());
                  } else {
                    tokenFileWriter.write("stringConst:" + "\\0" + "\n");
                    symbolList.add("\\0");
                  }
                  break;
                } else {
                  word.append(ch);
                }
              } else if (state == 2) {
                state = 1;
                switch (ch) {
                  case '\\':
                  case 't':
                  case '\'':
                  case '\"':
                  case 'n':
                  case '0':
                  case 'r':
                    word.append("\\" + ch);
                    break;
                  default:
                    System.out.println("error:invalid escape character at Line:" + curLine);
                    word.append(ch);
                    break;
                }
              }
            }
          } else if (ch == '\r') {
            // 换行
            cur++;
            curLine++;
          } else if (ch == '\t') {
            // 制表符
          } else if (ch == ' ') {
            // 空格
          } else if (ch == '+') {
            tokenFileWriter.write("+:_\n");
          } else if (ch == '-') {
            tokenFileWriter.write("-:_\n");
          } else if (ch == '*') {
            tokenFileWriter.write("*:_\n");
          } else if (ch == '/') {
            /*---------------------------注释和/------------------------------------*/
            int state = 0;// 0表示除，1表示注释，2表示注释的最后一个*
            while (true) {
              if (cur >= len) {
                // 如果缓冲区剩余字符为0，再读一批
                len = source.read(buffer);
                if (len <= 0) {
                  // 没有读到新的字符
                  if (state == 0) {
                    tokenFileWriter.write("/:_\n");
                  } else {
                    System.out.println("error:注释意外终止");
                  }
                  break;
                } else {
                  // 读取成功，重置缓冲区指针
                  cur = 0;
                }
              }
              ch = (char) buffer[cur++];
              if (ch == '\n') {
                curLine++;
              }
              if (ch != '*' && state == 0) {
                // 判定是/
                tokenFileWriter.write("/:_\n");
                cur--;
                break;
              } else if (ch == '*' && state == 0) {
                state = 1;
              } else if (ch == '*' && state == 1) {
                state = 2;
              } else if (ch == '/' && state == 2) {
                state = 0;
                break;
                // 注释结束
              } else if (ch != '/' && state == 2) {
                state = 1;
              }

            }

          } else if (ch == '<') {
            /*--------------------------------<   <=------------------------------*/
            if (cur >= len) {
              // 如果缓冲区剩余字符为0，再读一批
              len = source.read(buffer);
              if (len <= 0) {
                // 没有读到新的字符
                tokenFileWriter.write("<:_\n");
                break;
              } else {
                // 读取成功，重置缓冲区指针
                cur = 0;
              }
            }
            ch = (char) buffer[cur++];
            if (ch == '=') {
              tokenFileWriter.write("<=:_\n");
            } else {
              tokenFileWriter.write("<:_\n");
              cur--;// 回退
            }
          } else if (ch == '>') {
            /*-------------------------------->   >=-------------------------------*/
            if (cur >= len) {
              // 如果缓冲区剩余字符为0，再读一批
              len = source.read(buffer);
              if (len <= 0) {
                // 没有读到新的字符
                tokenFileWriter.write(">:_\n");
                break;
              } else {
                // 读取成功，重置缓冲区指针
                cur = 0;
              }
            }
            ch = (char) buffer[cur++];
            if (ch == '=') {
              tokenFileWriter.write(">=:_\n");
            } else {
              tokenFileWriter.write(">:_\n");
              cur--;// 回退
            }

          } else if (ch == '=') {
            /*--------------------------------=   ==-----------------------------*/
            if (cur >= len) {
              // 如果缓冲区剩余字符为0，再读一批
              len = source.read(buffer);
              if (len <= 0) {
                // 没有读到新的字符
                tokenFileWriter.write("=:_\n");
                break;
              } else {
                // 读取成功，重置缓冲区指针
                cur = 0;
              }
            }
            ch = (char) buffer[cur++];
            if (ch == '=') {
              tokenFileWriter.write("==:_\n");
            } else {
              tokenFileWriter.write("=:_\n");
              cur--;// 回退
            }

          } else if (ch == '!') {
            /*--------------------------------!   !=------------------------------------*/
            if (cur >= len) {
              // 如果缓冲区剩余字符为0，再读一批
              len = source.read(buffer);
              if (len <= 0) {
                // 没有读到新的字符
                tokenFileWriter.write("!:_\n");
                break;
              } else {
                // 读取成功，重置缓冲区指针
                cur = 0;
              }
            }
            ch = (char) buffer[cur++];
            if (ch == '=') {
              tokenFileWriter.write("!=:_\n");
            } else {
              tokenFileWriter.write("!:_\n");
              cur--;// 回退
            }

          } else if (ch == '&') {
            /*-----------------------------&&--------------------------------------------*/
            if (cur >= len) {
              // 如果缓冲区剩余字符为0，再读一批
              len = source.read(buffer);
              if (len <= 0) {
                // 没有读到新的字符
                System.out.println("error:expect && at Line:" + curLine);
                break;
              } else {
                // 读取成功，重置缓冲区指针
                cur = 0;
              }
            }
            ch = (char) buffer[cur++];
            if (ch == '&') {
              tokenFileWriter.write("&&:_\n");
            } else {
              System.out.println("error:expect && at Line:" + curLine);
              cur--;// 回退
            }

          } else if (ch == '|') {
            /*-----------------------------||---------------------------------------------*/
            if (cur >= len) {
              // 如果缓冲区剩余字符为0，再读一批
              len = source.read(buffer);
              if (len <= 0) {
                // 没有读到新的字符
                System.out.println("error:expect || at Line:" + curLine);
                break;
              } else {
                // 读取成功，重置缓冲区指针
                cur = 0;
              }
            }
            ch = (char) buffer[cur++];
            if (ch == '|') {
              tokenFileWriter.write("||:_\n");
            } else {
              System.out.println("error:expect || at Line:" + curLine);
              cur--;// 回退
            }
            /*----------------------------------------------------------------------------*/
          } else if (ch == ',') {
            tokenFileWriter.write(",:_\n");
          } else if (ch == ';') {
            tokenFileWriter.write(";:_\n");
          } else if (ch == '[') {
            tokenFileWriter.write("[:_\n");
          } else if (ch == ']') {
            tokenFileWriter.write("]:_\n");
          } else if (ch == '(') {
            tokenFileWriter.write("(:_\n");
          } else if (ch == ')') {
            tokenFileWriter.write("):_\n");
          } else if (ch == '{') {
            tokenFileWriter.write("{:_\n");
          } else if (ch == '}') {
            tokenFileWriter.write("}:_\n");
          } else {
            // 非法字符
            System.out.println("error:illegal symbol:\'" + ch + "\' at Line:" + curLine);
          }
        }
        if (cur >= len) {
          len = source.read(buffer);
        }
      }
    }

    for (String symbol : symbolList) {
      symbolFileWriter.write(symbol + "\n");
    }
    tokenFileWriter.write("@end:_");
    tokenFileWriter.close();
    symbolFileWriter.close();
    source.close();
  }

  static private void initCodeOfKind() {
    codeOfKind = new Hashtable<>();
    codeOfKind.put("id", 1);
    codeOfKind.put("intConst", 2);
    codeOfKind.put("floatConst", 3);
    codeOfKind.put("charConst", 4);
    codeOfKind.put("stringConst", 5);
    codeOfKind.put("+", 6);
    codeOfKind.put("-", 7);
    codeOfKind.put("*", 8);
    codeOfKind.put("/", 9);
    codeOfKind.put("<", 10);
    codeOfKind.put("<=", 11);
    codeOfKind.put(">", 12);
    codeOfKind.put(">=", 13);
    codeOfKind.put("=", 14);
    codeOfKind.put("==", 15);
    codeOfKind.put("!=", 16);
    codeOfKind.put("!", 17);
    codeOfKind.put("&&", 18);
    codeOfKind.put("||", 19);
    codeOfKind.put(",", 20);
    codeOfKind.put(";", 21);
    codeOfKind.put("[", 22);
    codeOfKind.put("]", 23);
    codeOfKind.put("(", 24);
    codeOfKind.put(")", 25);
    codeOfKind.put("{", 26);
    codeOfKind.put("}", 27);
    codeOfKind.put("\'", 28);
    codeOfKind.put("\"", 29);
    codeOfKind.put("int", 30);
    codeOfKind.put("float", 31);
    codeOfKind.put("char", 32);
    codeOfKind.put("string", 33);
    codeOfKind.put("if", 34);
    codeOfKind.put("else", 35);
    codeOfKind.put("while", 36);
    codeOfKind.put("break", 37);
    codeOfKind.put("continue", 38);
    codeOfKind.put("return", 39);
    codeOfKind.put("main", 40);
  }
}
