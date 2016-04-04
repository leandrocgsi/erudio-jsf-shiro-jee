package br.com.tecsinapse.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import org.omnifaces.util.Messages;

import br.com.tecsinapse.cdi.annotation.UsuarioLogado;
import br.com.tecsinapse.model.Usuario;
import br.com.tecsinapse.service.UsuarioService;
import br.com.tecsinapse.util.Token;

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
