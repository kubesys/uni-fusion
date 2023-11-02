package com.qnkj.common.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {

    /**
     * 分割数组
     * @param array 原数组
     * @param Size  分割后每个数组的最大长度
     * @return
     */
    private static  List<List> splitArray(List array, int Size) {
        List<List> lists = new ArrayList<>();
        int i = (array.size()) % Size == 0 ? (array.size()) / Size : (array.size()) / Size + 1;
        for (int j = 0; j < i; j++) {
            List<Object> list1 = new ArrayList<Object>();
            for (int k = 0; k < Size; k++) {
                if ((j * Size + k) >= array.size()) {
                    break;
                } else {
                    list1.add(array.get(j * Size + k));
                }
            }
            lists.add(list1);
        }
        return lists;
    }

    /**
     * 差集
     */
    public static List subtract(List<?> list1,List<?> list2) {
        if (list1 != null && list2 != null) {
           return CollectionUtils.subtract(list1, list2).stream().collect(Collectors.toList());
        } else if (list1 != null ) {
            return list1;
        }
        return list2;
    }

    /**
     * 并集
     */
    public static List union(List<?> list1,List<?> list2) {
        if (list1 != null && list2 != null) {
            return CollectionUtils.union(list1, list2).stream().collect(Collectors.toList());
        } else if (list1 != null ) {
            return list1;
        }
        return list2;
    }

    /**
     * 交集
     */
    public static List intersection(List<?> list1,List<?> list2) {
        if (list1 != null && list2 != null) {
            return CollectionUtils.intersection(list1, list2).stream().collect(Collectors.toList());
        } else if (list1 != null ) {
            return list1;
        }
        return list2;
    }
}
