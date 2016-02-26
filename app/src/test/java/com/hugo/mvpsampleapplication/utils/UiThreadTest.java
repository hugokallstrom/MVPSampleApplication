package com.hugo.mvpsampleapplication.utils;

import org.junit.Test;
import rx.android.schedulers.AndroidSchedulers;

import static org.junit.Assert.assertEquals;

/**
 * Created by hugo on 2/25/16.
 */
public class UiThreadTest {

  @Test
  public void getSchedulerShouldReturnAndroidSchedulersMainThread() {
    UiThread uiThread = new UiThread();
    assertEquals(AndroidSchedulers.mainThread(), uiThread.getScheduler());
  }
}