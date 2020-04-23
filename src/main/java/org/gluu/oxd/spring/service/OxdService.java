package org.gluu.oxd.spring.service;


import com.fasterxml.jackson.databind.JsonNode;
import org.gluu.oxd.client.GetTokensByCodeResponse2;
import org.gluu.oxd.common.response.GetAuthorizationUrlResponse;
import org.gluu.oxd.common.response.GetLogoutUriResponse;
import org.gluu.oxd.common.response.RegisterSiteResponse;
import org.gluu.oxd.common.response.UpdateSiteResponse;

public interface OxdService {

    RegisterSiteResponse registerSite(String redirectUrl, String postLogoutRedirectUrl);

    UpdateSiteResponse updateSite(String oxdId, String redirectUrl);

    GetAuthorizationUrlResponse getAuthorizationUrl(String oxdId);

    GetTokensByCodeResponse2 getTokenByCode(String oxdId, String code, String state);

    JsonNode getUserInfo(String oxdId, String accessToken);

    GetLogoutUriResponse getLogoutUrl(String oxdId, String idToken);
}
