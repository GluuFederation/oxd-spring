package org.xdi.oxd.spring.service;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.response.GetAuthorizationUrlResponse;
import org.xdi.oxd.common.response.UpdateSiteResponse;
import org.xdi.oxd.spring.OxdSpringApplication;
import org.xdi.oxd.spring.Settings;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OxdSpringApplication.class)
public class OxdServiceImplTest {

    @Value("${oxd.client.redirect-uri}")
    private String redirectUrl;

    @Inject
    private OxdService oxdService;

    @Inject
    private Settings settings;

    @Test
    public void updateSite() {
	CommandResponse cr = oxdService.updateSite(settings.getOxdId(), redirectUrl);
	Optional<UpdateSiteResponse> updateSiteResponse = Optional.of(cr)
		.map(c -> c.dataAsResponse(UpdateSiteResponse.class));
	Assert.assertTrue(updateSiteResponse.isPresent());
    }

    @Test
    public void getAuthorizationUrl() {
	Optional<GetAuthorizationUrlResponse> getAuthorizationUrlResponse = Optional
		.of(oxdService.getAuthorizationUrl(settings.getOxdId()))
		.map(c -> c.dataAsResponse(GetAuthorizationUrlResponse.class));
	Assert.assertTrue(getAuthorizationUrlResponse.isPresent());
    }
}
