/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.mirror.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author   wuheng@iscas.ac.cn
 * @version  0.0.1
 * @since    2023/06/23
 *
 */
class SQLUtilTest {
	
	@Test
	void testValidTable() {
		assertEquals("abcdefghi", SQLUtil.table("abc-def/gh/i-"));
	}
	
	@Test
	void test1() {
		assertEquals("data ->> 'apiVersion'", SQLUtil.jsonKey("apiVersion"));
	}
	
	@Test
	void test2() {
		assertEquals("data -> 'metadata' ->> 'name'", SQLUtil.jsonKey("metadata##name"));
	}
	
	@Test
	void test3() {
		assertEquals("data -> 'metadata' -> 'test' ->> 'name'", SQLUtil.jsonKey("metadata##test##name"));
	}

}
