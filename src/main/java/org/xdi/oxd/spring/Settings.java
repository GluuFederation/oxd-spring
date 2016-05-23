package org.xdi.oxd.spring;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.ResponseStatus;
import org.xdi.oxd.common.response.RegisterSiteResponse;
import org.xdi.oxd.spring.domain.AppSettings;
import org.xdi.oxd.spring.domain.enumiration.SettingsType;
import org.xdi.oxd.spring.repository.AppSettingsRepository;
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

    @Inject
    private AppSettingsRepository appSettingsRepository;

    private String oxdId;

    public String getOxdId() {
	return oxdId;
    }

    @EventListener({ ContextRefreshedEvent.class })
    private void onContextStarted() {
	AppSettings appSettings = appSettingsRepository.findOneByType(SettingsType.OXD_ID);
	if (appSettings != null) {
	    this.oxdId = appSettings.getValue();
	    return;
	}

	CommandResponse commandResponse = oxdService.registerSite(redirectUrl, logoutUrl, postLogoutUrl);
	if (commandResponse.getStatus().equals(ResponseStatus.ERROR))
	    throw new RuntimeException("Can not register site");

	RegisterSiteResponse response = commandResponse.dataAsResponse(RegisterSiteResponse.class);
	this.oxdId = response.getOxdId();

	appSettings = new AppSettings();
	appSettings.setType(SettingsType.OXD_ID);
	appSettings.setValue(oxdId);
	appSettings = appSettingsRepository.save(appSettings);
    }
}
