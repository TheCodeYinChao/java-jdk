package org.omg.PortableServer;


/**
* org/omg/PortableServer/LifespanPolicyValue.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u144/9417/corba/src/share/classes/org/omg/PortableServer/poa.idl
* Friday, July 21, 2017 9:58:52 PM PDT
*/


/**
	 * The LifespanPolicyValue can have the following values.
	 * TRANSIENT - The objects implemented in the POA 
	 * cannot outlive the POA instance in which they are 
	 * first created. 
	 * PERSISTENT - The objects implemented in the POA can 
	 * outlive the process in which they are first created. 
	 */
public class LifespanPolicyValue implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 2;
  private static LifespanPolicyValue[] __array = new LifespanPolicyValue [__size];

  public static final int _TRANSIENT = 0;
  public static final LifespanPolicyValue TRANSIENT = new LifespanPolicyValue(_TRANSIENT);
  public static final int _PERSISTENT = 1;
  public static final LifespanPolicyValue PERSISTENT = new LifespanPolicyValue(_PERSISTENT);

  public int value ()
  {
    return __value;
  }

  public static LifespanPolicyValue from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected LifespanPolicyValue (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class LifespanPolicyValue
