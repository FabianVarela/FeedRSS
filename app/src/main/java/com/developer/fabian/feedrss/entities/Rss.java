package com.developer.fabian.feedrss.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "rss", strict = false)
@Namespace(reference = "http://search.yahoo.com/mrss/")
public class Rss {

    @Element
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
