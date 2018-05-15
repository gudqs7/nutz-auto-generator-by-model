package cn.oa.cyb.base.pojo;

import java.util.concurrent.ConcurrentHashMap;

public class MapBean extends ConcurrentHashMap<String, Object> {

	private static final long serialVersionUID = -4814930088097871554L;

	public MapBean() {
	}

	public MapBean(Object... props) {
		put(props);
	}

	public Integer getInteger(Object key) {
		return (Integer) get(key);
	}

	public Integer getInteger(Object key, int defaultValue) {
		Integer i = (Integer) get(key);
		return (i == null ? defaultValue : i);
	}

	public Long getLong(Object key) {
		return (Long) get(key);
	}

	public Long getLong(Object key, long defaultValue) {
		Long i = (Long) get(key);
		return (i == null ? defaultValue : i);
	}

	public String getString(Object key) {
		return (String) get(key);
	}

	public String getString(Object key, String defaultValue) {
		String i = (String) get(key);
		return (i == null ? defaultValue : i);
	}

	public void put(Object... props) {
		for (int i = 1; i < props.length; i += 2) {
			put(String.valueOf(props[i - 1]), props[i]);
		}
	}
}