package br.com.erudio.component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;

@FacesRenderer(componentFamily = "br.com.erudio.component", rendererType = "br.com.erudio.component.CollapsePanelRenderer")
public class CollapsePanelRenderer extends Renderer {

    private String escapeToJquery(String value) {
        return value.replaceAll(":", "\\\\:");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    	
        super.encodeBegin(context, component);
        
        final CollapsePanel collapsePanel = (CollapsePanel) component;

        final String accordionId = component.getClientId();
        final String collapseId = collapsePanel.getIdContent() != null ? collapsePanel.getIdContent() : accordionId.replaceAll(":", "_") + "_collapse";

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", component);
        writer.writeAttribute("id", accordionId, "id");
        writer.writeAttribute("class", "panel-group", null);

        writer.startElement("div", null);
        writer.writeAttribute("class", "panel panel-default", null);

        writer.startElement("div", null);
        writer.writeAttribute("class", "panel-heading", null);

        writer.startElement("h4", null);
        writer.writeAttribute("class", "panel-title", null);

        encodeA(context, writer, collapsePanel, accordionId, collapseId);

        writer.endElement("h4");

        writer.endElement("div");

        writer.startElement("div", null);
        writer.writeAttribute("id", collapseId, null);
        writer.writeAttribute("class", "panel-collapse collapse" + (!collapsePanel
                .isCollapsed() ? " in" : ""), null);

        writer.startElement("div", null);
        writer.writeAttribute("class", "panel-body", null);
    }

    private void encodeA(final FacesContext context, final ResponseWriter writer, final CollapsePanel collapsePanel, String accordionId, String collapseId) throws IOException {
    	
        writer.startElement("a", null);
        writer.writeAttribute("data-toggle", "collapse", null);
        writer.writeAttribute("data-parent", "#" + escapeToJquery(accordionId), null);
        writer.writeAttribute("href", "#" + collapseId, null);

        final UIComponent headerFacet = collapsePanel.getFacet("header");
        if (headerFacet != null) headerFacet.encodeAll(context);
        else if (collapsePanel.getHeaderText() != null) writer.writeText(collapsePanel.getHeaderText(), null);

        writer.endElement("a");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    	
        super.encodeEnd(context, component);
        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");

    }
}