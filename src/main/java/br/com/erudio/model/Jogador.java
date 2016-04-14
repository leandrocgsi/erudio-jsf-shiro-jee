package br.com.erudio.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.erudio.model.enums.TipoJogador;

@Entity
@Table(name = "usuario")
@DiscriminatorValue("J")
public class Jogador extends User {
    
    private static final long serialVersionUID =  1L;   
    
    @Column(name="observacao", length=100)
    private String observacao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoJogador tipoJogador;
    
    @ManyToMany(cascade=CascadeType.ALL)  
    @JoinTable(name="jogador_local", joinColumns=@JoinColumn(name="jogadorId"), inverseJoinColumns=@JoinColumn(name="localId"))  
    private List<Local> locais;
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "jogador")
    private List<SeguidorLocal> seguidores;

    public Jogador() { }

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TipoJogador getTipoJogador() {
		return tipoJogador;
	}

	public void setTipoJogador(TipoJogador tipoJogador) {
		this.tipoJogador = tipoJogador;
	}

	public List<Local> getLocais() {
		return locais;
	}

	public void setLocais(List<Local> locais) {
		this.locais = locais;
	}

	public List<SeguidorLocal> getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(List<SeguidorLocal> seguidores) {
		this.seguidores = seguidores;
	}
}