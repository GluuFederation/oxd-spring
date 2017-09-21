package org.xdi.oxd.spring.service;

import org.xdi.oxd.common.CommandResponse;

public interface OxdService {

    CommandResponse registerSite(String redirectUrl, String postLogoutRedirectUrl);

    CommandResponse updateSite(String oxdId, String redirectUrl);

    CommandResponse getAuthorizationUrl(String oxdId);

    CommandResponse getTokenByCode(String oxdId, String code,String state);

    CommandResponse getUserInfo(String oxdId, String accessToken);

    CommandResponse getLogoutUrl(String oxdId, String idToken);
}
