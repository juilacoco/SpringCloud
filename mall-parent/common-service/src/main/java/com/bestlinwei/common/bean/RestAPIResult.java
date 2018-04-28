
package com.bestlinwei.common.bean;


import com.bestlinwei.common.constant.ReturnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API接口统一响应接口实体
 * @author LINWEI
 * @param <T>
 */
@Data
public class RestAPIResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String returnCode;

	private String msg;

	private Object data;

	public RestAPIResult(String success) {
		this.returnCode = this.returnCode;
	}

	private RestAPIResult(String returnCode, Object result) {
		this.returnCode = returnCode;
		this.data = result;
	}

	private RestAPIResult(String returnCode, String msg, Object result) {
		this.returnCode = returnCode;
		this.msg = msg;
		this.data = result;
	}

	public static RestAPIResult success() {
		return new RestAPIResult(ReturnCode.SUCCESS);
	}

	public static RestAPIResult fail(String returnCode,String msg) {
		return new RestAPIResult(returnCode, msg,null);
	}

	private interface ResultInner {
	}

	/**
	 * 单个对象
	 * @param <T>
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class SingleResultInner<T> implements ResultInner {
		private T obj;
	}

	/**
	 * 集合对象
	 * @param <T>
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class ListResultInner<T> implements ResultInner {
		private List<T> list;
		private Integer total;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MultiResultBuilder {
		private Map<String, Object> data = new HashMap<>();

		public <T> MultiResultBuilder addSingle(String name, T result) {
			data.put(name, new SingleResultInner<T>(result));
			return this;
		}

		public <T> MultiResultBuilder addList(String name, List<T> result, Integer total) {
			data.put(name, new ListResultInner<T>(result, total));
			return this;
		}

		public RestAPIResult build() {
			return new RestAPIResult(ReturnCode.SUCCESS, data);
		}
	}

}
