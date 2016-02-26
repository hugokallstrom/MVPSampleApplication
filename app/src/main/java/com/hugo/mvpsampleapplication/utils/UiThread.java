package com.hugo.mvpsampleapplication.utils;

import javax.inject.Inject;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by hugo on 2/13/16.
 */
public class UiThread implements PostExecutionThread {

  @Inject
  public UiThread() {

  }

  @Override
  public Scheduler getScheduler() {
    return AndroidSchedulers.mainThread();
  }
}
