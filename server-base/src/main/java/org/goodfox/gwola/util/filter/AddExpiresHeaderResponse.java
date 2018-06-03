package org.goodfox.gwola.util.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Arrays;
import java.util.Calendar;

public class AddExpiresHeaderResponse extends HttpServletResponseWrapper {
    private static final String[] CACHEABLE_CONTENT_TYPES = new String[]{"text/css", "text/javascript", "application/javascript", "image/png", "image/jpeg", "image/gif", "image/jpg", "application/font-woff2"};
    private long maxAge = 0;

    static {
        Arrays.sort(CACHEABLE_CONTENT_TYPES);
    }

    public AddExpiresHeaderResponse(HttpServletRequest request, HttpServletResponse response, long maxAge) {
        super(response);
        this.maxAge = maxAge;

        if (request.getRequestURI().startsWith("/api")) {
            response.setHeader("Expires", "-1");
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        }
    }

    @Override
    public void setContentType(String contentType) {
        if (contentType != null && Arrays.binarySearch(CACHEABLE_CONTENT_TYPES, contentType) > -1) {
            Calendar inTwoMonths = Calendar.getInstance();
            inTwoMonths.add(Calendar.MONTH, 2);

            super.setDateHeader("Expires", inTwoMonths.getTimeInMillis());
            super.setHeader("Cache-Control" , "max-age=" + maxAge);
        } else {
            super.setHeader("Expires", "-1");
            super.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        }
        super.setContentType(contentType);
    }
}
