package com.qnkj.core.base;


import com.qnkj.common.entitys.Layout;
import com.qnkj.common.entitys.OutsideLink;
import com.qnkj.common.entitys.TabField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FlowTaskSettings {
    protected List<Layout> Layouts = new ArrayList<>();
    protected List<TabField> Fields = new ArrayList<>();
    protected Map<String, OutsideLink> OutsideLinks = new HashMap<>();

    public FlowTaskSettings setLayouts(List<Layout> layouts) {
        this.Layouts = layouts;
        return this;
    }
    public List<Layout> getLayouts() {
        return this.Layouts;
    }

    public FlowTaskSettings setFields(List<TabField> fields) {
        this.Fields = fields;
        return this;
    }
    public List<TabField> getFields() {
        return this.Fields;
    }

    public FlowTaskSettings setOutsideLinks(Map<String,OutsideLink> outsideLinks) {
        this.OutsideLinks = outsideLinks;
        return this;
    }
    public Map<String,OutsideLink> getOutsideLinks() {
        return this.OutsideLinks;
    }
}
