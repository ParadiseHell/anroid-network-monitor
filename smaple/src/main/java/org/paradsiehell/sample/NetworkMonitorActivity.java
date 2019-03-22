package org.paradsiehell.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.paradsiehell.monitor.NetworkMonitor;
import org.paradsiehell.monitor.NetworkMonitorManager;
import org.paradsiehell.monitor.NetworkType;

public final class NetworkMonitorActivity extends AppCompatActivity {
  //<editor-fold desc="常量">
  private static final String TAG = "NetworkMonitorActivity";
  //</editor-fold>

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network_monitor);
    NetworkMonitorManager.getInstance().register(this);
  }

  //<editor-fold desc="网络状态监听">

  @NetworkMonitor(type = NetworkType.CONNECTED)
  public void onNetworkConnected(@NonNull NetworkType type) {
    log("on network connected : " + type.name());
  }

  @NetworkMonitor(type = NetworkType.CELLULAR)
  public void onCellular() {
    log("on cellular network type");
  }

  @NetworkMonitor(type = NetworkType.WIFI)
  public void onWifiType(@NonNull NetworkType type) {
    log("on wifi network type : " + type.name());
  }

  @NetworkMonitor(type = NetworkType.DISCONNECTED)
  public void onNetworkDisconnected() {
    log("on network disconnected");
  }
  //</editor-fold>

  //</editor-fold>

  //<editor-fold desc="工具方法">
  private void log(@NonNull String message) {
    Log.w(TAG, message);
  }
  //</editor-fold>

  //<editor-fold desc="生命周期">

  @Override
  protected void onDestroy() {
    NetworkMonitorManager.getInstance().unregister(this);
    super.onDestroy();
  }
  //</editor-fold>
}
