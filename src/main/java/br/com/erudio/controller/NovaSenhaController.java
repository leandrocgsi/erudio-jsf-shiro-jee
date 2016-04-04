package br.com.erudio.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.google.common.base.Strings;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.erudio.jsf.JsfUtil;
import br.com.erudio.model.Usuario;
import br.com.erudio.service.UsuarioService;
import br.com.erudio.util.Token;

import org.omnifaces.util.Messages;
import org.slf4j.Logger;

@Named
@RequestScoped
@URLMappings(mappings = {
        @URLMapping(id = "nova-senha", pattern = "/senha/nova/#{novaSenhaController.tokenNovaSenha}/", viewId = "/jsf/pages/livre/senha-nova.xhtml"),
        @URLMapping(id = "esqueci-senha", pattern = "/senha/esqueci/", viewId = "/jsf/pages/livre/senha-esqueci.xhtml")})
public class NovaSenhaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private transient Logger logger;

    @Inject
    private UsuarioService usuarioService;

    private String tokenNovaSenha;

    private Usuario usuario;

    private String emailCadastrado;
    private boolean enviado;

    @URLAction(mappingId = "nova-senha", onPostback = false)
    public void novaSenha() {
        if (!Strings.isNullOrEmpty(tokenNovaSenha)) {
            usuario = usuarioService
                    .findByTokenNovaSenhaAndNaoExpirado(tokenNovaSenha);
            enviado = false;
            if (usuario != null) {
                return;
            }
        }
        Messages.addGlobalWarn("Seu tempo de cadastrar nova senha expirou! Para cadastrar uma nova senha clique <a href=\""
                + JsfUtil.getContextPath() + "/senha/esqueci/\">aqui</a>");
    }

    public void esqueciSenha() {
        if (Strings.isNullOrEmpty(emailCadastrado)) {
            Messages.addGlobalWarn("Informe o email de seu usuário para receber informações de como criar uma nova senha!");
            return;
        }

        if (usuarioService.gerarTokenNovaSenhaAndEnviaEmail(emailCadastrado)) {
            Messages.addGlobalInfo("Em instantes você receberá um email explicando como criar uma nova senha!");
            emailCadastrado = "";
            enviado = true;
        } else {
            Messages.addGlobalWarn("O email \"" + emailCadastrado
                    + "\" não está cadastrado no sistema!");
        }
    }

    public void salvarNovaSenha() {
        if (!usuario.getSenha().equals(usuario.getSenhaConfirmacao())) {
            Messages.addGlobalWarn("As senhas devem ser iguais!");
            return;
        }

        usuario.setSenha(Token.sha256(usuario.getSenha()));
        usuarioService.save(usuario);
        Messages.addGlobalInfo("Nova senha salva com sucesso!");
    }

    public String getTokenNovaSenha() {
        return tokenNovaSenha;
    }

    public void setTokenNovaSenha(String tokenNovaSenha) {
        this.tokenNovaSenha = tokenNovaSenha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmailCadastrado() {
        return emailCadastrado;
    }

    public void setEmailCadastrado(String emailCadastrado) {
        this.emailCadastrado = emailCadastrado;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

}
