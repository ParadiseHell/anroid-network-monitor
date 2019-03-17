package org.paradsiehell.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import org.paradsiehell.monitor.NetworkMonitor;
import org.paradsiehell.monitor.NetworkMonitorManager;
import org.paradsiehell.monitor.NetworkType;

public final class MainActivity extends AppCompatActivity {
  //<editor-fold desc="控件">
  @Nullable
  private TextView mTextView;
  //</editor-fold>

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTextView = findViewById(R.id.text_view);
    NetworkMonitorManager.getInstance().register(this);
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.WIFI)
  public void onWifiNetwork() {
    if (mTextView != null) {
      mTextView.setText("onWifiNetwork()");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.WIFI)
  public void onWifiNetwork(Object object) {
    if (mTextView != null) {
      mTextView.setText("onWifiNetwork(Object object)");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.CELLULAR)
  public void onMobileNetwork() {
    if (mTextView != null) {
      mTextView.setText("onMobileNetwork()");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.CELLULAR)
  public void onMobileNetwork(Object object) {
    if (mTextView != null) {
      mTextView.setText("onMobileNetwork(Object object)");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.BOTH)
  public void onBothNetwork() {
    if (mTextView != null) {
      mTextView.setText("onBothNetwork()");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.BOTH)
  public void onBothNetwork(Object object) {
    if (mTextView != null) {
      mTextView.setText("onBothNetwork(Object object)");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.BOTH)
  public void onBothNetwork(NetworkType networkType) {
    if (mTextView != null) {
      mTextView.setText("onBothNetwork(NetworkType networkType)");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.NONE)
  public void onNoNetwork() {
    if (mTextView != null) {
      mTextView.setText("onNoNetwork()");
    }
  }

  @SuppressLint("SetTextI18n")
  @NetworkMonitor(type = NetworkType.NONE)
  public void onNoNetwork(Object object) {
    if (mTextView != null) {
      mTextView.setText("onNoNetwork()");
    }
  }

  @Override
  protected void onDestroy() {
    NetworkMonitorManager.getInstance().unregister(this);
    super.onDestroy();
  }
}
