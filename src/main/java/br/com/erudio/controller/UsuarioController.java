package br.com.erudio.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.erudio.model.User;
import br.com.erudio.service.UserService;

import org.omnifaces.util.Messages;

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
    private UserService usuarioService;

    private List<User> usuarios;
    private Long usuarioId;
    private User usuario;

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
        usuario = new User();
    }

    public void salvar() {
        usuario = usuarioService.salvar(usuario);
        Messages.addGlobalInfo("Usuário salvo com sucesso!");
    }

    public void resetarSenha() {
        usuario = usuarioService.resetSenhaAndEnviaEmailUsuario(usuario);
        Messages.addGlobalInfo("Senha resetada com sucesso e o email com a nova senha será enviada!");
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
