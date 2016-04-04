package br.com.tecsinapse.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.fileupload.FileUploadRenderer;

/**
 * http://stackoverflow.com/questions/19262356/file-upload-doesnt-work-with-ajax-in-primefaces-4-0-running-on-jsf-2-2-x
 * bug https://code.google.com/p/primefaces/issues/detail?id=6925
 * Remover apos correçao
 */
public class PrimeFileUpload extends FileUploadRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (context.getExternalContext().getRequestContentType().toLowerCase().startsWith("multipart/")) {
            super.decode(context, component);
        }
    }
}
