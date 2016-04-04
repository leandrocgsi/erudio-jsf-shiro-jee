package br.com.tecsinapse.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.LocalDateTime;
import org.omnifaces.util.Messages;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.tecsinapse.model.Jogador;
import br.com.tecsinapse.model.enums.TipoJogador;
import br.com.tecsinapse.service.JogadorService;
import br.com.tecsinapse.util.Token;

@Named
@ViewScoped
@URLMappings(mappings = {
        @URLMapping(id = "jogadores", pattern = "/jogadores/", viewId = "/jsf/pages/protegido/geral/jogador/jogadores.xhtml"),
        @URLMapping(id = "jogador", pattern = "/jogador/id/#{jogadorController.jogadorId}/", viewId = "/jsf/pages/protegido/geral/jogador/jogador.xhtml"),
        @URLMapping(id = "jogador-ativar", pattern = "/jogadores/id/#{jogadorController.jogadorId}/", viewId = "/jsf/pages/protegido/geral/jogador/jogadores.xhtml"),
        @URLMapping(id = "jogador-bloquear", pattern = "/jogadores/id/#{jogadorController.jogadorId}/", viewId = "/jsf/pages/protegido/geral/jogador/jogadores.xhtml"),
        @URLMapping(id = "jogador-novo", pattern = "/jogador/novo/", viewId = "/jsf/pages/protegido/geral/jogador/jogador.xhtml")
})
public class JogadorController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private JogadorService jogadorService;
    
    private List<TipoJogador> tipoJogadors;

    @PostConstruct
    public void init() {
        tipoJogadors = Arrays.asList(TipoJogador.values());
    }

    private List<Jogador> jogadores;
    private Long jogadorId;
    private Jogador jogador;

    @URLAction(mappingId = "jogadores", onPostback = false)
    public void idJogadores() {
        jogadores = jogadorService.findAll();
    }

    @URLAction(mappingId = "jogador", onPostback = false)
    public void idJogador() {
        jogador = jogadorService.findById(jogadorId);
    }
    
    @URLAction(mappingId = "jogador-ativar", onPostback = false)
    public String idJogadorAtivar() {
    	jogador = jogadorService.findById(jogadorId);
    	jogador = jogadorService.bloquearDesbloquearJogador(jogador, true);
    	jogadores = jogadorService.findAll();
        Messages.addGlobalInfo("Jogador ativado com sucesso um email será enviado para o mesmo!");
        return "/jsf/pages/protegido/geral/jogador/jogadores.xhtml";
    }
    
    @URLAction(mappingId = "jogador-bloquear", onPostback = false)
    public String idJogadorBloquear() {
    	jogador = jogadorService.findById(jogadorId);
    	jogador = jogadorService.bloquearDesbloquearJogador(jogador, false);
    	jogadores = jogadorService.findAll();
    	Messages.addGlobalInfo("Jogador bloqueado com sucesso!");
    	return "/jsf/pages/protegido/geral/jogador/jogadores.xhtml";
    }

    @URLAction(mappingId = "jogador-novo", onPostback = false)
    public void idJogadorNovo() {
        jogador = new Jogador();
    }

    public String gravar() {
        validarSenhas();
        try {
        	jogador.setAtivo(false);
        	jogador.setTokenNovaSenha(Token.generateCadastrarNovaSenha());
        	jogador.setDataExpiracaoTokenNovaSenha(LocalDateTime.now().plusDays(4).toDate());
        	jogador.setSenha(Token.sha256(jogador.getSenha()));
        	jogadorService.save(jogador);
        	Messages.addGlobalInfo("Jogador cadastrado com sucesso! Você será notificado por email.");
			return "/jogadores/";
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

	private void validarSenhas() {
		if (!jogador.getSenha().equals(jogador.getSenhaConfirmacao())) {
            Messages.addGlobalWarn("As senhas devem ser iguais!");
        }
	}

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

	public Long getJogadorId() {
        return jogadorId;
    }

    public void setJogadorId(Long jogadorId) {
        this.jogadorId = jogadorId;
    }

    public List<TipoJogador> getTipoJogadors() {
        return tipoJogadors;
    }

	public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}
