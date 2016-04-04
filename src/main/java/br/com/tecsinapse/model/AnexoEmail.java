package br.com.tecsinapse.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "anexo_email")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnexoEmail implements Model<Long> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "email_pk")
    private Email email;

    @NotBlank
    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @NotBlank
    @Column(name = "nome_arquivo_storage")
    private String nomeArquivoStorage;

    @Column(name = "content_id")
    private String contentId;

    protected AnexoEmail() {
    }

    public AnexoEmail(String nomeArquivo, String nomeArquivoStorage) {
        this.nomeArquivo = nomeArquivo;
        this.nomeArquivoStorage = nomeArquivoStorage;
    }

    public AnexoEmail(String contentId, String nomeArquivo,
                      String nomeArquivoStorage) {
        this(nomeArquivo, nomeArquivoStorage);
        this.contentId = contentId;
    }

    public static AnexoEmail fromLocalFile(File file) {
        return new AnexoEmail(UUID.randomUUID().toString(), file.getName(), file.getPath());
    }

    @Override
    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    void setEmail(Email email) {
        this.email = email;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getContentId() {
        return contentId;
    }

    public String getNomeArquivoStorage() {
        return nomeArquivoStorage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, nomeArquivoStorage);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnexoEmail)) {
            return false;
        }
        final AnexoEmail other = (AnexoEmail) obj;

        return Objects.equals(this.nomeArquivoStorage, other.nomeArquivoStorage)
                && Objects.equals(this.email, other.email);
    }
}
