package com.qnkj.common.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.services.IProgramServices;
import com.qnkj.common.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

@Service
public class ProgramServicesImpl implements IProgramServices {


    private static Map<String,List<Program>> cacheProgramss = new HashMap<>();


    @Override
    public void clear() {
        cacheProgramss.clear();
    }

    @Override
    public void clear(String group) {
        String application = BaseSaasConfig.getApplication();
        if (cacheProgramss.containsKey(application)) {
            cacheProgramss.get(application).removeIf(program -> program.group.equals(group));
        }
    }

    @Override
    public Program get(String group) {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cacheProgramss.containsKey(application)) {
                this.list();
            }
            for (Map.Entry<String, List<Program>> entry : cacheProgramss.entrySet()) {
                for(Program program: entry.getValue()){
                    if(program.group.equals(group)){
                        return program;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public Program load(String programid) {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cacheProgramss.containsKey(application)) {
                this.list();
            }
            for (Map.Entry<String, List<Program>> entry : cacheProgramss.entrySet()) {
                for(Program program: entry.getValue()){
                    if(program.id.equals(programid)){
                        return program;
                    }
                }
            }
            List<Program> programs = cacheProgramss.get(application);
            Content conn = XN_Content.load(programid, "programs");
            Program program = new Program(conn);
            programs.add(program);
            programs.sort((o1, o2) -> {
                int diff = o1.order - o2.order;
                return Integer.compare(diff, 0);
            });
            return program;
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public List<Program> list() {
        try {
            String application = BaseSaasConfig.getApplication();
            if (!cacheProgramss.containsKey(application)) {
                List<Object> query = XN_Query.contentQuery().tag("programs")
                        .filter("type", "eic", "programs")
                        .order("my.order", "A_N")
                        .notDelete().end(-1).execute();
                if (!query.isEmpty()) {
                    List<Program> programs = new ArrayList<>();
                    for (Object item : query) {
                        programs.add(new Program(item));
                    }
                    cacheProgramss.put(application,programs);
                }
            }
            if (cacheProgramss.containsKey(application)) {
                return cacheProgramss.get(application);
            }
        }catch (Exception ignored){}
        return null;
    }

    @Override
    public void update(Program program) throws Exception {
        if (Utils.isEmpty(program.group)) {
            throw new Exception("分组名不能为空！");
        }
        if (Utils.isEmpty(program.id) && !Utils.isEmpty(this.get(program.group))) {
            throw new Exception("分组名已经存在！");
        }
        this.clear(program.group);
        if(Utils.isEmpty(program.authorize)) {
            program.authorize("general");
        }
        if (Utils.isEmpty(program.id)) {
            Content conn = XN_Content.create("programs", "");
            program.toContent(conn);
            conn.save("programs");
            program.fromContent(conn);
        } else {
            Content conn = XN_Content.load(program.id, "programs");
            if(program.deleted == 0) {
                program.toContent(conn);
                conn.save("programs");
            } else {
                conn.delete("programs");
            }
        }
        if(program.deleted == 0) {
            String application = BaseSaasConfig.getApplication();
            List<Program> programs = new ArrayList<>();
            if (cacheProgramss.containsKey(application)) {
                programs = cacheProgramss.get(application);
            }
            programs.add(program);
            programs.sort((o1, o2) -> {
                int diff = o1.order - o2.order;
                return Integer.compare(diff, 0);
            });
            cacheProgramss.put(application, programs);
        }
    }
}
