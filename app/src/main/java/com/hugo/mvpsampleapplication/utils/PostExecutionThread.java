package com.hugo.mvpsampleapplication.utils;

import rx.Scheduler;

public interface PostExecutionThread {
  Scheduler getScheduler();
}
