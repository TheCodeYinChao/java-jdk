package org.omg.PortableServer.POAPackage;


/**
* org/omg/PortableServer/POAPackage/InvalidPolicyHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u144/9417/corba/src/share/classes/org/omg/PortableServer/poa.idl
* Friday, July 21, 2017 9:58:52 PM PDT
*/

abstract public class InvalidPolicyHelper
{
  private static String  _id = "IDL:omg.org/PortableServer/POA/InvalidPolicy:1.0";

  public static void insert (org.omg.CORBA.Any a, InvalidPolicy that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static InvalidPolicy extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ushort);
          _members0[0] = new org.omg.CORBA.StructMember (
            "index",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (InvalidPolicyHelper.id (), "InvalidPolicy", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static InvalidPolicy read (org.omg.CORBA.portable.InputStream istream)
  {
    InvalidPolicy value = new InvalidPolicy ();
    // read and discard the repository ID
    istream.read_string ();
    value.index = istream.read_ushort ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, InvalidPolicy value)
  {
    // write the repository ID
    ostream.write_string (id ());
    ostream.write_ushort (value.index);
  }

}
