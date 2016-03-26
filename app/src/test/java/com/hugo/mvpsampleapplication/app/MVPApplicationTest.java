package com.hugo.mvpsampleapplication.app;

import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.ApplicationComponent;
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
  private MVPApplication mvpApplication;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvpApplication = new MVPApplication();
  }

  @Test
  public void testSetAndGetApplicationComponent() throws Exception {
    ApplicationComponent nullApplicationComponent = mvpApplication.getApplicationComponent();
    assertNull(nullApplicationComponent);

    mvpApplication.setApplicationComponent(applicationComponent);
    applicationComponent = mvpApplication.getApplicationComponent();
    assertNotNull(applicationComponent);
  }


}