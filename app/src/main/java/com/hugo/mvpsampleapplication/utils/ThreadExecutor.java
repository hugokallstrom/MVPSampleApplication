package com.hugo.mvpsampleapplication.utils;

import rx.Scheduler;

public interface ThreadExecutor {
    Scheduler getScheduler();
}
