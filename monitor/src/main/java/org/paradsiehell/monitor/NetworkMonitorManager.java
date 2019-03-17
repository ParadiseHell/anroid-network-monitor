package org.paradsiehell.monitor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class NetworkMonitorManager {
  //<editor-fold desc="单例">

  @SuppressLint("StaticFieldLeak")
  private static volatile NetworkMonitorManager sInstance;
  //</editor-fold>

  //<editor-fold desc="属性">
  @Nullable
  private Context mApplicationContext;
  @Nullable
  private NetworkMonitorBroadcastReceiver mReceiver;
  @Nullable
  private NetworkMonitorListener mMonitorListener;
  //</editor-fold>

  //<editor-fold desc="单例获取">

  private NetworkMonitorManager() {

  }

  @NonNull
  public static NetworkMonitorManager getInstance() {
    if (sInstance == null) {
      synchronized (NetworkMonitorManager.class) {
        if (sInstance == null) {
          sInstance = new NetworkMonitorManager();
        }
      }
    }
    return sInstance;
  }
  //</editor-fold>

  //<editor-fold desc="公开方法">

  /**
   * 初始化
   *
   * @param applicationContext 初始化
   * @throws IllegalArgumentException 当 applicationContext 不是 ApplicationContext 的触发异常
   */
  public void init(@NonNull Context applicationContext) {
    if (!(applicationContext instanceof Application)) {
      throw new IllegalArgumentException("applicationContext must be Application");
    }
    if (mApplicationContext != null) {
      return;
    }
    mApplicationContext = applicationContext;
    // 判断 Android 版本
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      // 小于 21 的使用 BroadcastReceiver
      mReceiver = new NetworkMonitorBroadcastReceiver();
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction(NetworkMonitorBroadcastReceiver.ACTION_CONNECTIVITY_CHANGED);
      mApplicationContext.registerReceiver(mReceiver, intentFilter);
    } else {
      // 大于 21 的使用 ConnectivityManager.NetworkCallback
      ConnectivityManager manager = (ConnectivityManager) mApplicationContext.getSystemService(
          Context.CONNECTIVITY_SERVICE
      );
      if (manager != null) {
        mMonitorListener = new NetworkMonitorListener();
        manager.registerNetworkCallback(new NetworkRequest.Builder().build(), mMonitorListener);
      }
    }
  }

  /**
   * 销毁
   */
  public void onDestroy() {
    if (mApplicationContext != null) {
      if (mReceiver != null) {
        mApplicationContext.unregisterReceiver(mReceiver);
      }
      if (mMonitorListener != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          ConnectivityManager manager = (ConnectivityManager) mApplicationContext.getSystemService(
              Context.CONNECTIVITY_SERVICE
          );
          if (manager != null) {
            manager.unregisterNetworkCallback(mMonitorListener);
          }
        }
      }
    }
  }

  /**
   * 注册监听
   *
   * @param object 持有 {@link NetworkMonitor} 注解的对象
   */
  @SuppressWarnings("WeakerAccess")
  public void register(@NonNull Object object) {
    NetworkMonitorMethodManager.getInstance().register(object);
  }

  /**
   * 取消注册监听
   *
   * @param object 持有 {@link NetworkMonitor} 注解的对象
   */
  @SuppressWarnings("WeakerAccess")
  public void unregister(@NonNull Object object) {
    NetworkMonitorMethodManager.getInstance().unregister(object);
  }
  //</editor-fold>
}