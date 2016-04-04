package br.com.tecsinapse.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import org.omnifaces.util.Messages;

import br.com.tecsinapse.model.Usuario;
import br.com.tecsinapse.service.UsuarioService;

@Named
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "usuarios", pattern = "/usuarios/", viewId = "/jsf/pages/protegido/geral/usuario/usuarios.xhtml"),
        @URLMapping(id = "usuario", pattern = "/usuario/id/#{usuarioController.usuarioId}/", viewId = "/jsf/pages/protegido/geral/usuario/usuario.xhtml"),
        @URLMapping(id = "usuario-novo", pattern = "/usuario/novo/", viewId = "/jsf/pages/protegido/geral/usuario/usuario.xhtml")
})
public class UsuarioController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private List<Usuario> usuarios;
    private Long usuarioId;
    private Usuario usuario;

    @URLAction(mappingId = "usuarios", onPostback = false)
    public void idUsuarios() {
        usuarios = usuarioService.findAll();
    }

    @URLAction(mappingId = "usuario", onPostback = false)
    public void idUsuario() {
        usuario = usuarioService.findById(usuarioId);
    }

    @URLAction(mappingId = "usuario-novo", onPostback = false)
    public void idUsuarioNovo() {
        usuario = new Usuario();
    }

    public void salvar() {
        usuario = usuarioService.salvar(usuario);
        Messages.addGlobalInfo("Usuário salvo com sucesso!");
    }

    public void resetarSenha() {
        usuario = usuarioService.resetSenhaAndEnviaEmailUsuario(usuario);
        Messages.addGlobalInfo("Senha resetada com sucesso e o email com a nova senha será enviada!");
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
