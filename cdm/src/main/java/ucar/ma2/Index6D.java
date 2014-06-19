/*
 * Copyright 1998-2014 University Corporation for Atmospheric Research/Unidata
 *
 *   Portions of this software were developed by the Unidata Program at the
 *   University Corporation for Atmospheric Research.
 *
 *   Access and use of this software shall impose the following obligations
 *   and understandings on the user. The user is granted the right, without
 *   any fee or cost, to use, copy, modify, alter, enhance and distribute
 *   this software, and any derivative works thereof, and its supporting
 *   documentation for any purpose whatsoever, provided that this entire
 *   notice appears in all copies of the software, derivative works and
 *   supporting documentation.  Further, UCAR requests that the user credit
 *   UCAR/Unidata in any publications that result from the use of this
 *   software or in any product that includes this software. The names UCAR
 *   and/or Unidata, however, may not be used in any advertising or publicity
 *   to endorse or promote any products or commercial entity unless specific
 *   written permission is obtained from UCAR/Unidata. The user also
 *   understands that UCAR/Unidata is not obligated to provide the user with
 *   any support, consulting, training or assistance of any kind with regard
 *   to the use, operation and performance of this software nor to provide
 *   the user with any updates, revisions, new versions or "bug fixes."
 *
 *   THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 *   INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 *   FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *   NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 *   WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ucar.ma2;

/**
 * Specialization of Index for rank 6 arrays.
 *
 * @see Index
 * @author caron
 */
public class Index6D extends Index {

  /** current element's indices */
  private int curr0, curr1, curr2, curr3, curr4, curr5;
  /** array strides */
  private int stride0, stride1, stride2, stride3, stride4, stride5;
  /** array shapes */
  private int shape0, shape1, shape2, shape3, shape4, shape5;

  Index6D() { super(6); }
  public Index6D( int[] shape) {
    super(shape);
    precalc();
  }

  protected void precalc() {
    shape0 = shape[0];
    shape1 = shape[1];
    shape2 = shape[2];
    shape3 = shape[3];
    shape4 = shape[4];
    shape5 = shape[5];

    stride0 = stride[0];
    stride1 = stride[1];
    stride2 = stride[2];
    stride3 = stride[3];
    stride4 = stride[4];
    stride5 = stride[5];

    curr0 = current[0];
    curr1 = current[1];
    curr2 = current[2];
    curr3 = current[3];
    curr4 = current[4];
    curr5 = current[5];
  }

   public String toString() {
     return curr0+","+curr1+","+curr2+","+curr3+","+curr4+","+curr5;
   }

  public int [] getCurrentCounter() {
    current[0] = curr0;
    current[1] = curr1;
    current[2] = curr2;
    current[3] = curr3;
    current[4] = curr4;
    current[5] = curr5;
    return current.clone();
  }

  public int currentElement() {
    return offset + curr0*stride0 + curr1*stride1 + curr2*stride2 +
        + curr3*stride3 + curr4*stride4 + curr5*stride5;
  }

  public int incr() {

      if (++curr5 >= shape5) {
        curr5 = 0;
        if (++curr4 >= shape4) {
          curr4 = 0;
          if (++curr3 >= shape3) {
            curr3 = 0;
              if (++curr2 >= shape2) {
                curr2 = 0;
                if (++curr1 >= shape1) {
                  curr1 = 0;
                  if (++curr0 >= shape0) {
                    curr0 = 0;    // rollover !
                  }
                }
              }
            }
          }
        }


    return offset + curr0*stride0 + curr1*stride1 + curr2*stride2 +
        + curr3*stride3 + curr4*stride4 + curr5*stride5;
  }

  public void setDim(int dim, int value) {
    if (value < 0 || value >= shape[dim])  // check index here
      throw new ArrayIndexOutOfBoundsException();

    if (dim == 5)
      curr5 = value;
    else if (dim == 4)
      curr4 = value;
    else if (dim == 3)
      curr3 = value;
    else if (dim == 2)
      curr2 = value;
    else if (dim == 1)
      curr1 = value;
    else
      curr0 = value;
  }

  public Index set0(int v) {
    if (v < 0 || v >= shape0)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr0 = v;
    return this;
  }

  public Index set1(int v) {
    if (v < 0 || v >= shape1)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr1 = v;
    return this;
  }

  public Index set2(int v) {
    if (v < 0 || v >= shape2)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr2 = v;
    return this;
  }

  public Index set3(int v) {
    if (v < 0 || v >= shape3)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr3 = v;
    return this;
  }

  public Index set4(int v) {
    if (v < 0 || v >= shape4)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr4 = v;
    return this;
  }

  public Index set5(int v) {
    if (v < 0 || v >= shape5)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr5 = v;
    return this;
  }

  public Index set(int v0, int v1, int v2, int v3, int v4, int v5) {
    set0(v0);
    set1(v1);
    set2(v2);
    set3(v3);
    set4(v4);
    set5(v5);
    return this;
  }

  public Index set(int[] index){
    if (index.length != rank)
      throw new ArrayIndexOutOfBoundsException();
    set0(index[0]);
    set1(index[1]);
    set2(index[2]);
    set3(index[3]);
    set4(index[4]);
    set5(index[5]);
    return this;
  }

  public Object clone() {
    return super.clone();
  }

    //experimental : should be package private
  int setDirect(int v0, int v1, int v2, int v3, int v4, int v5) {
    return offset + v0*stride0 + v1*stride1 + v2*stride2 + v3*stride3 +
        v4*stride4 + v5*stride5;
  }


}
