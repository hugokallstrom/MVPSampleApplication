package com.hugo.mvpsampleapplication.utils;

import org.junit.Test;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Created by hugo on 2/25/16.
 */
public class JobExecutorTest {

  @Test
  public void getSchedulerShouldReturnIoScheduler() {
    JobExecutor jobExecutor = new JobExecutor();
    assertEquals(Schedulers.io(), jobExecutor.getScheduler());
  }
}