package org.paradsiehell.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络监听广播
 */
public final class NetworkMonitorBroadcastReceiver extends BroadcastReceiver {
  //<editor-fold desc="常量">

  // action
  public static final String ACTION_CONNECTIVITY_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE";
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
          // 默认为
          NetworkType networkType = NetworkType.DISCONNECTED;
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
          // 处理网络类型
          NetworkTypeHandler.getInstance().handleNetworkType(networkType);
          break;
        }
      default:
        break;
    }
  }
}
