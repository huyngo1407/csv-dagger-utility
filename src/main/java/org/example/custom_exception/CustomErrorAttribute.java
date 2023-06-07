package org.example.custom_exception;

import org.example.util.DateTimeUtil;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.util.Map;

public class CustomErrorAttribute extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions errorAttributeOptions) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, errorAttributeOptions);
        Object timestampObj = errorAttributes.get("timestamp");
        if (timestampObj == null) {
            errorAttributes.put("timestamp", DateTimeUtil.getCurrentDate());
        } else {
            Timestamp timestamp = (Timestamp) timestampObj;
            errorAttributes.put("timestamp", DateTimeUtil.toLocalDate(timestamp));
        }
        return errorAttributes;
    }
}
