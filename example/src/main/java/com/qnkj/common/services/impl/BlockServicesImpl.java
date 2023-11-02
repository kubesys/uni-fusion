package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.Block;
import com.qnkj.common.services.IBlockServices;
import com.qnkj.common.services.ITabFieldServices;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

@Slf4j
@Service
public class BlockServicesImpl implements IBlockServices {
    private final ITabFieldServices fieldServices;
    private static Map<String, List<Block>> cacheBlocks = new HashMap<>();

    public BlockServicesImpl(ITabFieldServices fieldServices) {
        this.fieldServices = fieldServices;
    }

    private String getKey(String modulename) {
        return BaseSaasConfig.getApplication() + "_" + modulename;
    }
    private void updateCache(Block block) {
        String key = getKey(block.modulename);
        cacheBlocks.computeIfAbsent(key, v -> new ArrayList<>(1));
//        if (!cacheBlocks.containsKey(key)) {
//            cacheBlocks.put(key, new ArrayList<>());
//        }
        cacheBlocks.get(key).add(block);
        cacheBlocks.get(key).sort((o1, o2) -> {
            int diff = o1.sequence - o2.sequence;
            return Integer.compare(diff, 0);
        });
    }

    @Override
    public void clear() {
        cacheBlocks.clear();
    }

    @Override
    public void clear(String modulename) {
        String key = getKey(modulename);
        cacheBlocks.remove(key);
    }

    @Override
    public void clear(String modulename, String blockid) {
        String key = getKey(modulename);
        if (cacheBlocks.containsKey(key)) {
            cacheBlocks.get(key).removeIf(block -> block.blockid.toString().equals(blockid));
        }
    }

    @Override
    public Block get(String modulename, String blockid) {
        String key = getKey(modulename);
        if (!cacheBlocks.containsKey(key)) {
            this.list(key);
        }
        if (cacheBlocks.containsKey(key)) {
            List<Block> blocks = cacheBlocks.get(key);
            for (Block block : blocks) {
                if (blockid.equals(block.blockid.toString())) {
                    return block;
                }
            }
        }
        return null;
    }

    @Override
    public Boolean isExist(Block block) {
        String key = getKey(block.modulename);
        if (!cacheBlocks.containsKey(key)) {
            this.list(key);
        }
        if (cacheBlocks.containsKey(key)) {
            List<Block> blocks = cacheBlocks.get(key);
            for (Block item : blocks) {
                if (!item.id.equals(block.id) && block.blockid.equals(item.blockid)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Block load(String blockid) {
        if (!Utils.isEmpty(blockid)) {
            try {
                for (Map.Entry<String, List<Block>> entry : cacheBlocks.entrySet()) {
                    List<Block> blocks = entry.getValue();
                    for (Block block : blocks) {
                        if (blockid.equals(block.id)) {
                            return block;
                        }
                    }
                }
                Content conn = XN_Content.load(blockid, "blocks");
                Block block = new Block(conn);
                updateCache(block);
                return block;
            } catch (Exception e) {
                log.error("load Error: {}",e.getMessage());
            }
        }
        return null;
    }

    @Override
    public List<Block> list(String modulename) {
        try {
            String key = getKey(modulename);
            if (!cacheBlocks.containsKey(key) || cacheBlocks.get(key).isEmpty()) {
                List<Block> blocks = new ArrayList<>();
                List<Object> query = XN_Query.contentQuery().tag("blocks")
                        .filter("type", "eic", "blocks")
                        .filter("my.modulename", "eic", modulename)
                        .notDelete().order("my.sequence", "A_N")
                        .end(-1).execute();
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        blocks.add(new Block(item));
                    }
                    cacheBlocks.put(key, blocks);
                }
            }
            if (cacheBlocks.containsKey(key)) {
                return cacheBlocks.get(key);
            }
        } catch (Exception e) {
            log.error("list Error: {}",e.getMessage());
        }
        return new ArrayList<>(1);
    }

    @Override
    public void update(Block block) throws Exception {
        if (Utils.isEmpty(block.modulename)) {
            throw new Exception("模块名不能为空！");
        }
        if (Utils.isEmpty(block.id) && Boolean.TRUE.equals(isExist(block))) {
            throw new Exception("区块索引已经存在");
        }
        this.clear(block.modulename, block.blockid.toString());
        if (!Utils.isEmpty(block.id)) {
            Content conn = XN_Content.load(block.id, "blocks");
            if (block.deleted == 0) {
                block.toContent(conn);
                conn.save("blocks");
            } else {
                conn.delete("blocks");
            }
        } else {
            Content conn = XN_Content.create("blocks", "");
            block.toContent(conn);
            conn.save("blocks");
            block.fromContent(conn);
        }
        if (block.deleted == 0) {
            updateCache(block);
        }
    }

    @Override
    public void delete(String blockid) throws Exception {
        if (Utils.isEmpty(blockid)) {
            throw new Exception("区块索引不能为空");
        }
        long deleted = DateTimeUtils.gettimeStamp();
        Block block = load(blockid);
        block.deleted = deleted;
        update(block);
    }
}
