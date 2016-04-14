package br.com.erudio.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import br.com.erudio.model.AnexoEmail;
import br.com.erudio.model.Email;
import br.com.erudio.util.Constants;
import br.com.erudio.util.EnvProperties;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Singleton
public class EnviaEmailService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private transient Logger logger;

    @Resource(mappedName = "java:jboss/mail/MandrillLipjava")
    private Session mailSession;

    @Inject
    private EmailService emailService;

    @Inject
    private EnvProperties envProps;

    @Schedule(second = "0", minute = "*", hour = "*", persistent = false)
    @AccessTimeout(-1)
    public void enviarEmails() {
        logger.info("agendamento - inicio - enviarEmails");
        List<Email> emails = emailService
                .findNaoEnviadosAindaComTentativasAFazer();

        for (Email email : emails) {
            enviarEmail(email);
        }
        logger.info("agendamento - fim - enviarEmails");
    }

    private void enviarEmail(Email email) {
        try {
            final String assunto = getAssunto(email);

            if (email.getFrom() == null) {
                logger.error("Remetente está null no email " + email.getId());
                return;
            } else if (email.getFrom().trim().isEmpty()) {
                logger.error("Remetente está vazio no email " + email.getId());
                return;
            }


            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(email.getFrom()));
            message.addHeader("X-MC-PreserveRecipients", "false");
            message.addRecipients(RecipientType.CC, getDestinatarios(email));
            message.setSubject(assunto);


            Multipart multipart = new MimeMultipart();

            MimeBodyPart bodyText = new MimeBodyPart();
            bodyText.setContent(emailService.fillEmailTemplateData(email), "text/html; charset=utf-8");
            multipart.addBodyPart(bodyText);

            addAttachments(email, multipart);

            message.setContent(multipart);

            Transport.send(message);

            email.setDataEnvio(new LocalDateTime().toDate());
            email.setEnviado(true);
        } catch (Exception e) {
            logger.error("Erro ao enviar email de id {}", email.getId(), e);
            email.incrementaTentativas();
        }

        emailService.merge(email);
    }

    private String getAssunto(Email email) {
        String assunto = "[ERUDIO Java] ";
        if (!isProd()) {
            assunto += "-> " + envProps.env().toUpperCase();
            assunto += " <- ";
        }
        assunto += email.getAssunto();

        return assunto;
    }

    private boolean isProd() {
        return envProps.env().equals("prod");
    }

    private InternetAddress[] getDestinatarios(Email email) {
        String[] to;
        if (isProd()) {
            to = email.getDestinatariosArray();
        } else {
            Collection<String> destinatarios = Collections2.filter(Arrays.asList(email.getDestinatariosArray()), new Predicate<String>() {
                public boolean apply(String e) {
                    return e.endsWith(Constants.DEFAULT_EMAIL_PREFIX) || envProps.emailsSempreRecebem().contains(e);
                }
            });

            to = destinatarios.isEmpty()
                    ? Iterables.toArray(Constants.COMMA_SPLITTER.split(envProps.emailsPadroes()), String.class)
                    : destinatarios.toArray(new String[0]);
        }

        return FluentIterable.from(Arrays.asList(to))
                .transform(new Function<String, InternetAddress>() {
                    @Override
                    public InternetAddress apply(String email) {
                        try {
                            return new InternetAddress(email);
                        } catch (AddressException e) {
                            throw Throwables.propagate(e);
                        }
                    }
                }).toArray(InternetAddress.class);
    }

    private void addAttachments(Email email, Multipart multipart) throws MessagingException {
        for (AnexoEmail anexo : email.getAnexos()) {
            DataSource source = new FileDataSource(anexo.getNomeArquivoStorage());

            MimeBodyPart part = new MimeBodyPart();
            if (!Strings.isNullOrEmpty(anexo.getContentId())) {
                part.setDisposition(MimeBodyPart.INLINE);
                part.setContentID(anexo.getContentId());
            }
            part.setFileName(anexo.getNomeArquivo());
            part.setDataHandler(new DataHandler(source));

            multipart.addBodyPart(part);
        }
    }
}
