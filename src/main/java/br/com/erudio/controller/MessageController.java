package br.com.erudio.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.omnifaces.util.Faces;

@Named
@RequestScoped
public class MessageController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<FacesMessage> messages() {
        /**
         * vem como UnmodifiableCollection
         */
        return Lists.newArrayList(Faces.getContext().getMessageList());
    }

    private Collection<Severity> splitSeverities(String severities) {
        Iterable<String> iterable = Splitter.on(" ").omitEmptyStrings()
                .trimResults().split(severities);
        List<Severity> list = new ArrayList<>();
        for (String sev : iterable) {
            list.add(toSeverity(sev));
        }
        return list;
    }

    public List<FacesMessage> messages(String severity) {
        return getMessages(true, splitSeverities(severity));
    }

    public boolean hasMessages(String severity) {
        return !getMessages(false, splitSeverities(severity)).isEmpty();
    }

    private Severity toSeverity(String sev) {
        switch (sev) {
            case "INFO":
                return FacesMessage.SEVERITY_INFO;
            case "WARN":
                return FacesMessage.SEVERITY_WARN;
            case "FATAL":
                return FacesMessage.SEVERITY_FATAL;
            case "ERROR":
                return FacesMessage.SEVERITY_ERROR;
            default:
                return null;
        }
    }

    private List<FacesMessage> getMessages(boolean toRender,
                                           Collection<Severity> severities) {
        List<FacesMessage> msgs = messages();
        Iterator<FacesMessage> it = msgs.iterator();

        while (it.hasNext()) {
            FacesMessage fm = it.next();
            if (!severities.contains(fm.getSeverity())) {
                it.remove();
            } else if (toRender) {
                fm.rendered();
            }
        }

        return msgs;

    }
}
