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
    // 判断网络类型是否符合要求
    // 1. 注解网络类型和参数网络类型相同
    // 2. 注解网络类型为 BOTH 并且 参数网络类型为 CELLULAR 或者 WIFI
    if (mAnnotationNetworkType.equals(type)
        || (mAnnotationNetworkType.equals(NetworkType.BOTH)
        && (type.equals(NetworkType.CELLULAR) || type.equals(NetworkType.WIFI)))) {
      try {
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
}
