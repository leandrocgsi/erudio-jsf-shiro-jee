package br.com.erudio.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlPanelGroup;

@FacesComponent("br.com.erudio.component.CollapsePanel")
public class CollapsePanel extends HtmlPanelGroup {
    public CollapsePanel() {
        setRendererType("br.com.erudio.component.CollapsePanelRenderer");
    }

    protected enum PropertyKeys {
        headerText,
        collapsed,
        idContent
    }

    @Override
    public String getFamily() {
        return "br.com.erudio.component";
    }

    public String getHeaderText() {
        return (String) getStateHelper().eval(PropertyKeys.headerText);
    }

    public void setHeaderText(String headerText) {
        getStateHelper().put(PropertyKeys.headerText, headerText);
    }

    public boolean isCollapsed() {
        return (boolean) getStateHelper().eval(PropertyKeys.collapsed, true);
    }

    public void setCollapsed(boolean collapsed) {
        getStateHelper().put(PropertyKeys.collapsed, collapsed);
    }

    public String getIdContent() {
        return (String) getStateHelper().eval(PropertyKeys.idContent);
    }

    public void setIdContent(String idContent) {
        getStateHelper().put(PropertyKeys.idContent, idContent);
    }
}