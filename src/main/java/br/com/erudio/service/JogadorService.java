package br.com.erudio.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.erudio.interceptor.Logging;
import br.com.erudio.model.Jogador;
import br.com.erudio.model.Local;
import br.com.erudio.model.SeguidorLocal;
import br.com.erudio.util.EnvProperties;

@Stateless
@Logging
public class JogadorService extends GenericService<Jogador, Long> {
	
	private static final long serialVersionUID = 1L;
	
    @Inject
    private EnvProperties envProps;

	@Inject
    private EmailService emailService;
    
    public void deletar(Long id) {
    	Jogador jogador = findById(id);
		delete(jogador);
    }

    public Jogador findJogadorById(Long id) {
        return findById(id);
    }
    
    public ArrayList<Jogador> findAllJogadores() {
    	ArrayList<Jogador> jogadors = (ArrayList<Jogador>) findAll();
        if (jogadors.isEmpty()) {
            return null;
        }
        return jogadors;
    }

	public void gravaLocaisJogos(List<Local> locais, Integer id) {
		
	}

	public void seguirLocaisJogos(List<Local> locais, Integer id, Boolean silencioso) {
		for (Local local : locais) {
			SeguidorLocal seguidorLocal = new SeguidorLocal();
			seguidorLocal.setIsSilencioso(silencioso);
			seguidorLocal.setJogador(findById(id.longValue()));;
			seguidorLocal.setLocalId(local.getId());
			getEntityManager().merge(seguidorLocal);
		}
	}

	public Jogador bloquearDesbloquearJogador(Jogador jogador, boolean ativo) {
		String enderecoDeEmail = jogador.getEmail();
		jogador.setAtivo(ativo);
        jogador = save(jogador);
        StringBuilder texto = geraTextoEmail(jogador);
		gerarEmail(texto, enderecoDeEmail);
        return jogador;
	}

	private void gerarEmail(StringBuilder texto, String email) {
		emailService.gerarEmail("Jogador Ativo", texto.toString(), email);
	}

	private StringBuilder geraTextoEmail(Jogador jogador) {
		StringBuilder texto = new StringBuilder();
        texto.append("Olá ").append(jogador.getNome()).append(", seja bem-vindo!<br/><br/>");
        texto.append("Você agora pode gerenciar seus jogos no LIP Java.<br/><br/>");
        texto.append("Clique ");
        texto.append("<a href=\"").append(envProps.host()).append("\">aqui</a>");
        texto.append(" para acessar o sistema.<br/><br/>");
		return texto;
	}
}
