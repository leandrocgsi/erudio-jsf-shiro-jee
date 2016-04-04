package br.com.tecsinapse.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;

import org.omnifaces.converter.SelectItemsConverter;

import br.com.tecsinapse.model.Model;

@FacesConverter("genericEntityConverter")
public class GenericEntityConverter extends SelectItemsConverter {

    @Override
    @SuppressWarnings("unchecked")
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Serializable id = (value instanceof Model<?>) ? ((Model<Serializable>) value).getId() : null;
        return (id != null) ? id.toString() : null;
    }

}