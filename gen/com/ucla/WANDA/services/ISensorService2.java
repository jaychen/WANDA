/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\java\\WANDA\\src\\com\\ucla\\WANDA\\services\\ISensorService2.aidl
 */
package com.ucla.WANDA.services;
public interface ISensorService2 extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ucla.WANDA.services.ISensorService2
{
private static final java.lang.String DESCRIPTOR = "com.ucla.WANDA.services.ISensorService2";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ucla.WANDA.services.ISensorService2 interface,
 * generating a proxy if needed.
 */
public static com.ucla.WANDA.services.ISensorService2 asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ucla.WANDA.services.ISensorService2))) {
return ((com.ucla.WANDA.services.ISensorService2)iin);
}
return new com.ucla.WANDA.services.ISensorService2.Stub.Proxy(obj);
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
case TRANSACTION_getKm:
{
data.enforceInterface(DESCRIPTOR);
double _result = this.getKm();
reply.writeNoException();
reply.writeDouble(_result);
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.ucla.WANDA.services.ICallBack _arg0;
_arg0 = com.ucla.WANDA.services.ICallBack.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
com.ucla.WANDA.services.ICallBack _arg0;
_arg0 = com.ucla.WANDA.services.ICallBack.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopAcc:
{
data.enforceInterface(DESCRIPTOR);
this.stopAcc();
reply.writeNoException();
return true;
}
case TRANSACTION_startAcc:
{
data.enforceInterface(DESCRIPTOR);
this.startAcc();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ucla.WANDA.services.ISensorService2
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
public double getKm() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
double _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getKm, _data, _reply, 0);
_reply.readException();
_result = _reply.readDouble();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void registerCallback(com.ucla.WANDA.services.ICallBack cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void unregisterCallback(com.ucla.WANDA.services.ICallBack cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void stopAcc() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopAcc, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void startAcc() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startAcc, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getKm = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stopAcc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_startAcc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public double getKm() throws android.os.RemoteException;
public void registerCallback(com.ucla.WANDA.services.ICallBack cb) throws android.os.RemoteException;
public void unregisterCallback(com.ucla.WANDA.services.ICallBack cb) throws android.os.RemoteException;
public void stopAcc() throws android.os.RemoteException;
public void startAcc() throws android.os.RemoteException;
}
