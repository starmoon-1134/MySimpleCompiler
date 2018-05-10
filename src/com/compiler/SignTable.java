package com.compiler;

import java.util.ArrayList;

public class SignTable {
  private int size;
  private SignTable parentTable;
  private ArrayList<Sign> table;

  public SignTable()
  {
    super();
    this.setSize(0);
    this.setParentTable(null);
    this.table = new ArrayList<>();
  }

  public boolean add(Sign sign) {
    return table.add(sign);
  }

  /**
   * @return size
   */
  public int getSize() {
    return size;
  }

  /**
   * @param size
   *          要设置的 size
   */
  public void setSize(int size) {
    this.size = size;
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

}
