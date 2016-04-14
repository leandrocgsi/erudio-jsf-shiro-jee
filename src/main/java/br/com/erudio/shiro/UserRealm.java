package br.com.erudio.shiro;

import java.util.Arrays;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import br.com.erudio.model.User;
import br.com.erudio.service.UserService;

public class UserRealm extends AuthorizingRealm {

    public static final String REALM_NAME = "userRealm";

    public UserRealm() {
        setName(REALM_NAME);
    }

    public static PrincipalCollection createPrincipalCollection(User user) {
        return new SimplePrincipalCollection(Arrays.asList(user.getEmail(), user.getId(), user), REALM_NAME);
    }

    UserService getUserService() {
        return BeanProvider.getContextualReference(UserService.class, false);
    }

    public void cleanUserCache(User user) {
        clearCache(createPrincipalCollection(user));
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User userPrincipal = principals.oneByType(User.class);
        if (userPrincipal == null) {
            throw new AuthorizationException("Invalid credentials");
        }

        final User user = getUserService().findToLogin(userPrincipal.getEmail());

        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;

        User user = getUserService().findToLogin(token.getUsername());
        if (user != null) {
            PrincipalCollection pc = createPrincipalCollection(user);
            return new SimpleAuthenticationInfo(pc, user.getPassword());
        } else {
            return null;
        }
    }
}
