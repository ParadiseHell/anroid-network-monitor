package org.paradsiehell.monitor;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 网络类型处理类
 */
final class NetworkTypeHandler {
  //<editor-fold desc="单例">

  private static volatile NetworkTypeHandler sInstance;
  //</editor-fold>

  //<editor-fold desc="属性">

  // 上一次网络类型, 避免重复通知
  @Nullable
  private volatile NetworkType mLastNetworkType;
  // 单例线程池, 同于同步处理网络类型
  @NonNull
  private Executor mSingleExecutor;
  // Handler, 用于切换到主线程
  @NonNull
  private Handler mHandler;
  //</editor-fold>

  //<editor-fold desc="单例获取">

  private NetworkTypeHandler() {
    mSingleExecutor = Executors.newSingleThreadExecutor();
    mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
      @Override
      public boolean handleMessage(@NonNull Message msg) {
        Object object = msg.obj;
        if (object instanceof NetworkType) {
          // 执行网络监听方法
          NetworkMonitorMethodManager.getInstance().invokeNetworkMonitorMethods(
              (NetworkType) object
          );
        }
        return true;
      }
    });
  }

  @NonNull
  static NetworkTypeHandler getInstance() {
    if (sInstance == null) {
      synchronized (NetworkTypeHandler.class) {
        if (sInstance == null) {
          sInstance = new NetworkTypeHandler();
        }
      }
    }
    return sInstance;
  }
  //</editor-fold>

  //<editor-fold desc="公开方法">

  /**
   * 处理网络类型
   *
   * @param networkType 网络类型
   */
  void handleNetworkType(@NonNull final NetworkType networkType) {
    mSingleExecutor.execute(new NetworkRunnable(networkType));
  }
  //</editor-fold>

  //<editor-fold desc="内部类">

  /**
   * 用于处理网络类型的 Runnable
   */
  private final class NetworkRunnable implements Runnable {
    @NonNull
    private final NetworkType mNetworkType;

    NetworkRunnable(@NonNull NetworkType networkType) {
      mNetworkType = networkType;
    }

    @Override
    public void run() {
      synchronized (NetworkTypeHandler.class) {
        // 保证本次网络状态和上一次网络状态不一致
        if (mNetworkType != mLastNetworkType) {
          // 重置上一次网络类型
          mLastNetworkType = mNetworkType;
          // 发送消息到主线程
          Message message = Message.obtain();
          message.obj = mNetworkType;
          mHandler.sendMessage(message);
        }
      }
    }
  }
  //</editor-fold>
}
