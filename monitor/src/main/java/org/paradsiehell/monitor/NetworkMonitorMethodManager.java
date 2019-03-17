package org.paradsiehell.monitor;

import android.support.annotation.NonNull;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class NetworkMonitorMethodManager {
  //<editor-fold desc="单例">

  private static volatile NetworkMonitorMethodManager sInstance;
  //</editor-fold>

  //<editor-fold desc="属性">
  @NonNull
  private Map<Object, List<NetworkMonitorMethod>> mMethodMap;
  //</editor-fold>

  //<editor-fold desc="单例获取">

  private NetworkMonitorMethodManager() {
    mMethodMap = new HashMap<>();
  }

  @NonNull
  static NetworkMonitorMethodManager getInstance() {
    if (sInstance == null) {
      synchronized (NetworkMonitorMethodManager.class) {
        if (sInstance == null) {
          sInstance = new NetworkMonitorMethodManager();
        }
      }
    }
    return sInstance;
  }
  //</editor-fold>

  //<editor-fold desc="公开方法">

  /**
   * 注册网络监听方法
   *
   * @param object 持有网络监听注解的对象
   */
  synchronized void register(@NonNull Object object) {
    // 获取该类中定义的所有方法
    Method[] methods = object.getClass().getMethods();
    // 循环方法数组找到符合要求的方法
    for (Method method : methods) {
      // 找出有 NetworkMonitor 注解的方法
      NetworkMonitor annotation = method.getAnnotation(NetworkMonitor.class);
      if (annotation != null) {
        // 判断方法的返回值是否为 void
        Type type = method.getGenericReturnType();
        if ("void".equals(type.toString())) {
          // 判断方法的参数
          Class<?>[] parameterTypes = method.getParameterTypes();
          // 参数数必须小于 2
          if (parameterTypes.length < 2) {
            // 获取参数类型
            Class<?> parameterType = null;
            if (parameterTypes.length == 1) {
              // 获取有一个参数, 参数类型必须是 NetworkType
              if (!NetworkType.class.equals(parameterTypes[0])) {
                continue;
              }
              parameterType = parameterTypes[0];
            }
            // 添加网络监听方法
            List<NetworkMonitorMethod> networkMonitorMethodList = mMethodMap.get(object);
            if (networkMonitorMethodList == null) {
              networkMonitorMethodList = new ArrayList<>();
              mMethodMap.put(object, networkMonitorMethodList);
            }
            networkMonitorMethodList.add(
                new NetworkMonitorMethod(parameterType, annotation.type(), method)
            );
          }
        }
      }
    }
  }

  /**
   * 反注册网络监听方法
   *
   * @param object 持有网络监听注解的对象
   */
  synchronized void unregister(@NonNull Object object) {
    mMethodMap.remove(object);
  }

  /**
   * 执行网络监听方法
   *
   * @param type 网络类型
   */
  synchronized void invokeNetworkMonitorMethods(@NonNull NetworkType type) {
    if (!mMethodMap.isEmpty()) {
      for (Map.Entry<Object, List<NetworkMonitorMethod>> entry : mMethodMap.entrySet()) {
        Object object = entry.getKey();
        List<NetworkMonitorMethod> methodList = entry.getValue();
        if (object != null && methodList != null && !methodList.isEmpty()) {
          for (NetworkMonitorMethod method : methodList) {
            if (method != null) {
              method.invoke(object, type);
            }
          }
        }
      }
    }
  }
  //</editor-fold>
}
