package br.com.erudio.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.erudio.cdi.annotation.LoggedUser;
import br.com.erudio.model.User;
import br.com.erudio.service.UserService;
import br.com.erudio.util.Token;

import org.omnifaces.util.Messages;

@Named
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "perfil", pattern = "/perfil/", viewId = "/jsf/pages/protegido/geral/perfil/perfil.xhtml")
})
public class PerfilController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private UserService usuarioService;

    @Inject
    @LoggedUser
    private User usuarioLogado;

    @URLAction(mappingId = "perfil", onPostback = false)
    public void idPerfil() {
        usuarioLogado.setSenha(null);
    }

    public void salvarNovaSenha() {
        if (!usuarioLogado.getPassword().equals(usuarioLogado.getSenhaConfirmacao())) {
            Messages.addGlobalWarn("As senhas precisam ser iguais!");
            return;
        }
        usuarioLogado.setSenha(Token.sha256(usuarioLogado.getPassword()));
        usuarioService.salvar(usuarioLogado);
        usuarioLogado.setSenha(null);
        usuarioLogado.setSenhaConfirmacao(null);
        Messages.addGlobalInfo("Senha atualizada!");
    }

    public User getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(User usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
