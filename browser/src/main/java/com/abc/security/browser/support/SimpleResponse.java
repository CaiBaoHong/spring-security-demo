package com.abc.security.browser.support;

import java.io.Serializable;

public class SimpleResponse implements Serializable{

    private Object content;

    public SimpleResponse() {

    }

    public SimpleResponse(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
