package br.com.tecsinapse.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalDateTime;

/**
 * http://blog.bdoughan.com/2011/05/jaxb-and-joda-time-dates-and-times.html
 */
public class LocalDateTimeAdapter
        extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return new LocalDateTime(v);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return v.toString();
    }

}