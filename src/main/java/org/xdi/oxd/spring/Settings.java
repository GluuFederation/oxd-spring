package org.xdi.oxd.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.ResponseStatus;
import org.xdi.oxd.common.response.RegisterSiteResponse;
import org.xdi.oxd.spring.domain.AppSettings;
import org.xdi.oxd.spring.repository.AppSettingsRepository;
import org.xdi.oxd.spring.service.OxdService;

import javax.inject.Inject;

@Component
public class Settings {

    @Value("${oxd.server.op-host}")
    private String opHost;

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

    @EventListener({ContextRefreshedEvent.class})
    private void onContextStarted() {
        AppSettings appSettings = appSettingsRepository.findOneByOpHost(opHost);
        if (appSettings != null) {
            this.oxdId = appSettings.getOxdId();
            return;
        }

        CommandResponse commandResponse = oxdService.registerSite(redirectUrl, logoutUrl, postLogoutUrl);
        if (commandResponse.getStatus().equals(ResponseStatus.ERROR))
            throw new RuntimeException("Can not register site");

        RegisterSiteResponse response = commandResponse.dataAsResponse(RegisterSiteResponse.class);
        this.oxdId = response.getOxdId();

        appSettings = new AppSettings();
        appSettings.setOxdId(oxdId);
        appSettings.setOpHost(opHost);
        appSettingsRepository.save(appSettings);
    }
}
