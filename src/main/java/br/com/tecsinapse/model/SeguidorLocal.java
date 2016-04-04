package br.com.tecsinapse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "seguidor_local")
public class SeguidorLocal implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "jogadorId", nullable=false)
    private Jogador jogador;
    
    @Id
    @Column(name="localId")
    private Integer localId;
    
    @Column(name="isSilencioso")
    private Boolean isSilencioso;

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Integer getLocalId() {
		return localId;
	}

	public void setLocalId(Integer localId) {
		this.localId = localId;
	}

	public Boolean getIsSilencioso() {
		return isSilencioso;
	}

	public void setIsSilencioso(Boolean isSilencioso) {
		this.isSilencioso = isSilencioso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isSilencioso == null) ? 0 : isSilencioso.hashCode());
		result = prime * result + ((jogador == null) ? 0 : jogador.hashCode());
		result = prime * result + ((localId == null) ? 0 : localId.hashCode());
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
		SeguidorLocal other = (SeguidorLocal) obj;
		if (isSilencioso == null) {
			if (other.isSilencioso != null)
				return false;
		} else if (!isSilencioso.equals(other.isSilencioso))
			return false;
		if (jogador == null) {
			if (other.jogador != null)
				return false;
		} else if (!jogador.equals(other.jogador))
			return false;
		if (localId == null) {
			if (other.localId != null)
				return false;
		} else if (!localId.equals(other.localId))
			return false;
		return true;
	}
}