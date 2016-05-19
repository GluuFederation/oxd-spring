package org.xdi.oxd.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.xdi.oxd.spring.service.OxdServiceImplTest;
import org.xdi.oxd.spring.web.ApplicationControllerTest;
import org.xdi.oxd.spring.web.GluuControllerTest;

@RunWith(Suite.class)
@SuiteClasses({ ApplicationControllerTest.class, GluuControllerTest.class, OxdServiceImplTest.class })
public class RunAllTests {

}
