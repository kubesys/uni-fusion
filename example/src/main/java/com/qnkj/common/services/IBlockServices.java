package com.qnkj.common.services;

import com.qnkj.common.entitys.Block;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IBlockServices {

    void clear();
    void clear(String modulename);
    void clear(String modulename,String blockid);

    Boolean isExist(Block block);

    Block get(String modulename,String blockid);

    Block load(String blockid);

    List<Block> list(String modulename);

    void update(Block block) throws Exception;

    void delete(String blockid) throws Exception;
}
