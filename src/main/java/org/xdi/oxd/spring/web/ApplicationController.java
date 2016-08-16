package org.xdi.oxd.spring.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xdi.oxd.common.response.GetAuthorizationUrlResponse;
import org.xdi.oxd.common.response.LogoutResponse;
import org.xdi.oxd.spring.Settings;
import org.xdi.oxd.spring.security.GluuUser;
import org.xdi.oxd.spring.service.OxdService;

import javax.inject.Inject;
import java.util.Optional;

@Controller
public class ApplicationController {

    @Inject
    private OxdService oxdService;

    @Inject
    private Settings settings;

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public String user() {
        return "fragments/user";
    }

    @RequestMapping(path = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        return "fragments/home";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String loginError(Model model, @ModelAttribute("error") String error, @ModelAttribute("error_description") String errorDescription) {
        model.addAttribute("error", error);
        model.addAttribute("error_description", errorDescription);
        return "fragments/error";
    }

    @ModelAttribute("isLoggedIn")
    public Boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken))
            return true;
        else
            return false;
    }

    @ModelAttribute("user")
    public GluuUser getUser() {
        if (!isLoggedIn())
            return null;
        return (GluuUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @ModelAttribute("logoutUrl")
    public String getLogoutUrl() {
        if (!isLoggedIn())
            return null;
        GluuUser user = getUser();
        return Optional.of(oxdService).map(c -> c.getLogoutUrl(settings.getOxdId(), user.getIdToken()))
                .map(c -> c.dataAsResponse(LogoutResponse.class)).map(LogoutResponse::getUri).orElse(null);
    }

    @ModelAttribute("authorizationUrl")
    public String getAuthorizationUrl() {
        if (isLoggedIn())
            return null;

        return Optional.of(oxdService).map(c -> c.getAuthorizationUrl(settings.getOxdId()))
                .map(c -> c.dataAsResponse(GetAuthorizationUrlResponse.class))
                .map(GetAuthorizationUrlResponse::getAuthorizationUrl).orElse(null);
    }

    @ModelAttribute("oxdId")
    public String getOxdId() {
        return settings.getOxdId();
    }
}