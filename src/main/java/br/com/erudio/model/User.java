package br.com.erudio.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@NamedQueries({
        @NamedQuery(name = "Usuario.findByEmailAndAtivo", query = "SELECT DISTINCT u FROM Usuario u WHERE u.email = :email AND u.ativo = true ORDER BY u.email")
})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator", discriminatorType=DiscriminatorType.STRING)
//@DiscriminatorFormula("case when Usuario is null then 0 else Usuario end") 
@DiscriminatorValue(value="U")
public class User implements Model<Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "data_hora_criacao")
    private Date dataHoraCriacao;

    @NotNull
    @Column(length = 1000)
    @Size(min = 1, max = 1000)
    private String nome;

    @NotNull
    @NotBlank
    @org.hibernate.validator.constraints.Email
    @Column(length = 1000)
    private String email;

    @NotNull
    @Column(length = 1000)
    private String senha;

    @Column(name = "token_nova_senha", length = 128)
    private String tokenNovaSenha;

    @Column(name = "data_expiracao_token_nova_senha")
    private Date dataExpiracaoTokenNovaSenha;

    @NotNull
    @Column
    private boolean ativo = true;

    @Transient
    private String senhaConfirmacao;

    public User() {
        dataHoraCriacao = LocalDateTime.now().toDate();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(Date dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public String getTokenNovaSenha() {
        return tokenNovaSenha;
    }

    public void setTokenNovaSenha(String tokenNovaSenha) {
        this.tokenNovaSenha = tokenNovaSenha;
    }

    public Date getDataExpiracaoTokenNovaSenha() {
        return dataExpiracaoTokenNovaSenha;
    }

    public void setDataExpiracaoTokenNovaSenha(Date dataExpiracaoTokenNovaSenha) {
        this.dataExpiracaoTokenNovaSenha = dataExpiracaoTokenNovaSenha;
    }

    public String getSenhaConfirmacao() {
        return senhaConfirmacao;
    }

    public void setSenhaConfirmacao(String senhaConfirmacao) {
        this.senhaConfirmacao = senhaConfirmacao;
    }
    
    public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getAtivoString() {
        return ativo ? "SIM" : "NÃO";
    }
}