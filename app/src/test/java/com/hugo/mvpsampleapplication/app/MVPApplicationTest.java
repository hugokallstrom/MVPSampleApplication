package com.hugo.mvpsampleapplication.app;

import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.ApplicationComponent;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by hugo on 2/25/16.
 */
public class MVPApplicationTest {

  @Mock
  private ApplicationComponent applicationComponent;
  private MVPApplication mvvmApplication;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvvmApplication = new MVPApplication();
  }

  @Test
  public void testSetAndGetApplicationComponent() throws Exception {
    ApplicationComponent nullApplicationComponent = mvvmApplication.getApplicationComponent();
    assertNull(nullApplicationComponent);

    mvvmApplication.setApplicationComponent(applicationComponent);
    applicationComponent = mvvmApplication.getApplicationComponent();
    assertNotNull(applicationComponent);
  }


}