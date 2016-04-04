package br.com.erudio.service;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;

import br.com.erudio.interceptor.Logging;
import br.com.erudio.model.Usuario;
import br.com.erudio.util.EnvProperties;
import br.com.erudio.util.Token;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Logging
public class UsuarioService extends GenericService<Usuario, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
    private EmailService emailService;

    @Inject
    private EnvProperties envProps;

    public Usuario buscaParaLogin(String email) {
        final TypedQuery<Usuario> findByEmailQuery = getEntityManager().createNamedQuery("Usuario.findByEmailAndAtivo", Usuario.class);
        findByEmailQuery.setParameter("email", email);
        List<Usuario> usuarios = findByEmailQuery.getResultList();
        if (usuarios.isEmpty()) {
            return null;
        }
        return usuarios.get(0);
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Usuario findByTokenNovaSenhaAndNaoExpirado(String tokenNovaSenha) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("tokenNovaSenha", tokenNovaSenha));
        criteria.add(Restrictions.ge("dataExpiracaoTokenNovaSenha",
                new LocalDateTime().toDate()));
        criteria.add(Restrictions.eq("ativo", true));
        criteria.setCacheable(true);
        criteria.setCacheRegion("Usuario.findByTokenNovaSenhaAndNaoExpirado");
        return uniqueByCriteria(criteria);
    }


    public boolean gerarTokenNovaSenhaAndEnviaEmail(String email) {
        Usuario usuario = buscaParaLogin(email);
        if (usuario == null) {
            return false;
        }
        usuario.setTokenNovaSenha(Token.generateCadastrarNovaSenha());
        usuario.setDataExpiracaoTokenNovaSenha(LocalDateTime.now().plusDays(4).toDate());
        usuario = save(usuario);
        enviaEmailEsqueciSenha(usuario);
        return true;

    }

    private void enviaEmailEsqueciSenha(Usuario usuario) {
        StringBuilder texto = new StringBuilder();
        texto.append(
                "Você nos avisou que esqueceu sua senha então precisa cadastrar uma nova, clique <a href=\"");
        texto.append(envProps.host() + "senha/nova/");
        texto.append(usuario.getTokenNovaSenha());
        texto.append(
                "/\">aqui</a> e informe sua nova senha no LIP Java!<br/><br/>Você poderá cadastrar uma nova senha até ");
        texto.append(new LocalDateTime(usuario.getDataExpiracaoTokenNovaSenha())
                .toString("dd/MM/yyyy HH:mm"));
        emailService.gerarEmail("Esqueci a senha", texto.toString(),
                usuario.getEmail());
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            resetSenhaAndEnviaEmailUsuario(usuario);
        }
        return save(usuario);
    }

    public Usuario resetSenhaAndEnviaEmailUsuario(Usuario usuario) {
        final String senha = Token.generatePassword();
        usuario.setSenha(Token.sha256(senha));

        usuario = save(usuario);

        StringBuilder texto = new StringBuilder();

        texto.append("Olá ").append(usuario.getNome()).append(", seja bem-vindo!<br/><br/>");

        texto.append("Seu cadastro foi ativado no LIP Java.<br/><br/>");

        texto.append("Clique ");
        texto.append("<a href=\"").append(envProps.host()).append("\">aqui</a>");
        texto.append(" para acessar o sistema.<br/><br/>");

        texto.append("login: ").append(usuario.getEmail()).append("<br/>");
        texto.append("senha inicial: ").append(senha);

        emailService.gerarEmail("Cadastro Ativo", texto.toString(),
                usuario.getEmail());

        return usuario;
    }
}
