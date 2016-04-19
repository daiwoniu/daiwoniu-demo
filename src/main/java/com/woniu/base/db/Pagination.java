package com.woniu.base.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woniu.base.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination<T> {

	public static enum Mode {
		FULL, NEXT_ONLY
	}

	public static final int DEFAULT_PER_PAGE = 10;
	/**
	 * 页数从1开始.
	 */
	@JsonProperty
	private int page;
	@JsonProperty
    private int perPage;
	@JsonProperty
    private int total;
	@JsonProperty
    private boolean hasNext;
	@JsonProperty
    private Mode mode;
	@JsonProperty
	private List<T> data = new ArrayList<T>();

	public Pagination(int page, int perPage, int total) {
		this.page = page < 1 ? 1 : page;
		this.perPage = perPage;
		this.total = total;
		this.mode = Mode.FULL;
		this.hasNext = false;
	}

	public Pagination(int page, boolean hasNext) {
		this.page = page;
		this.hasNext = hasNext;
		this.mode = Mode.NEXT_ONLY;
		this.perPage = -1;
		this.total = -1;
	}

    public Pagination() {
    }

    public void setData(List<T> data) {
		this.data.clear();
		this.data.addAll(data);
	}

	public List<T> getData() {
		return data;
	}

    public int getTotalPages() {
        return (total + perPage - 1) / perPage;
    }

	/**
	 * start from 1;
	 * 
	 * @return
	 */
	public int offset() {
		return (page - 1) * perPage + 1;
	}

	public int nextPage() {
		if (page < getTotalPages()) {
			return page + 1;
		}
		return -1;
	}

	public int previousPage() {
		return page <= 1 ? -1 : page - 1;
	}

	public <V> Pagination<V> replace(List<V> data) {
		Pagination<V> result = new Pagination<V>(page, perPage, total);
		if (mode == Mode.NEXT_ONLY) {
			result = new Pagination<>(page, hasNext);
		}
		result.setData(data);
		return result;
	}

	public List<Integer> pageNavigation() {
		List<Integer> pages = new ArrayList<Integer>();
		int min = 1;
		int max = getTotalPages();

		int innerWindow = 2;
		int from = Math.max(min, page - innerWindow);
		int to = Math.min(max, page + innerWindow);

		boolean leftGap = min + 2 < from;
		boolean rightGap = to + 2 < max;
		if (!leftGap) {
			from = min;
		}
		if (!rightGap) {
			to = max;
		}

		if (min != from) {
			pages.add(min);
		}
		if (leftGap) {
			pages.add(-1);
		}
		for (int i = from; i <= to; i++) {
			pages.add(i);
		}
		if (rightGap) {
			pages.add(-1);
		}
		if (max != to) {
			pages.add(max);
		}

		return pages;
	}

	public int getPage() {
		return page;
	}

	public int getPerPage() {
		return perPage;
	}

	public int getTotal() {
		return total;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public Mode getMode() {
		return mode;
	}

	@Override
	public String toString() {
		return "Pagination [page=" + page + ", perPage=" + perPage + ", total="
				+ total + ", data=" + data + "]";
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pagination that = (Pagination) o;

        if (hasNext != that.hasNext) return false;
        if (page != that.page) return false;
        if (perPage != that.perPage) return false;
        if (total != that.total) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (mode != that.mode) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + perPage;
        result = 31 * result + total;
        result = 31 * result + (hasNext ? 1 : 0);
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public String toJson() {
		return JsonUtil.dump(this);
	}

    public static <T> Pagination<T> fromJson(String json, Class<T> klass) {
        try {
            ObjectMapper mapper = JsonUtil.getMapper();

            JavaType javaType = mapper.getTypeFactory().constructParametricType(Pagination.class, klass);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new JsonUtil.CodecException(e);
        }
    }
}
