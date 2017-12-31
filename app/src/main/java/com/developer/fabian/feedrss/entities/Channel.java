package com.developer.fabian.feedrss.entities;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {

    @ElementList(inline = true)
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }
}
