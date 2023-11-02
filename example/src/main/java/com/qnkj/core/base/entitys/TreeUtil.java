package com.qnkj.core.base.entitys;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
public class TreeUtil {

    private static final String TOP_NODE_ID = "0";

    public static <T> MenuTree<T> buildMenuTree(List<MenuTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<MenuTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || pid.isEmpty() || TOP_NODE_ID.equals(pid)) {
                topNodes.add(children);
                return;
            }
            for (MenuTree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    children.setHasParent(true);
                    parent.setHasChild(true);
                    return;
                }
            }
        });

        //过滤掉为空子节点的根节点
        List<MenuTree<T>> rootNodes = new ArrayList<>();
        for(MenuTree<T> item: topNodes){
            if(!item.getChilds().isEmpty()){
                List<MenuTree<T>> subitems = new ArrayList<>();
                for(MenuTree<T> subitem: item.getChilds()){
                    if(!subitem.isHasChild() || !subitem.getChilds().isEmpty()){
                        subitems.add(subitem);
                    }
                }
                item.setChilds(subitems);
                if(!item.getChilds().isEmpty()) {
                    rootNodes.add(item);
                }
            }
        }
        MenuTree<T> root = new MenuTree<>();
        root.setId(TOP_NODE_ID);
        root.setParentId(StringUtils.EMPTY);
        root.setHasParent(false);
        root.setHasChild(true);
        root.setChecked(true);
        root.setChilds(rootNodes);
        Map<String, Object> state = new HashMap<>(16);
        root.setState(state);
        return root;
    }


    public static <T> List<MenuTree<T>> buildList(List<MenuTree<T>> nodes, String idParam) {
        if (nodes == null) {
            return new ArrayList<>();
        }
        List<MenuTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);
                return;
            }
            nodes.forEach(parent -> {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    children.setHasParent(true);
                    parent.setHasChild(true);
                }
            });
        });
        return topNodes;
    }
}