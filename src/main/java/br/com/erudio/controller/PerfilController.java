package br.com.erudio.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.erudio.cdi.annotation.UsuarioLogado;
import br.com.erudio.model.Usuario;
import br.com.erudio.service.UsuarioService;
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
    private UsuarioService usuarioService;

    @Inject
    @UsuarioLogado
    private Usuario usuarioLogado;

    @URLAction(mappingId = "perfil", onPostback = false)
    public void idPerfil() {
        usuarioLogado.setSenha(null);
    }

    public void salvarNovaSenha() {
        if (!usuarioLogado.getSenha().equals(usuarioLogado.getSenhaConfirmacao())) {
            Messages.addGlobalWarn("As senhas precisam ser iguais!");
            return;
        }
        usuarioLogado.setSenha(Token.sha256(usuarioLogado.getSenha()));
        usuarioService.salvar(usuarioLogado);
        usuarioLogado.setSenha(null);
        usuarioLogado.setSenhaConfirmacao(null);
        Messages.addGlobalInfo("Senha atualizada!");
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
