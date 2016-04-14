package br.com.erudio.model;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import br.com.erudio.model.Model;
import br.com.erudio.model.User;
import br.com.erudio.util.Constants;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

@Entity
@Table(name = "email")
@NamedQuery(name = "Email.findNaoEnviadosAindaComTentativas", query
        = "select d from Email d where d.enviado = false and d.tentativas < :tentativas", hints = {
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "Email.findNaoEnviadosAindaComTentativas")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Email implements Model<Long> {
    public static final String EMAIL_FROM_DEFAULT = "naoresponda@jsf-shiro-jee.com.br";
    @NotBlank
    @org.hibernate.validator.constraints.Email
    @Column(name = "email_from", length = 100)
    private String from = EMAIL_FROM_DEFAULT;
    private static final long serialVersionUID = 1L;
    private static final Joiner COMMA_JOINER = Joiner.on(',').skipNulls();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column
    private String destinatarios;

    @NotNull
    @Column
    private String assunto;

    @NotNull
    @Column
    private boolean fake;

    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column
    private String conteudo;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<AnexoEmail> anexos = new ArrayList<>();

    @NotNull
    @Column
    private int tentativas;

    @NotNull
    @Column
    private boolean enviado = false;

    @NotNull
    @Column
    private boolean html = true;

    @NotNull
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = LocalDateTime.now().toDate();

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvio;

    protected Email() {
    }

    public Email(String from, String destinatarios, String assunto, String conteudo) {
        this.destinatarios = destinatarios;
        this.assunto = assunto;
        this.conteudo = conteudo;
        this.from = from;
    }

    public static String getDescricaoDestinatarios(
            Collection<User> destinatarios) {
        Collection<String> transform
                = Collections2.transform(destinatarios,
                new Function<User, String>() {
                    @Override
                    public String apply(User usuario) {
                        return usuario.getEmail();
                    }
                });
        return COMMA_JOINER.join(transform);
    }

    public String[] getDestinatariosArray() {
        return Iterables
                .toArray(Constants.COMMA_SPLITTER.split(destinatarios), String.class);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public boolean isHtml() {
        return html;
    }

    public int getTentativas() {
        return tentativas;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public void incrementaTentativas() {
        tentativas++;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public List<AnexoEmail> getAnexos() {
        return anexos;
    }

    public void addAnexos(Collection<AnexoEmail> anexosEmail) {
        anexos.addAll(anexosEmail);
        for (AnexoEmail anexoEmail : anexosEmail) {
            anexoEmail.setEmail(this);
        }
    }

    public String getFrom() {
        return from;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Email)) {
            return false;
        }

        final Email other = (Email) obj;
        return Objects.equals(this.id, other.id);
    }
}
