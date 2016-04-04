package br.com.erudio.shiro;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import br.com.erudio.model.Usuario;

@WebListener
public class LoginListener implements AuthenticationListener, HttpSessionListener {

    private static final Map<String, String> SESSIONID_LOGIN = new HashMap<>();
    private static final Map<String, HttpSession> SESSIONS_BY_IDS = new HashMap<>();

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        Usuario usuario = info.getPrincipals().oneByType(Usuario.class);
        if (info.getPrincipals().getRealmNames().contains(UsuarioRealm.REALM_NAME)) {
            singleLogin(usuario.getEmail());
        }
    }

    private void singleLogin(String login) {
        final Session session = SecurityUtils.getSubject().getSession();

        final String old = SESSIONID_LOGIN.put(login, session.getId().toString());
        if (old != null) {
            final HttpSession oldSession = SESSIONS_BY_IDS.get(old);
            if (oldSession != null) {
                //oldSession.invalidate();
            }
        }
    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException e) {
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        SESSIONS_BY_IDS.put(se.getSession().getId(), se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        SESSIONS_BY_IDS.remove(se.getSession().getId());
    }
}
