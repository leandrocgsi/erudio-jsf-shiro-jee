package br.com.tecsinapse.adapter;

import org.joda.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * http://blog.bdoughan.com/2011/05/jaxb-and-joda-time-dates-and-times.html
 */
public class LocalDateAdapter
        extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return new LocalDate(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }

}