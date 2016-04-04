package br.com.tecsinapse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name="local")
public class Local implements Serializable, Model<Integer> {
    
    private static final long serialVersionUID =  1L;   
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    
    @Column(name="Descricao", unique=true, nullable=false, length=100)
    private String descricao;
    
    @Column(name="status")
    private Boolean status;
    
    @Column(name="destaqueAte")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date destaqueAte;
    
    @Column(name="cidade", length=100)
    private String cidade;
    
    @Column(name="cep", length=10)
    private String cep;
    
    @Column(name="bairro", length=100)
    private String bairro;
    
    @Column(name="rua", length=100)
    private String rua;
    
    @Column(name="numero")
    private Integer numero;
    
    @Column(name="complemento", length=20)
    private String complemento;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "local")
    private List<Quadra> quadras;

    public Local() { }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getDestaqueAte() {
		return destaqueAte;
	}

	public void setDestaqueAte(Date destaqueAte) {
		this.destaqueAte = destaqueAte;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public List<Quadra> getQuadras() {
		return quadras;
	}

	public void setQuadras(List<Quadra> quadras) {
		this.quadras = quadras;
	}
	
	public String getAtivoString() {
        return status ? "SIM" : "NÃO";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Local other = (Local) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}