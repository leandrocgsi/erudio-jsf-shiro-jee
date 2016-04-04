package br.com.tecsinapse.service;

import java.util.ArrayList;

import javax.ejb.Stateless;

import br.com.tecsinapse.interceptor.Logging;
import br.com.tecsinapse.model.Quadra;

@Stateless
@Logging
public class QuadraService extends GenericService<Quadra, Integer> {
	
	private static final long serialVersionUID = 1L;

	public Quadra salvar(Quadra quadra) {
        return save(quadra);
    }
    
    public void deletar(Integer id) {
    	Quadra quadra = findById(id);
		delete(quadra);
    }

    public Quadra findQuadraById(Integer id) {
        return findById(id);
    }
    
    public ArrayList<Quadra> findAllQuadras() {
    	ArrayList<Quadra> quadras = (ArrayList<Quadra>) findAll();
        if (quadras.isEmpty()) {
            return null;
        }
        return quadras;
    }
}
