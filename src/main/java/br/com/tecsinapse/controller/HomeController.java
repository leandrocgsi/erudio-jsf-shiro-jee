package br.com.tecsinapse.controller;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
@URLMappings(mappings = {
        @URLMapping(id = "home", pattern = "/home/", viewId = "/jsf/pages/protegido/home.xhtml"),
        @URLMapping(id = "pagina-nao-existe", pattern = "/pagina-nao-existe/", viewId = "/jsf/ops/pagina-nao-existe.xhtml"),
        @URLMapping(id = "acesso-nao-autorizado", pattern = "/acesso-nao-autorizado/", viewId = "/jsf/ops/acesso-nao-autorizado.xhtml"),
        @URLMapping(id = "sessao-expirada", pattern = "/sessao-expirada/", viewId = "/jsf/ops/sessao-expirada.xhtml")
})
public class HomeController implements Serializable {
    private static final long serialVersionUID = 1L;

}
