package com.woniu.base.web;

import com.woniu.base.db.Query;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.displaytag.tags.TableTagParameters;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryForm {
	private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^d-(\\d+)-s");

	private Map<String, String[]> conditions = Maps.newHashMap();
	private String orderBy;
	private int page = 1;

	public boolean contains(String name) {
		return conditions.containsKey(name);
	}

	public boolean containsMultiValue(String name) {
		String[] values = conditions.get(name);
		return values != null && values.length > 1;
	}

	public void set(String name, String value) {
		conditions.put(name, new String[] { value });
	}

	public void set(String name, String[] value) {
		conditions.put(name, value);
	}

	public String get(String name) {
		String values[] = conditions.get(name);
		if (values != null) {
			return values[0];
		}
		return null;
	}

	public String[] getValues(String name) {
		return conditions.get(name);
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Map<String, String[]> getConditions() {
		return conditions;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setupLikeConditions(Query q, String... names) {
		for (String name : names) {
			if (!contains(name)) {
				continue;
			}
			q.like(name, "%" + get(name) + "%");
		}
	}

	public void setupEqConditions(Query q, String... names) {
		for (String name : names) {
			if (!contains(name)) {
				continue;
			}

			if (containsMultiValue(name)) {
				q.where(name, getValues(name));
			} else {
				q.where(name, get(name));
			}
		}
	}

	private static String detectTableId(HttpServletRequest req) {
		// d-149522-s
		for (String name : req.getParameterMap().keySet()) {
			Matcher m = TABLE_NAME_PATTERN.matcher(name);
			if (m.find()) {
				return m.group(1);
			}
		}
		return null;
	}

	public static QueryForm build(HttpServletRequest req) {
		QueryForm q = new QueryForm();

		String tableId = detectTableId(req);
		if (tableId != null) {
			String sort = req.getParameter("d-" + tableId + "-"
					+ TableTagParameters.PARAMETER_SORT);
			String order = req.getParameter("d-" + tableId + "-"
					+ TableTagParameters.PARAMETER_ORDER);
			order = "2".equals(order) ? "!" : "";
			if (sort != null) {
				q.setOrderBy(sort + order);
			}
		}

		String page = req.getParameter("page");
		if (!Strings.isNullOrEmpty(page)) {
			q.setPage(Integer.parseInt(page));
		}

		// override sortBy by if order present
		String order = req.getParameter("order");
		if (!Strings.isNullOrEmpty(order)) {
			q.setOrderBy(order);
		}

		Enumeration<String> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			if ("page".equals(name) || "order".equals(name)) {
				continue;
			}
			if (Strings.isNullOrEmpty(req.getParameter(name))) {
				continue;
			}
			String values[] = req.getParameterValues(name);
			q.set(name, values);
		}

		return q;
	}
}
