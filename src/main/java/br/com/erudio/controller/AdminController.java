package br.com.erudio.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

import br.com.erudio.interceptor.Logging;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.omnifaces.util.Messages;


@Named
@RequestScoped
@URLMappings(mappings = {
        @URLMapping(id = "admin", pattern = "/admin/", viewId = "/jsf/pages/protegido/admin.xhtml")
})
@Logging
public class AdminController implements Serializable {
    private static final long serialVersionUID = 1L;


    public void gerarErro() {
        //pra teste do logback
        throw new IllegalStateException("Estado inválido! Só pra testar o logback enviando email!");
    }

    public void resetarCache() {
        try {
            CacheManager manager = CacheManager.getInstance();

            String[] names = manager.getCacheNames();

            for (String name : names) {
                Ehcache cache = manager.getEhcache(name);

                cache.removeAll();
            }
            Messages.addGlobalInfo("operacao realizada com sucesso!");
        } catch (CacheException | IllegalStateException e) {
            Messages.addGlobalError("Erro" + e.getMessage());
        }
    }
}
