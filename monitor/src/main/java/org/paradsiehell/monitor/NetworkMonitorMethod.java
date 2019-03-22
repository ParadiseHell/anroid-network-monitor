package org.paradsiehell.monitor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class NetworkMonitorMethod {
  //<editor-fold desc="属性">

  // 参数类型
  @Nullable
  private Class<?> mParameterClass;
  // 注解网路类型
  @NonNull
  private NetworkType mAnnotationNetworkType;
  // 方法
  @NonNull
  private Method mMethod;
  //</editor-fold>

  //<editor-fold desc="构造方法">

  NetworkMonitorMethod(
      @Nullable Class<?> parameterClass,
      @NonNull NetworkType networkType,
      @NonNull Method method) {
    mParameterClass = parameterClass;
    mAnnotationNetworkType = networkType;
    mMethod = method;
  }
  //</editor-fold>

  //<editor-fold desc="公开方法">

  /**
   * 执行方法
   *
   * @param object 方法所在的类对象
   * @param type 网络类型
   */
  void invoke(@NonNull Object object, @NonNull NetworkType type) {
    // 类型检测
    // 1. 注解类型是否相同
    // 2. 是否为连接成功类型
    if (type.equals(mAnnotationNetworkType)
        || isConnectedType(type)) {
      try {
        // 判断方法是否有参数, 并执行方法
        if (mParameterClass == null) {
          mMethod.invoke(object);
        } else {
          mMethod.invoke(object, type);
        }
      } catch (IllegalAccessException e) {
        // do nothing, because it will never happen
      } catch (InvocationTargetException e) {
        // do nothing, because it will never happen
      }
    }
  }
  //</editor-fold>

  //<editor-fold desc="工具方法">

  /**
   * 是否为连接成功的方法
   *
   * @param type 网络类型
   */
  private boolean isConnectedType(@NonNull NetworkType type) {
    return NetworkType.CONNECTED.equals(mAnnotationNetworkType)
        && (NetworkType.CELLULAR.equals(type)
        || NetworkType.WIFI.equals(type));
  }
  //</editor-fold>
}
