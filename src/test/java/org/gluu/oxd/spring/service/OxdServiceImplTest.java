package org.gluu.oxd.spring.service;

import org.gluu.oxd.common.response.GetAuthorizationUrlResponse;
import org.gluu.oxd.common.response.UpdateSiteResponse;
import org.gluu.oxd.spring.OxdSpringApplication;
import org.gluu.oxd.spring.Settings;
import org.gluu.oxd.spring.service.OxdService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OxdSpringApplication.class)
public class OxdServiceImplTest {

    @Value("${oxd.client.callback-uri}")
    private String callbackUrl;

    @Inject
    private OxdService oxdService;

    @Inject
    private Settings settings;

    @Test
    public void updateSite() {
        UpdateSiteResponse resp = oxdService.updateSite(settings.getOxdId(), callbackUrl);
        Assert.assertNotNull(resp);
    }

    /*@Test
    public void getAuthorizationUrl() {
        Optional<GetAuthorizationUrlResponse> getAuthorizationUrlResponse = Optional
                .of(oxdService.getAuthorizationUrl(settings.getOxdId()));
        Assert.assertTrue(getAuthorizationUrlResponse.isPresent());
    }*/
}
