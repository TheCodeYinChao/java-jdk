package org.omg.CosNaming;


/**
* org/omg/CosNaming/Binding.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u144/9417/corba/src/share/classes/org/omg/CosNaming/nameservice.idl
* Friday, July 21, 2017 9:58:51 PM PDT
*/

public final class Binding implements org.omg.CORBA.portable.IDLEntity
{
  public NameComponent binding_name[] = null;

  // name
  public BindingType binding_type = null;

  public Binding ()
  {
  } // ctor

  public Binding (NameComponent[] _binding_name, BindingType _binding_type)
  {
    binding_name = _binding_name;
    binding_type = _binding_type;
  } // ctor

} // class Binding
