package org.gluu.oxd.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.gluu.oxd.client.ClientInterface;
import org.gluu.oxd.client.GetTokensByCodeResponse2;
import org.gluu.oxd.client.OxdClient;
import org.gluu.oxd.common.params.*;
import org.gluu.oxd.common.response.*;
import org.gluu.oxd.spring.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.UUID;

@Component
public class OxdServiceImpl implements OxdService {

    private static final Logger logger = LoggerFactory.getLogger(OxdServiceImpl.class);

    @Value("${oxd.server.scopes}")
    private String scopes;

    @Value("${oxd.server.acr-values}")
    private String acrValues;

    @Value("${oxd.server.op-host}")
    private String opHost;

    @Value("${oxd.server.host}")
    private String host;

    @Value("${oxd.server.grant-types}")
    private String grantTypes;

    @Value("${oxd.server.port}")
    private int port;

    @Value("${oxd.client.callback-uri}")
    private String callbackUrl;

    @Value("${oxd.client.post-logout-uri}")
    private String postLogoutUrl;

    @Inject
    private Settings settings;

    private ClientInterface client;

    @PostConstruct
    public void initIt() {
        try {
            client = OxdClient.newTrustAllClient(getTargetHost(host, port));
        } catch (Exception e) {
            logger.error("oxd client did not initialized properly with: {host: '" + host + "', port: '" + port + "'}", e);
        }
    }

    @Override
    public RegisterSiteResponse registerSite(String redirectUrl, String postLogoutRedirectUrl) {
        final RegisterSiteParams params = new RegisterSiteParams();

        params.setOpHost(opHost);
        params.setPostLogoutRedirectUris(Lists.newArrayList(postLogoutRedirectUrl.split(",")));
        params.setRedirectUris(Lists.newArrayList(redirectUrl.split(",")));
        params.setScope(Lists.newArrayList(scopes.split(",")));
        params.setTrustedClient(true);
        params.setGrantTypes(Lists.newArrayList(grantTypes.split(",")));
        params.setClientName("sampleapp-client-extension-" + System.currentTimeMillis());
        //params.setResponseTypes(Lists.newArrayList("code"));
        params.setAcrValues(Lists.newArrayList(acrValues.split(",")));

        final RegisterSiteResponse resp = client.registerSite(params);

        return resp;
    }

    @Override
    public UpdateSiteResponse updateSite(String oxdId, String redirectUrl) {
        final UpdateSiteParams params = new UpdateSiteParams();
        params.setOxdId(oxdId);
        params.setRedirectUris(Lists.newArrayList(redirectUrl));

        final UpdateSiteResponse resp = client.updateSite(getClientToken(oxdId), params);
        return resp;
    }

    @Override
    public GetAuthorizationUrlResponse getAuthorizationUrl(String oxdId) {
        final GetAuthorizationUrlParams cmdParams = new GetAuthorizationUrlParams();
        cmdParams.setOxdId(oxdId);
        cmdParams.setAcrValues(Lists.newArrayList(acrValues.split(",")));
        cmdParams.setScope(Lists.newArrayList(scopes.split(",")));

        final GetAuthorizationUrlResponse resp = client.getAuthorizationUrl(getClientToken(oxdId), cmdParams);
        return resp;
    }

    @Override
    public GetTokensByCodeResponse2 getTokenByCode(String oxdId, String code,String state) {
        final GetTokensByCodeParams cmdParams = new GetTokensByCodeParams();
        cmdParams.setOxdId(oxdId);
        cmdParams.setCode(code);
        cmdParams.setState(state);

        final GetTokensByCodeResponse2 resp = client.getTokenByCode(getClientToken(oxdId), cmdParams);
        return resp;
    }

    @Override
    public JsonNode getUserInfo(String oxdId, String accessToken) {
        GetUserInfoParams cmdParams = new GetUserInfoParams();
        cmdParams.setOxdId(oxdId);
        cmdParams.setAccessToken(accessToken);
        JsonNode resp = client.getUserInfo(getClientToken(oxdId), cmdParams);

        return resp;
    }

    @Override
    public GetLogoutUriResponse getLogoutUrl(String oxdId, String idToken) {
        final GetLogoutUrlParams params = new GetLogoutUrlParams();
        params.setOxdId(oxdId);
        params.setIdTokenHint(idToken);
        params.setPostLogoutRedirectUri(postLogoutUrl);
        params.setState(UUID.randomUUID().toString());
        params.setSessionState(UUID.randomUUID().toString()); // here must be real session instead of dummy UUID

        final GetLogoutUriResponse resp = client.getLogoutUri(getClientToken(oxdId), params);

        return resp;
    }

    private static String getTargetHost(String host, int port) {
        return "https://" + host + ":" + port;
    }

    private String getClientToken(String oxdId) {
        if (StringUtils.isNotBlank(oxdId)) {
            final GetClientTokenParams params = new GetClientTokenParams();
            params.setOpHost(opHost);
            params.setScope(Lists.newArrayList(scopes.split(",")));
            params.setClientId(settings.getClientId());
            params.setClientSecret(settings.getClientSecret());

            GetClientTokenResponse resp = client.getClientToken(params);

            return "Bearer " + resp.getAccessToken();
        }
        return null;
    }
}
