package br.com.tecsinapse.service;

import java.util.ArrayList;

import javax.ejb.Stateless;

import br.com.tecsinapse.interceptor.Logging;
import br.com.tecsinapse.model.Local;
import br.com.tecsinapse.model.Local;

@Stateless
@Logging
public class LocalService extends GenericService<Local, Integer> {
	
	private static final long serialVersionUID = 1L;

	public Local salvar(Local local) {
        return save(local);
    }
    
    public void deletar(Integer id) {
    	Local local = findById(id);
		delete(local);
    }

    public Local findLocalById(Integer id) {
        return findById(id);
    }
    
    public ArrayList<Local> findAllLocais() {
    	ArrayList<Local> locais = (ArrayList<Local>) findAll();
        if (locais.isEmpty()) {
            return null;
        }
        return locais;
    }
    
	public Local desbloquearLocal(Local local, boolean ativo) {
		local.setStatus(ativo);
        local = save(local);
        return local;
	}
}
