package org.xdi.oxd.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xdi.oxd.client.CommandClient;
import org.xdi.oxd.common.Command;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.CommandType;
import org.xdi.oxd.common.params.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;

@Component
public class OxdServiceImpl implements OxdService {

    private static final Logger logger = LoggerFactory.getLogger(OxdServiceImpl.class);

    @Value("${oxd.server.op-host}")
    private String opHost;

    @Value("${oxd.server.host}")
    private String host;

    @Value("${oxd.server.port}")
    private int port;

    private CommandClient client;

    @PostConstruct
    public void initIt() {
        try {
            client = new CommandClient(host, port);
        } catch (IOException e) {
            logger.error("oxd client did not initialized properly with: {host: '" + host + "', port: '" + port + "'}", e);
        }
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        CommandClient.closeQuietly(client);
    }

    @Override
    public CommandResponse registerSite(String redirectUrl, String postLogoutRedirectUrl) {
        final RegisterSiteParams commandParams = new RegisterSiteParams();
        commandParams.setOpHost(opHost);
        commandParams.setAuthorizationRedirectUri(redirectUrl);
        commandParams.setPostLogoutRedirectUri(postLogoutRedirectUrl);
        commandParams.setRedirectUris(Arrays.asList(redirectUrl));
        commandParams.setAcrValues(Arrays.asList("basic"));
        commandParams.setScope(Arrays.asList("openid", "profile"));
        commandParams.setGrantType(Arrays.asList("authorization_code"));
        commandParams.setResponseTypes(Arrays.asList("code"));

        final Command command = new Command(CommandType.REGISTER_SITE).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse updateSite(String oxdId, String redirectUrl) {
        final UpdateSiteParams commandParams = new UpdateSiteParams();
        commandParams.setOxdId(oxdId);
        commandParams.setAuthorizationRedirectUri(redirectUrl);

        final Command command = new Command(CommandType.UPDATE_SITE).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse getAuthorizationUrl(String oxdId) {
        final GetAuthorizationUrlParams commandParams = new GetAuthorizationUrlParams();
        commandParams.setAcrValues(Arrays.asList(new String[]{"basic"}));
        commandParams.setOxdId(oxdId);
        final Command command = new Command(CommandType.GET_AUTHORIZATION_URL).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse getTokenByCode(String oxdId, String code,String state) {
        final GetTokensByCodeParams commandParams = new GetTokensByCodeParams();
        commandParams.setOxdId(oxdId);
        commandParams.setCode(code);
        commandParams.setState(state);
        final Command command = new Command(CommandType.GET_TOKENS_BY_CODE).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse getUserInfo(String oxdId, String accessToken) {
        GetUserInfoParams params = new GetUserInfoParams();
        params.setOxdId(oxdId);
        params.setAccessToken(accessToken);

        final Command command = new Command(CommandType.GET_USER_INFO).setParamsObject(params);

        return client.send(command);
    }

    @Override
    public CommandResponse getLogoutUrl(String oxdId, String idToken) {
        GetLogoutUrlParams params = new GetLogoutUrlParams();
        params.setOxdId(oxdId);
        params.setIdTokenHint(idToken);
        final Command command = new Command(CommandType.GET_LOGOUT_URI).setParamsObject(params);

        return client.send(command);
    }
}
