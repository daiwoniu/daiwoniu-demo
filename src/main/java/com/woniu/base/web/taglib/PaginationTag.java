package com.woniu.base.web.taglib;

import com.woniu.base.db.Pagination;
import com.woniu.base.db.Pagination.Mode;
import com.woniu.base.web.displaytag.RequestHelperFactory;
import org.displaytag.util.Href;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class PaginationTag extends TagSupport {

	private String name;
	private String css;
	private String uri;

    private boolean hasPreAndNext;
    /**
     * 只有一页的时候是否展示分页标签
     */
    private boolean displayAtOnlyOnePage;

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();

		try {
			HttpServletRequest req = (HttpServletRequest) pageContext
					.getRequest();
			Pagination<Object> pagination = (Pagination<Object>) req
					.getAttribute(name);

			RequestHelperFactory.CustomRequestHelper requestHelper = new RequestHelperFactory.CustomRequestHelper(req,
					(HttpServletResponse) pageContext.getResponse());
			Href href = requestHelper.getHref();
			if (uri != null) {
				href.setFullUrl(uri);
			}

			if (pagination.getMode() == Mode.FULL) {
				this.numberNav(out, pagination, href);
			} else {
				this.nextNav(out, pagination, href);
			}

		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	private void numberNav(JspWriter out, Pagination<Object> pagination,
			Href href) throws IOException {
        if(!displayAtOnlyOnePage && pagination.getTotalPages()==1){
            out.append("<div class=\"").append("pagination").append("\">").append("</div>");
        }else {
            out.append("<div class=\"").append("pagination");
            if (css != null) {
                out.append(" ").append(css);
            }
            out.append("\">");
            out.append("<ul>");
            if (hasPreAndNext) {//是否有上一页下一页的标签
                if (pagination.getTotalPages() > 1) {
                    if(pagination.getPage()>1) {
                        href.addParameter("page", pagination.getPage() - 1);
                    }else{
                        href.addParameter("page",pagination.getPage());
                    }
                    out.append("<li class=\"pre-page\"><a href=\"").append(href.toString()).append("\">").append("<").append("</a></li>");
                }
            }
            for (Integer p : pagination.pageNavigation()) {
                if (p == -1) {
                    out.append("<li class=\"disabled\"><a href=\"#\">...</a></li>");
                } else if (p == pagination.getPage()) {
                    out.append("<li class=\"active\"><a href=\"#\">")
                            .append(p.toString()).append("</a></li>");
                } else {
                    href.addParameter("page", p);
                    out.append("<li><a href=\"").append(href.toString())
                            .append("\">").append(p.toString()).append("</a></li>");
                }
            }
            if (hasPreAndNext) {//是否有上一页下一页的标签
                if (pagination.getTotalPages() > 1) {
                    if(pagination.getTotalPages() == pagination.getPage()){
                        href.addParameter("page",pagination.getPage());
                    }else {
                        href.addParameter("page", pagination.getPage() + 1);
                    }
                    out.append("<li class=\"next-page\"><a href=\"").append(href.toString()).append("\">").append(">").append("</a></li>");
                }
            }
            out.append("</ul>");
            out.append("</div>");
        }
	}

	private void nextNav(JspWriter out, Pagination<Object> pagination, Href href)
			throws IOException {
		out.append("<div class=\"").append("pull-right");
		if (css != null) {
			out.append(" ").append(css);
		}
		out.append("\">");
		if (pagination.getPage() > 1 && pagination.isHasNext()) {
			this.nextNavHelper(out, href, 1, "首页");
			this.nextNavHelper(out, href, pagination.getPage() - 1, "上一页");
			this.nextNavHelper(out, href, pagination.getPage() + 1, "下一页");
		} else if (pagination.getPage() == 1) {
			this.nextNavHelper(out, href, pagination.getPage() + 1, "下一页");
		} else if (!pagination.isHasNext()) {
			this.nextNavHelper(out, href, 1, "首页");
			this.nextNavHelper(out, href, pagination.getPage() - 1, "上一页");
		}
		out.append("</div>");
	}

	private void nextNavHelper(JspWriter out, Href href, int page, String name)
			throws IOException {
		href.addParameter("page", page);
		out.append("<sapn class=\"basic-form\" style=\"padding: 3px 5px\"><a href=\"").append(href.toString()).append("\">")
				.append(name).append("</a></span>");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

    public boolean isHasPreAndNext() {
        return hasPreAndNext;
    }

    public void setHasPreAndNext(boolean hasPreAndNext) {
        this.hasPreAndNext = hasPreAndNext;
    }

    public boolean isDisplayAtOnlyOnePage() {
        return displayAtOnlyOnePage;
    }

    public void setDisplayAtOnlyOnePage(boolean displayAtOnlyOnePage) {
        this.displayAtOnlyOnePage = displayAtOnlyOnePage;
    }
}
