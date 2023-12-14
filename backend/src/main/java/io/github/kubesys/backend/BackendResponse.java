/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kubesys.devfrk.spring.constants.BeanConstants;
import io.github.kubesys.devfrk.spring.resp.HttpResponse;
import jakarta.annotation.Resource;

/**
 * @author  wuheng@iscas.ac.cn
 * @since   2.2.3
 * 
 * <p>
 * The {@code HttpResponse} class represents the return
 * value should be bound to the web response body.
 */
public class BackendResponse implements HttpResponse {

	/**
	 * logger
	 */
	public static final Logger m_logger = Logger.getLogger(BackendResponse.class.getName());
	
	/**
	 * 上下文信息
	 */
	@Resource
	protected ApplicationContext context;
	
	
	/**
	 * 正常响应
	 */
	static final String SUCESS = "success";
	
	/**
	 * 异常响应
	 */
	static final String FAIL   = "fail";

	/**
	 * 正常ID
	 */
	static final String SUCESS_ID = "-1";
	
	@Override
	public String success(Object obj) {
		try {
			return ((BackendResponse) context
							.getBean(BeanConstants.RESPONSE))
						.unwrap(SUCESS, SUCESS_ID, obj);
		} catch (JsonProcessingException | BeansException e) {
			return null;
		}
	}
	
	@Override
	public String fail(Exception ex) {
		m_logger.warning("handle request error: " + ex);
		try {
			return ((BackendResponse) context.getBean(BeanConstants.RESPONSE))
					.unwrap("fail", "500", ex);
		} catch (Exception e) {
			try {
				return ((BackendResponse) context.getBean(BeanConstants.RESPONSE))
						.unwrap("fail", "300", e);
			} catch (JsonProcessingException | BeansException e1) {
				return fail(e1);
			}
		}
		
	}
	
	public String unwrap(String status, String id, Object value) throws JsonProcessingException {
		
		if (value instanceof Exception ex) {
			value = ex.toString();
		}
		
		HttpResponseData response = FAIL.equals(status) ?
				new HttpResponseData(50000, Integer.parseInt(id), value.toString()) 
				: new HttpResponseData(20000, value);
		
		return new ObjectMapper().writeValueAsString(response);
	}
	
	
	public static class HttpResponseCookie {
		
		protected int maxAge = 3600;
		
		protected String cookie;

		public HttpResponseCookie(String cookie) {
			super();
			this.cookie = cookie;
		}

		public int getMaxAge() {
			return maxAge;
		}

		public void setMaxAge(int maxAge) {
			this.maxAge = maxAge;
		}

		public String getCookie() {
			return cookie;
		}

		public void setCookie(String cookie) {
			this.cookie = cookie;
		}
		
	}
	
	public static class HttpResponseData {
		/**
		 * neither Success or Failure
		 */
		protected int code;
		
		/**
		 * it represents the exception information,
		 * otherwise it should be null
		 */
		protected String message;
		
		/**
		 * Exception Id
		 */
		protected int exId;
		
		/**
		 * if it is not an exception, the response
		 * is the object.
		 */
		protected Object data;
		
		public HttpResponseData(int code, int exId, String message) {
			this.code = code;
			this.exId = exId;
			this.message = message;
		}

		public HttpResponseData(int code, Object data) {
			this.code = code;
			this.data = data;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}

		public int getExId() {
			return exId;
		}

		public void setExId(int exId) {
			this.exId = exId;
		}
		
	}

}
