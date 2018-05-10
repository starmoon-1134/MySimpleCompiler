package com.compiler;

public class Sign {
  public String id;
  public String type;
  public int offset;
  public int lengthofArray;
  public SignTable blockSignTable;

  public Sign(String id, String type, int offset, int lengthofArray, SignTable blockSignTable)
  {
    super();
    this.id = id;
    this.type = type;
    this.offset = offset;
    this.lengthofArray = lengthofArray;
    this.blockSignTable = blockSignTable;
  }

}
