package com.sun.corba.se.spi.activation.RepositoryPackage;

/**
* com/sun/corba/se/spi/activation/RepositoryPackage/ServerDefHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u144/9417/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Friday, July 21, 2017 9:58:51 PM PDT
*/

public final class ServerDefHolder implements org.omg.CORBA.portable.Streamable
{
  public ServerDef value = null;

  public ServerDefHolder ()
  {
  }

  public ServerDefHolder (ServerDef initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ServerDefHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ServerDefHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ServerDefHelper.type ();
  }

}
