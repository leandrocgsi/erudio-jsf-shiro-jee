package br.com.erudio.controller;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;

import br.com.erudio.cdi.annotation.LoggedUser;
import br.com.erudio.model.User;
import br.com.erudio.util.Constants;

@Named
@SessionScoped
public class BaseController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private transient Logger logger;

    private User usuarioLogado;

    /**
     * protected usar com @Inject
     */
    @Named("usuarioLogado")
    @Produces
    @LoggedUser
    protected User getUsuarioLogado() {
        final Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            return null;
        }

        if (usuarioLogado == null) {
            usuarioLogado = subject.getPrincipals().oneByType(User.class);
        }
        return usuarioLogado;
    }

    private void redirect(String page) {
        try {
            if (!getHttpServletResponse().isCommitted()) {
                logger.info("redirect to " + page);
                getHttpServletResponse().sendRedirect(getContextPath() + page);
            }
        } catch (IOException e) {
            logger.error("Erro ao redirecionar para " + page, e);
        }
    }

    public String getRealPath() {
        try {
            ExternalContext ec = getExternalContext();
            URI uri = new URI(ec.getRequestScheme(), null,
                    ec.getRequestServerName(), ec.getRequestServerPort(), null,
                    null, null);
            return uri.toASCIIString();
        } catch (URISyntaxException e) {
            throw new FacesException(e);
        }
    }

    public void redirectPageNotFound() {
        redirect("/pagina-nao-existe/");
    }

    public void redirectPageAccessNotAllowed() {
        redirect("/acesso-nao-autorizado/");
    }

    public boolean isAdminTS() {
        return getUsuarioLogado().getEmail().equalsIgnoreCase(Constants.ERUDIO_MAIL);
    }

    public void checkAdminTS() {
        if (!isAdminTS()) {
            redirectPageAccessNotAllowed();
        }
    }

    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public String getContextPath() {
        return getHttpServletRequest().getContextPath();
    }

    public String getRequestURL() {
        return getHttpServletRequest().getRequestURL().toString();
    }
}
