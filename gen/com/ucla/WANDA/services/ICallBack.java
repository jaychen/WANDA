/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\java\\WANDA\\src\\com\\ucla\\WANDA\\services\\ICallBack.aidl
 */
package com.ucla.WANDA.services;
public interface ICallBack extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ucla.WANDA.services.ICallBack
{
private static final java.lang.String DESCRIPTOR = "com.ucla.WANDA.services.ICallBack";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ucla.WANDA.services.ICallBack interface,
 * generating a proxy if needed.
 */
public static com.ucla.WANDA.services.ICallBack asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ucla.WANDA.services.ICallBack))) {
return ((com.ucla.WANDA.services.ICallBack)iin);
}
return new com.ucla.WANDA.services.ICallBack.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onKmChanged:
{
data.enforceInterface(DESCRIPTOR);
double _arg0;
_arg0 = data.readDouble();
this.onKmChanged(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ucla.WANDA.services.ICallBack
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void onKmChanged(double value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeDouble(value);
mRemote.transact(Stub.TRANSACTION_onKmChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onKmChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onKmChanged(double value) throws android.os.RemoteException;
}
