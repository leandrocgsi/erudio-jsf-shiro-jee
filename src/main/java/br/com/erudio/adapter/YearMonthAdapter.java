package br.com.erudio.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.YearMonth;

public class YearMonthAdapter
        extends XmlAdapter<String, YearMonth> {

    @Override
    public YearMonth unmarshal(String v) throws Exception {
        return new YearMonth(v);
    }

    @Override
    public String marshal(YearMonth v) throws Exception {
        return v.toString();
    }

}