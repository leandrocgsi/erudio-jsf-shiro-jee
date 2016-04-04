package br.com.tecsinapse.controller;

import br.com.tecsinapse.jsf.JsfUtil;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import org.apache.shiro.SecurityUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
@URLMappings(mappings = {
        @URLMapping(id = "login", pattern = "/login/", viewId = "/jsf/login.xhtml"),
})
public class IndexController implements Serializable {
    private static final long serialVersionUID = 1L;

    @URLAction(mappingId = "login", onPostback = true)
    public void login() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            JsfUtil.prettyRedirect("home");
        }
    }

    public int random() {
        return (int) (Math.random() * 100) % 4;
    }
}
