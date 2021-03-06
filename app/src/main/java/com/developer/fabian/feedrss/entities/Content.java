package com.developer.fabian.feedrss.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "content", strict = false)
public class Content {

    @Attribute(name = "url")
    private String url;

    public String getUrl() {
        return url;
    }
}
