package org.paradsiehell.monitor;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) final class NetworkMonitorListener
    extends ConnectivityManager.NetworkCallback {
  //<editor-fold desc="属性">
  // 上一次网络类型
  @Nullable
  private NetworkType mLastNetworkType;
  // 本次网络类型
  @Nullable
  private NetworkType mCurrentNetworkType;
  // Handler 用于主线程通知
  private Handler mMainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
      NetworkMonitorMethodManager.getInstance().invokeNetworkMonitorMethods((NetworkType) msg.obj);
      return false;
    }
  });
  //</editor-fold>

  @Override
  public void onLost(Network network) {
    super.onLost(network);
    mCurrentNetworkType = NetworkType.NONE;
    notifyNetworkChanged();
  }

  @Override
  public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
    super.onCapabilitiesChanged(network, networkCapabilities);
    if (networkCapabilities != null
        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
      if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        mCurrentNetworkType = NetworkType.WIFI;
      } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        mCurrentNetworkType = NetworkType.CELLULAR;
      }
      // 通知网络发生变化
      notifyNetworkChanged();
    }
  }

  /**
   * 通知网络发生变化
   */
  private synchronized void notifyNetworkChanged() {
    if (mCurrentNetworkType != null && mCurrentNetworkType != mLastNetworkType) {
      mLastNetworkType = mCurrentNetworkType;
      // 发送消息
      Message message = Message.obtain();
      message.obj = mCurrentNetworkType;
      mMainHandler.sendMessage(message);
    }
  }
}
