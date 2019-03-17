package org.paradsiehell.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * 网络监听广播
 */
public final class NetworkMonitorBroadcastReceiver extends BroadcastReceiver {
  //<editor-fold desc="常量">

  // action
  public static final String ACTION_CONNECTIVITY_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE";
  //</editor-fold>

  //<editor-fold desc="属性">

  // 上一次网络类型, 避免重复通知
  @Nullable
  private NetworkType mLastNetworkType;
  //</editor-fold>

  @SuppressWarnings("deprecation")
  @Override
  public void onReceive(Context context, Intent intent) {
    if (context == null || intent == null || intent.getAction() == null) {
      return;
    }
    switch (intent.getAction()) {
      case ACTION_CONNECTIVITY_CHANGED:
        // 获取当前网络类型
        ConnectivityManager manager =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
          NetworkType networkType = NetworkType.NONE;
          // 获取网络信息, 如果网络信息为 null, 则表明没有网络
          NetworkInfo networkInfo = manager.getActiveNetworkInfo();
          if (networkInfo != null) {
            int type = networkInfo.getType();
            switch (type) {
              case ConnectivityManager.TYPE_WIFI:
                networkType = NetworkType.WIFI;
                break;
              case ConnectivityManager.TYPE_MOBILE:
                networkType = NetworkType.CELLULAR;
                break;
              default:
                break;
            }
          }
          // 判断本次网络类型是否和上一次网络类型相同
          if (networkType != mLastNetworkType) {
            mLastNetworkType = networkType;
            NetworkMonitorMethodManager.getInstance().invokeNetworkMonitorMethods(networkType);
          }
          break;
        }
      default:
        break;
    }
  }
}
