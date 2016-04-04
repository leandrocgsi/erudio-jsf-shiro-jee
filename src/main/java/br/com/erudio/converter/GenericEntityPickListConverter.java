package br.com.erudio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.List;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import br.com.erudio.model.Model;

@FacesConverter(value = "genericEntityPickListConverter")
public class GenericEntityPickListConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        DualListModel<?> values = (DualListModel<?>) ((PickList) uiComponent).getValue();

        Object v = findObject(facesContext, uiComponent, value, values.getSource());

        if (v == null) {
            v = findObject(facesContext, uiComponent, value, values.getTarget());
        }

        return v;
    }

    private Object findObject(FacesContext facesContext, UIComponent uiComponent, String value, List<?> values) {
        for (Object s : values) {
            final String convertedItemValue = getAsString(facesContext, uiComponent, s);
            if (value == null ? convertedItemValue == null : value.equals(convertedItemValue)) {
                return s;
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Serializable id = (value instanceof Model<?>) ? ((Model<Serializable>) value).getId() : null;
        return (id != null) ? id.toString() : null;
    }


}