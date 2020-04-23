package org.gluu.oxd.spring;

import org.gluu.oxd.spring.service.OxdServiceImplTest;
import org.gluu.oxd.spring.web.ApplicationControllerTest;
import org.gluu.oxd.spring.web.GluuControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ApplicationControllerTest.class, GluuControllerTest.class, OxdServiceImplTest.class})
public class RunAllTests {

}
