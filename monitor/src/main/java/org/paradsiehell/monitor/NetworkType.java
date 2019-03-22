package org.paradsiehell.monitor;

/**
 * 网络类型
 */
public enum NetworkType {
  /**
   * 连接成功<br/>
   * 包含一下几种情况 : <br/>
   * 1. {@link NetworkType#WIFI} <br/>
   * 2. {@link NetworkType#CELLULAR} <br/>
   */
  CONNECTED,
  /**
   * WIFI <br/>
   */
  WIFI,
  /**
   * 蜂窝移动
   */
  CELLULAR,
  /**
   * 连接失败, 没有任何网络
   */
  DISCONNECTED
}
