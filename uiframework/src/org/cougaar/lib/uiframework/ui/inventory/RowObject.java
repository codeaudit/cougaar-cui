package org.cougaar.lib.uiframework.ui.inventory;
public class RowObject
{
  int numberOfRows;
  int numberOfColumns;
  Object[] columnValues;
  public RowObject(int rows, int cols, Object[] colValues)
  {
    numberOfRows = rows;
    numberOfColumns = cols;
    columnValues = colValues;
    //System.out.println("row object " + ((Double)columnValues[0]).doubleValue());
  }
  public int compareTo(RowObject b)
  {
    if(((Double)columnValues[0]).longValue() == ((Double)b.columnValues[0]).doubleValue())
      return 0;
    else if(((Double)columnValues[0]).doubleValue() < ((Double)b.columnValues[0]).doubleValue())
      return -1;
    else if(((Double)columnValues[0]).doubleValue() > ((Double)b.columnValues[0]).doubleValue())
      return 1;
    return 0;
  }
 public  void mergeUp(Object[] b)
 {
  for(int i = 1; i < numberOfColumns; i++) // start at column 2
  {
    /*
    Long colVal = ((Long)columnValues[i]).longValue();
    long appendData = ((Long)b[i]).longValue();
    colVal += appendData;
    columnValues[i] = new Long(colVal);
    */
    Class aCol = columnValues[i].getClass();
    Class bCol = b[i].getClass();
    if(aCol.getName().equals("java.lang.String"))
      if(bCol.getName().equals("java.lang.String"))
        continue;
      else
        columnValues[i] = b[i];
    else
      continue;
  }
 }
}
