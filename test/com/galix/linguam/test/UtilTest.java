package com.galix.linguam.test;

import junit.framework.Assert;

import org.junit.Test;
import com.galix.linguam.util.Util;


public class UtilTest {

	@Test
	public void testAuth() {	
		Assert.assertEquals("SACK", Util.trimWord("SACK, REPLACE: SACK STH"));
	}
}
