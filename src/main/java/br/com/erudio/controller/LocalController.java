package br.com.erudio.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.erudio.model.Local;
import br.com.erudio.service.LocalService;

@Named
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "locais", pattern = "/locais/", viewId = "/jsf/pages/protegido/geral/local/locais.xhtml"),
        @URLMapping(id = "local-ativar", pattern = "/locais/id/#{localController.localId}/", viewId = "/jsf/pages/protegido/geral/local/locais.xhtml")
})
public class LocalController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private LocalService localService;

    private List<Local> locais;
    private Long localId;
    private Local local;

    @URLAction(mappingId = "locais", onPostback = false)
    public void idLocais() {
        locais = localService.findAll();
    }
    
    @URLAction(mappingId = "local-ativar", onPostback = false)
    public String idLocalAtivar() {
    	local = localService.findById(localId.intValue());
    	local = localService.desbloquearLocal(local, true);
    	locais = localService.findAll();
        Messages.addGlobalInfo("Local ativado com sucesso um email será enviado para o mesmo!");
        return "/jsf/pages/protegido/geral/local/locais.xhtml";
    }
    
    @PostConstruct
    public void init() {
    	locais = localService.findAll();
    }

    public void salvar() {
        local = localService.salvar(local);
        Messages.addGlobalInfo("Usuário salvo com sucesso!");
    }

    public List<Local> getLocais() {
        return locais;
    }

    public void setLocais(List<Local> locais) {
        this.locais = locais;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}