package com.qnkj.common.configs;

import com.github.restapi.XN_Content;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.utils.ClassUtils;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author clubs
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class BaseRecordConfig {

    @ApiModelProperty("记录ID")
    public String id = "";

    @ApiModelProperty("创建者")
    public String author = "";

    @ApiModelProperty("创建时间")
    public String published = "";

    @ApiModelProperty("修改时间")
    public String updated = "";

    @ApiModelProperty("标题")
    public String title = "";

    @ApiModelProperty("应用")
    private String application = "";

    @ApiModelProperty("删除标记")
    public long deleted = 0;

    @ApiModelProperty("是否新建标记")
    public Integer createnew = 0;

    @ApiModelProperty("企业名称")
    public String supplierid = "0";

    @ApiModelProperty("是否编辑状态")
    protected Boolean isEditState = false;

    @ApiModelProperty("状态(0:未送审,1:等待审批,2:审批同意,3:审批不同意,4:已提交)")
    public Integer approvalstatus = 0;
    @ApiModelProperty("提交审批时间")
    public String submitdatetime = "";
    @ApiModelProperty("审批时间")
    public String approvaldatetime = "";
    @ApiModelProperty("审批人")
    public String finishapprover = "";

    @ApiModelProperty("流程标题")
    public String activiti_processtitle = "";
    @ApiModelProperty("所属流程")
    public String activiti_process = "";
    @ApiModelProperty("紧急程度(0：普通，1：重要，2：紧急)")
    public Integer activiti_urgencydegree = -1;
    @ApiModelProperty("发起时间")
    public String activiti_initiatetime = "";
    @ApiModelProperty("执行状态")
    public Integer activiti_executionstatus = -1;
    @ApiModelProperty("流程处理人员")
    public List<String> activiti_dealwiths = new ArrayList<>();
    @ApiModelProperty("流程流转次数")
    public Integer activiti_circulations = 0;

    public void setEditState(Boolean editState) {
        this.isEditState = editState;
    }

    public Boolean getEditState() {
        return this.isEditState;
    }

    @Override
    public String toString() {
        return Utils.objectToJson(this);
    }

    public Map<Object,Object> toMap() {
        return (Map<Object,Object>) Utils.classToData(this);
    }

    public void fromContent(Object content){
        if(content instanceof Content){
            for(Field field: this.getClass().getFields()){
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                Object fieldValue = ClassUtils.exeMethod(content,"get",fieldName);
                if(fieldValue == null){
                    continue;
                }
                if (fieldType.equals(String.class)) {
                    ClassUtils.setValue(this,fieldName,fieldValue);
                } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    if (Utils.isNotEmpty(fieldValue)) {
                        ClassUtils.setValue(this,fieldName,Integer.parseInt(fieldValue.toString(), 10));
                    } else {
                        ClassUtils.setValue(this,fieldName,0);
                    }
                } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    if (Utils.isNotEmpty(fieldValue)) {
                        ClassUtils.setValue(this, fieldName, Integer.parseInt(fieldValue.toString(), 10) != 0);
                    } else {
                        ClassUtils.setValue(this,fieldName,0);
                    }
                } else if (fieldType.equals(List.class)) {
                        if (fieldValue instanceof String) {
                            ClassUtils.setValue(this,fieldName,new ArrayList<>(ImmutableSet.of(fieldValue)));
                        } else {
                            for (int i = 0; i < ((List<?>) fieldValue).size(); i++) {
                                if (!Utils.isEmpty(((List<?>) fieldValue).get(i)) && "<empty>".equals(((List<String>) fieldValue).get(i))) {
                                    ((List<String>) fieldValue).set(i, "");
                                }
                            }
                            ClassUtils.setValue(this,fieldName,fieldValue);
                        }
                } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                    if (Utils.isNotEmpty(fieldValue)) {
                        ClassUtils.setValue(this, fieldName, Long.parseLong(fieldValue.toString()));
                    } else {
                        ClassUtils.setValue(this,fieldName,0L);
                    }
                } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    if (Utils.isNotEmpty(fieldValue)) {
                        ClassUtils.setValue(this, fieldName, Double.parseDouble(fieldValue.toString()));
                    } else {
                        ClassUtils.setValue(this,fieldName,0.0);
                    }
                } else {
                    ClassUtils.setValue(this,fieldName,fieldValue);
                }
            }
        }
    }

    public void toContent(Object content) {
        boolean isFlow = false;
        try {
            Object value = ClassUtils.getValue(this,"activiti_process");
            if(Utils.isNotEmpty(value)){
                isFlow = true;
            } else {
                value = ClassUtils.getValue(this,"approvalstatus");
                if(Utils.isNotEmpty(value) && Integer.parseInt(value.toString(),10) != -1){
                    isFlow = true;
                }
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldname = field.getName();
            Class<?> fieldtype = field.getType();
            if (field.getModifiers() == Modifier.PUBLIC) {
                try {
                    if (content instanceof Content) {
                        if (fieldtype.equals(Boolean.class) || fieldtype.equals(boolean.class)) {
                            ((Content) content).set(fieldname, Boolean.parseBoolean(field.get(this).toString()) ? 1 : 0);
                        } else if (fieldtype.equals(List.class)) {
                            if (Utils.isEmpty(field.get(this))) {
                                ((Content) content).set(fieldname, new ArrayList<>());
                            } else {
                                List<String> values = (List<String>)field.get(this);
                                for(int i=0;i<values.size();i++){
                                    if(Utils.isEmpty(values.get(i))){
                                        values.set(i,"<empty>");
                                    }
                                }
                                ((Content) content).set(fieldname, values);
                            }
                        } else {
                            if (Boolean.FALSE.equals(isRetainField(fieldname))){
                                if(Boolean.TRUE.equals(isSystemField(field,isFlow))){
                                    continue;
                                }
                                ((Content) content).set(fieldname, field.get(this));
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        Field[] allFields = this.getClass().getFields();
        for (Field field : allFields) {
            String fieldname = field.getName();
            Object fieldtype = field.getType();
            if (field.getModifiers() == Modifier.PUBLIC) {
                try {
                    if (Boolean.FALSE.equals(isDeclaredField(fieldname)) && Boolean.FALSE.equals(isRetainField(fieldname))) {
                        if(Boolean.TRUE.equals(isSystemField(field,isFlow))){
                            continue;
                        }
                        if (content instanceof Content) {
                            if (fieldtype.equals(Boolean.class) || fieldtype.equals(boolean.class)) {
                                ((Content) content).set(fieldname, Boolean.parseBoolean(field.get(this).toString()) ? 1 : 0);
                            } else if (fieldtype.equals(List.class)) {
                                if (Utils.isEmpty(field.get(this))) {
                                    ((Content) content).set(fieldname, "");
                                } else {
                                    ((Content) content).set(fieldname, field.get(this));
                                }
                            } else {
                                ((Content) content).set(fieldname, field.get(this));
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    private Boolean isSystemField(Field field, boolean isFlow) {
        boolean result = false;
        String fieldName = field.getName();
        Object fieldValue = ClassUtils.getValue(this,fieldName);
        try {
            if (("approvalstatus".equals(fieldName) && fieldValue != null && fieldValue.equals(-1)) ||
                    ("submitdatetime".equals(fieldName) && Utils.isEmpty(fieldValue)) ||
                    ("approvaldatetime".equals(fieldName) && Utils.isEmpty(fieldValue)) ||
                    ("finishapprover".equals(fieldName) && Utils.isEmpty(fieldValue))
            ) {
                result = true;
            }

            if (!isFlow && (("activiti_processtitle".equals(fieldName) && Utils.isEmpty(fieldValue)) ||
                    ("activiti_process".equals(fieldName) && Utils.isEmpty(fieldValue)) ||
                    ("activiti_urgencydegree".equals(fieldName) && fieldValue != null && fieldValue.equals(-1)) ||
                    ("activiti_initiatetime".equals(fieldName) && Utils.isEmpty(fieldValue)) ||
                    ("activiti_executionstatus".equals(fieldName) && fieldValue != null && fieldValue.equals(-1)) ||
                    ("activiti_dealwiths".equals(fieldName) && Utils.isEmpty(fieldValue)) ||
                    ("activiti_circulations".equals(fieldName) && fieldValue != null && fieldValue.equals(0))
            )) {
                result = true;
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
    private Boolean isDeclaredField(String fieldname) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fname = field.getName();
            if (fname.equals(fieldname)) {
                return true;
            }
        }
        return false;
    }
    private Boolean isRetainField(String fieldname) {
        return Arrays.asList("id","author","published","updated","isEditState","application").contains(fieldname);
    }

    public void fromRequest(Object httpRequest) {
        if(httpRequest instanceof Map) {
            fromRequest((Map<String, Object>) httpRequest);
        }else{
            log.error("fromRequest参数错误");
        }
    }
    public void fromRequest(Map<String, Object> httpRequest) {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            String fieldname = field.getName();
            Class<?> fieldtype = field.getType();
            if (field.getModifiers() == Modifier.PUBLIC && httpRequest.containsKey(fieldname) && !Arrays.asList("application", "isEditState", "createnew").contains(fieldname)) {
                try {
                    if (fieldtype.equals(String.class)) {
                        if(httpRequest.getOrDefault(fieldname, "") instanceof List){
                            ClassUtils.setValue(this,fieldname,String.join(",",(List<String>)httpRequest.getOrDefault(fieldname, "")));
                        } else if(httpRequest.get(fieldname) instanceof String){
                            ClassUtils.setValue(this,fieldname,httpRequest.getOrDefault(fieldname, ""));
                        } else if(Utils.isNotEmpty(httpRequest.get(fieldname))){
                            ClassUtils.setValue(this,fieldname,httpRequest.get(fieldname).toString());
                        }
                    } else if (fieldtype.equals(Boolean.class) || fieldtype.equals(boolean.class)) {
                        boolean fieldValue = !Utils.isEmpty(httpRequest.get(fieldname)) && ("1".equals(httpRequest.get(fieldname).toString()) || "true".equalsIgnoreCase(httpRequest.get(fieldname).toString()));
                        ClassUtils.setValue(this,fieldname,fieldValue);
                    } else if (fieldtype.equals(Integer.class) || fieldtype.equals(int.class)) {
                        if (Utils.isEmpty(httpRequest.get(fieldname))){
                            ClassUtils.setValue(this,fieldname,0);
                        } else {
                            ClassUtils.setValue(this,fieldname,Integer.parseInt(httpRequest.getOrDefault(fieldname, 0).toString(), 10));
                        }
                    } else if (fieldtype.equals(long.class)) {
                        if (Utils.isEmpty(httpRequest.get(fieldname))){
                            ClassUtils.setValue(this,fieldname,0);
                        }else {
                            ClassUtils.setValue(this,fieldname,Long.parseLong(httpRequest.getOrDefault(fieldname, 0).toString(), 10));
                        }
                    } else if (fieldtype.equals(Double.class)) {
                        if (Utils.isEmpty(httpRequest.get(fieldname))){
                            ClassUtils.setValue(this,fieldname,0.0);
                        }else {
                            ClassUtils.setValue(this,fieldname,Double.parseDouble(httpRequest.getOrDefault(fieldname, 0.0).toString()));
                        }
                    }else if (fieldtype.equals(List.class)) {
                        if (Utils.isEmpty(httpRequest.get(fieldname))) {
                            ClassUtils.setValue(this,fieldname,new ArrayList<>());
                        } else {
                            if (httpRequest.get(fieldname) instanceof String) {
                                ClassUtils.setValue(this,fieldname,Collections.singletonList(httpRequest.getOrDefault(fieldname, "")));
                            } else {
                                ClassUtils.setValue(this,fieldname,httpRequest.getOrDefault(fieldname, new ArrayList<>()));
                            }
                        }
                    } else {
                        ClassUtils.setValue(this,fieldname,httpRequest.getOrDefault(fieldname, ""));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public void fromJson(String json){
        Object jsonObj = Utils.jsonToObject(json);
        if(jsonObj instanceof Map){
            fromRequest(jsonObj);
        }
    }

    public void save(String type) {
        this.save(type,type,"",0);
    }
    public void save(String type,String tag) {
        this.save(type,tag,"",0);
    }
    public void save(String type,String tag,String author){
        this.save(type,tag,author,0);
    }
    public void save(String type,String tag,int datetype) {
        this.save(type,tag,"",datetype);
    }
    public void save(String type,String tag,String author,int datetype) {
        try {
            if (Utils.isEmpty(this.id)) {
                Content content = XN_Content.create(type, "", author, datetype);
                this.toContent(content);
                this.fromContent(content.save(tag));
            }else {
                Content content = XN_Content.load(this.id,tag,datetype);
                this.toContent(content);
                content.save(tag);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void setValues(BaseRecordConfig baseRecordConfig){
        id = baseRecordConfig.id;
        author = baseRecordConfig.author;
        published = baseRecordConfig.published;
        updated = baseRecordConfig.updated;
        title = baseRecordConfig.title;
        application = baseRecordConfig.application;
        deleted = baseRecordConfig.deleted;
        createnew = baseRecordConfig.createnew;
        isEditState = baseRecordConfig.isEditState;
        approvalstatus = baseRecordConfig.approvalstatus;
        submitdatetime = baseRecordConfig.submitdatetime;
        approvaldatetime = baseRecordConfig.approvaldatetime;
        finishapprover = baseRecordConfig.finishapprover;
        activiti_processtitle = baseRecordConfig.activiti_processtitle;
        activiti_process = baseRecordConfig.activiti_process;
        activiti_urgencydegree = baseRecordConfig.activiti_urgencydegree;
        activiti_initiatetime = baseRecordConfig.activiti_initiatetime;
        activiti_executionstatus = baseRecordConfig.activiti_executionstatus;
        activiti_dealwiths = baseRecordConfig.activiti_dealwiths;
        activiti_circulations = baseRecordConfig.activiti_circulations;
    }
}
