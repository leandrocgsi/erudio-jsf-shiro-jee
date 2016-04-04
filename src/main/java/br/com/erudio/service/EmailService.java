package br.com.erudio.service;

import com.google.common.base.Objects;

import br.com.erudio.model.AnexoEmail;
import br.com.erudio.model.Email;
import br.com.erudio.model.Usuario;
import br.com.erudio.util.EnvProperties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.*;

@Stateless
public class EmailService extends GenericService<Email, Long> {

    private static final long serialVersionUID = 1L;
    private static final Integer TENTATIVAS_ENVIO = 3;

    @Inject
    private transient Logger logger;
    @Inject
    private EnvProperties envProps;

    @Override
    public Email save(Email e) {
        throw new IllegalStateException("Método não implementado!");
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Email> findNaoEnviadosAindaComTentativasAFazer() {
        TypedQuery<Email> query = getEntityManager().createNamedQuery(
                "Email.findNaoEnviadosAindaComTentativas", Email.class);
        query.setParameter("tentativas", TENTATIVAS_ENVIO);
        List<Email> emails = query.getResultList();
        return emails;

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void gerarEmail(String assunto, String conteudo, String destinatarios) {
        gerarEmail(Email.EMAIL_FROM_DEFAULT, assunto, conteudo, destinatarios, Collections
                .<AnexoEmail>emptyList());
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void gerarEmail(String from, String assunto, String conteudo, String destinatarios, Collection<AnexoEmail> anexos) {
        if (from != null && from.trim().isEmpty()) {
            logger.error("Remetente está vazio. Será usado remetente padrão. Assunto = " + assunto + ", Destinatarios = " + destinatarios);
            from = null;
        }

        Email email = new Email(Objects.firstNonNull(from, Email.EMAIL_FROM_DEFAULT), destinatarios, assunto, conteudo);
        email.addAnexos(anexos);
        super.save(email);
    }

    public void gerarEmail(String assunto, String conteudo, Collection<Usuario> destinatarios) {
        gerarEmail(assunto, conteudo, Email.getDescricaoDestinatarios(destinatarios));
    }

    public String fillEmailTemplateData(Email email) {
        final StringWriter conteudo = new StringWriter();

        VelocityEngine velocityEngine = new VelocityEngine();
        VelocityContext velocityContext = new VelocityContext(getBMWTemplateParameters(email));

        velocityEngine.evaluate(velocityContext, conteudo, "emplacamentoTemplate",
                new InputStreamReader(getClass().getResourceAsStream("/email.vm")));
        return conteudo.toString();
    }

    public Map<String, Object> getBMWTemplateParameters(Email email) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("contextPath", envProps.host());
        parameters.put("url", envProps.host());
        parameters.put("titulo", email.getAssunto());
        parameters.put("logo", envProps.host());
        parameters.put("footer", "LIP Java");

        return parameters;
    }
}
