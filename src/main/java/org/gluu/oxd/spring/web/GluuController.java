package org.gluu.oxd.spring.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gluu.oxd.client.GetTokensByCodeResponse2;
import org.gluu.oxd.spring.Settings;
import org.gluu.oxd.spring.security.AuthoritiesConstants;
import org.gluu.oxd.spring.security.GluuUser;
import org.gluu.oxd.spring.service.OxdService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/gluu")
public class GluuController {

    @Inject
    private OxdService oxdService;

    @Inject
    private Settings settings;

    @RequestMapping(path = "/callback", method = RequestMethod.GET)
    public String redirect(@RequestParam(name = "code", required = false) String code,
                           @RequestParam(name = "state", required = false) String state,
                           @RequestParam(name = "error", required = false) String error,
                           @RequestParam(name = "error_description", required = false) String errorDescription,
                           RedirectAttributes redirectAttributes) {

        if (error != null) {
            redirectAttributes.addAttribute("error", error);
            redirectAttributes.addAttribute("error_description", errorDescription);
            return "redirect:/error";
        }

        Optional<GetTokensByCodeResponse2> tokenResponse = Optional.of(oxdService)
                .map(c -> c.getTokenByCode(settings.getOxdId(), code,state));

        JsonNode userInfoResponse = tokenResponse
                .map(c -> oxdService.getUserInfo(settings.getOxdId(), c.getAccessToken()))
                .orElseThrow(() -> new BadCredentialsException("Can't get user info"));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> result = mapper.convertValue(userInfoResponse, Map.class);

        Collection<GrantedAuthority> authorities = Arrays
                .asList(new GrantedAuthority[]{new SimpleGrantedAuthority(AuthoritiesConstants.USER)});

        GluuUser user = new GluuUser(tokenResponse.get().getIdToken(), result, authorities);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, "", authorities));

        return "redirect:/user";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/home";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleAllException(BadCredentialsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", e.getCause());
        redirectAttributes.addAttribute("error_description", e.getMessage());
        return "redirect:/error";
    }
}