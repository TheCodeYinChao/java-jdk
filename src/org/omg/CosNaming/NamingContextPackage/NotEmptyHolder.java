package org.omg.CosNaming.NamingContextPackage;

/**
* org/omg/CosNaming/NamingContextPackage/NotEmptyHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u144/9417/corba/src/share/classes/org/omg/CosNaming/nameservice.idl
* Friday, July 21, 2017 9:58:51 PM PDT
*/

public final class NotEmptyHolder implements org.omg.CORBA.portable.Streamable
{
  public NotEmpty value = null;

  public NotEmptyHolder ()
  {
  }

  public NotEmptyHolder (NotEmpty initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = NotEmptyHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    NotEmptyHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return NotEmptyHelper.type ();
  }

}
