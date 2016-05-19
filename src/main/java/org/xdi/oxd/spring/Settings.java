package org.xdi.oxd.spring;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.ResponseStatus;
import org.xdi.oxd.common.response.RegisterSiteResponse;
import org.xdi.oxd.spring.service.OxdService;

@Component
public class Settings {

    @Value("${oxd.client.redirect-uri}")
    private String redirectUrl;

    @Value("${oxd.client.logout-uri}")
    private String logoutUrl;

    @Value("${oxd.client.post-logout-uri}")
    private String postLogoutUrl;

    @Inject
    private OxdService oxdService;

    private String oxdId;

    public String getOxdId() {
	return oxdId;
    }

    @EventListener({ ContextRefreshedEvent.class })
    private void onContextStarted() {
	CommandResponse commandResponse = oxdService.registerSite(redirectUrl, logoutUrl, postLogoutUrl);
	if (commandResponse.getStatus().equals(ResponseStatus.ERROR))
	    throw new RuntimeException("Can not register site");

	RegisterSiteResponse response = commandResponse.dataAsResponse(RegisterSiteResponse.class);
	this.oxdId = response.getOxdId();
    }
}
