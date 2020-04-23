package org.gluu.oxd.spring.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GluuUser extends User {

    private static final long serialVersionUID = 1L;

    private final String idToken;
    private final Map<String, List<String>> claims;

    public GluuUser(String idToken, Map<String, List<String>> claims,
                    Collection<? extends GrantedAuthority> authorities) {
        super(idToken, "", authorities);

        this.idToken = idToken;
        this.claims = claims;
    }

    public String getIdToken() {
        return idToken;
    }

    public Map<String, List<String>> getClaims() {
        return claims;
    }
}
