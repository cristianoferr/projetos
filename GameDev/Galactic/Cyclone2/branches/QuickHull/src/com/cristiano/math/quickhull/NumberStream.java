package com.cristiano.math.quickhull;
import java.io.*;
/** This class provides a convenient way to read numbers from a text file */

public class NumberStream {

  protected StreamTokenizer st;  //The StreamTokenizer we use to get numbers

  /** Creates a NumberStream that parses the specified input stream.
@param is - an input stream.
   */
  public NumberStream(InputStream is) {
    this(new BufferedReader(new InputStreamReader(is)));
  }

  /** Creates a NumberStream that parses the specified character stream.
   */
  public NumberStream(Reader r) {
    st = new StreamTokenizer(r);
  }

  /** return the next double from the stream.
    Return Double.NaN if end of file has been reached.
    Note: you must use Double.isNaN() to test if a double is NaN
    */
  public double next() throws IOException{
    int t;
    while ((t=st.nextToken()) != StreamTokenizer.TT_EOF &&
	    t != StreamTokenizer.TT_NUMBER) {
      //nothing
    }
    if (t== StreamTokenizer.TT_EOF) {
      return Double.NaN;
    } else {
      return st.nval;
    }
  }
}
