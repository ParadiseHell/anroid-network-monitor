package org.paradsiehell.sample;

import android.app.Application;
import org.paradsiehell.monitor.NetworkMonitorManager;

public final class SampleApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    NetworkMonitorManager.getInstance().init(this);
  }

  @Override
  public void onTerminate() {
    NetworkMonitorManager.getInstance().onDestroy();
    super.onTerminate();
  }
}
