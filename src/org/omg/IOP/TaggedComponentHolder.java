package org.omg.IOP;

/**
* org/omg/IOP/TaggedComponentHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u144/9417/corba/src/share/classes/org/omg/PortableInterceptor/IOP.idl
* Friday, July 21, 2017 9:58:52 PM PDT
*/

public final class TaggedComponentHolder implements org.omg.CORBA.portable.Streamable
{
  public TaggedComponent value = null;

  public TaggedComponentHolder ()
  {
  }

  public TaggedComponentHolder (TaggedComponent initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = TaggedComponentHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    TaggedComponentHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return TaggedComponentHelper.type ();
  }

}
