package org.goodfox.gwola.util.filter;

import org.goodfox.gwola.util.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.goodfox.gwola.util.utils.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加Http缓存过滤器
 */
public class HttpCacheFilter implements Filter {
    private static final Log logger = LogFactory.getLog(HttpCacheFilter.class);
    private long maxAge = 60 * 60 * 24 * 30;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        chain.doFilter(request, new AddExpiresHeaderResponse(request, response, maxAge));
    }

    @Override
    public void init(FilterConfig config) {
        String maxAgeStr = config.getInitParameter("maxAge");
        if (StringUtils.isNotEmpty(maxAgeStr)) {
            maxAge = Long.valueOf(maxAge);
        }
    }

}