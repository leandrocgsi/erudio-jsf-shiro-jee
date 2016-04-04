package br.com.tecsinapse.shiro;

import java.util.Arrays;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.SimplePrincipalCollection;

import br.com.tecsinapse.model.Usuario;
import br.com.tecsinapse.util.Constantes;

public class RestRealm extends UsuarioRealm {
    private static final String REALM_NAME = "restRealm";

    public RestRealm() {
        setName(REALM_NAME);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        Usuario usuario = getUsuarioService().buscaParaLogin(Constantes.LIP_MAIL);
        if (Constantes.REST_USER.equals(token.getUsername())) {
            final SimplePrincipalCollection pc = new SimplePrincipalCollection(
                    Arrays.asList(Constantes.REST_USER, usuario.getId(), usuario), getName());

            return new SimpleAuthenticationInfo(pc, Constantes.SENHA_BASIC);
        } else {
            return null;
        }
    }
}
