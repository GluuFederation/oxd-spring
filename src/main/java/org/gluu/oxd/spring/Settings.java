package org.gluu.oxd.spring;

import org.gluu.oxd.common.response.RegisterSiteResponse;
import org.gluu.oxd.spring.domain.AppSettings;
import org.gluu.oxd.spring.repository.AppSettingsRepository;
import org.gluu.oxd.spring.service.OxdService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class Settings {

    @Value("${oxd.server.op-host}")
    private String opHost;

    @Value("${oxd.client.callback-uri}")
    private String callbackUrl;

    @Value("${oxd.client.post-logout-uri}")
    private String postLogoutUrl;

    @Inject
    private OxdService oxdService;

    @Inject
    private AppSettingsRepository appSettingsRepository;

    private String oxdId;

    private String clientId;

    private String clientSecret;

    public String getOxdId() {
        return oxdId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @EventListener({ContextRefreshedEvent.class})
    private void onContextStarted() {
        AppSettings appSettings = appSettingsRepository.findOneByOpHost(opHost);
        if (appSettings != null) {
            this.oxdId = appSettings.getOxdId();
            this.clientId = appSettings.getClientId();
            this.clientSecret = appSettings.getClientSecret();
            return;
        }
        try {
            RegisterSiteResponse response = oxdService.registerSite(callbackUrl, postLogoutUrl);

            this.oxdId = response.getOxdId();
            this.clientId = response.getClientId();
            this.clientSecret = response.getClientSecret();

            appSettings = new AppSettings();
            appSettings.setOxdId(oxdId);
            appSettings.setOpHost(opHost);
            appSettings.setClientId(clientId);
            appSettings.setClientSecret(clientSecret);
            appSettingsRepository.save(appSettings);
        } catch (Exception e) {
            throw new RuntimeException("Can not register site: {callbackUrl: '" + callbackUrl + "', postLogoutUrl: '" + postLogoutUrl + "'}. Plese see the oxd-server.log");
        }
    }
}
