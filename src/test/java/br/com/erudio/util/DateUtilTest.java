package br.com.erudio.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.erudio.util.DateUtil;

public class DateUtilTest {
	
	DateUtil dateUtil = new DateUtil();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getStrNumberTest() {
		assertEquals("01", dateUtil.getStrNumber(1));
		assertEquals("04", dateUtil.getStrNumber(4));
		assertEquals("09", dateUtil.getStrNumber(9));
		assertEquals("10", dateUtil.getStrNumber(10));
		assertEquals("31", dateUtil.getStrNumber(31));
	}

}
