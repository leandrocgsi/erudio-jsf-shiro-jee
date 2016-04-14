package br.com.erudio.shiro;

import java.util.Arrays;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.SimplePrincipalCollection;

import br.com.erudio.model.User;
import br.com.erudio.util.Constants;

public class RestRealm extends UserRealm {
    private static final String REALM_NAME = "restRealm";

    public RestRealm() {
        setName(REALM_NAME);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        User usuario = getUserService().findToLogin(Constants.ERUDIO_MAIL);
        if (Constants.REST_USER.equals(token.getUsername())) {
            final SimplePrincipalCollection pc = new SimplePrincipalCollection(Arrays.asList(Constants.REST_USER, usuario.getId(), usuario), getName());
            return new SimpleAuthenticationInfo(pc, Constants.BASIC_PASSWORD);
        } else {
            return null;
        }
    }
}