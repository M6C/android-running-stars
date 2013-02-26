/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Documents David\\Dropbox\\Dev\\Android\\Workspace\\GDocument\\RunningStars\\src\\com\\runningstars\\service\\system\\ISystemGpsSessionService.aidl
 */
package com.runningstars.service.system;
public interface ISystemGpsSessionService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.runningstars.service.system.ISystemGpsSessionService
{
private static final java.lang.String DESCRIPTOR = "com.runningstars.service.system.ISystemGpsSessionService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.runningstars.service.system.ISystemGpsSessionService interface,
 * generating a proxy if needed.
 */
public static com.runningstars.service.system.ISystemGpsSessionService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.runningstars.service.system.ISystemGpsSessionService))) {
return ((com.runningstars.service.system.ISystemGpsSessionService)iin);
}
return new com.runningstars.service.system.ISystemGpsSessionService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
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
case TRANSACTION_startService:
{
data.enforceInterface(DESCRIPTOR);
this.startService();
reply.writeNoException();
return true;
}
case TRANSACTION_stopService:
{
data.enforceInterface(DESCRIPTOR);
this.stopService();
reply.writeNoException();
return true;
}
case TRANSACTION_getSession:
{
data.enforceInterface(DESCRIPTOR);
org.gdocument.gtracergps.launcher.domain.Session _result = this.getSession();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.runningstars.service.system.ISystemGpsSessionService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void startService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public org.gdocument.gtracergps.launcher.domain.Session getSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.gdocument.gtracergps.launcher.domain.Session _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSession, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = org.gdocument.gtracergps.launcher.domain.Session.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_startService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void startService() throws android.os.RemoteException;
public void stopService() throws android.os.RemoteException;
public org.gdocument.gtracergps.launcher.domain.Session getSession() throws android.os.RemoteException;
}
