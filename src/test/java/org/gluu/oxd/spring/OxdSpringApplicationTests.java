package org.gluu.oxd.spring;

import org.gluu.oxd.spring.OxdSpringApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OxdSpringApplication.class)
@WebAppConfiguration
public class OxdSpringApplicationTests {

    @Test
    public void contextLoads() {
    }

}
