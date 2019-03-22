package org.paradsiehell.monitor;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * 网络监听接口实现
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) final class NetworkMonitorListener
    extends ConnectivityManager.NetworkCallback {

  @Override
  public void onLost(Network network) {
    super.onLost(network);
    NetworkTypeHandler.getInstance().handleNetworkType(NetworkType.DISCONNECTED);
  }

  @Override
  public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
    super.onCapabilitiesChanged(network, networkCapabilities);
    // 判断是否有网络变化
    if (networkCapabilities != null
        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
      if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        // WIFI
        NetworkTypeHandler.getInstance().handleNetworkType(NetworkType.WIFI);
      } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        // CELLULAR
        NetworkTypeHandler.getInstance().handleNetworkType(NetworkType.CELLULAR);
      }
    }
  }
}
