package br.com.erudio.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.ejb.Stateless;

@Stateless
public class DateUtil {
	
	public String getStrNumber(Integer number) {
		if (number < 10) return "0" + number;
		return number.toString();
	}
	
	public Date obterDataAPartirDeString(String data){
		LocalDate localDate = LocalDate.parse(data);
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}