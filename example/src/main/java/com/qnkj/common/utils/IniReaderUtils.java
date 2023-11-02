package com.qnkj.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * create by 徐雁
 * create date 2021/3/10
 * @author clubs
 */

public class IniReaderUtils {
    protected HashMap<Object,Object> sections = new HashMap<>(1);
    protected String iniFilename;
    private Properties current;

    /**
     * 构造函数
     */
    public IniReaderUtils(String filename) throws Exception {
        iniFilename = filename;
        if (Boolean.TRUE.equals(ContextUtils.isJar())){
            URL resUrl = IniReaderUtils.class.getClassLoader().getResource(filename);
            if(resUrl != null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(resUrl.openStream()));
                read(reader);
                reader.close();
            }
        }else {
            String filePath = ContextUtils.getLocalFilepath("resources" + File.separator + filename,true);
            if (FileUtil.exist(filePath)) {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                read(reader);
                reader.close();
            }
        }
    }

    /**
     * 读取文件
     */
    protected void read(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    /**
     * 解析配置文件行
     */
    @SuppressWarnings("unchecked")
    protected void parseLine(String line) {
        line = line.trim();
        if (line.matches("^\\[.*]$")) {
            //取出正则表达式匹配到的内容中的第一个条件组中的内容
            String currentSecion = line.replaceFirst("^\\[(.*)]$", "$1");//[dev]
            //创建一个Properties对象
            current = new Properties();
            //将currentSecion和current以键值对的形式存放在map集合中。
            sections.put(currentSecion, current);
        } else if (line.matches("^(.*)=(.*)$") && current != null) {
            int i = line.indexOf('=');
            String name = line.substring(0, i).trim();
            String value = line.substring(i + 1).trim();
            current.setProperty(name, value);
        }
    }
    /**
     * 根据提供的键获取值
     */
    public String getValue(String section, String key) {
        Properties p = (Properties) sections.get(section);
        if (p == null) {
            return null;
        }
        return p.getProperty(key);
    }

    /**
     * 保存健值
     * @param section 分组名称
     * @param key 键名
     * @param value 值
     */
    public void setValue(String section, String key, String value){
        Properties p = (Properties) sections.get(section);
        if (p == null) {
            p = new Properties();
        }
        p.setProperty(key,value);
        sections.put(section,p);
    }
    /**
     * 获取所有分组名称
     */
    public List<String> getSections() {
        List<String> tmpSections = new ArrayList<>();
        if(!sections.isEmpty()){
            for (Map.Entry<?,?> entry : sections.entrySet()) {
                tmpSections.add(entry.getKey().toString());
            }
        }
        return tmpSections;
    }

    /**
     * 获取指定的分组数据
     */
    public Properties getSection(String section) {
        if(sections.containsKey(section)){
            Properties properties = new Properties();
            properties.putAll((Properties)sections.get(section));
            return properties;
        } else {
            return new Properties();
        }
    }

    /**
     * 将资源数据保存
     */
    public void save() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : sections.entrySet()) {
            sb.append("[").append(entry.getKey()).append("]\n");
            Properties props = (Properties) entry.getValue();
            List<String> mapKeys = new ArrayList<>();
            for(Object prop : props.keySet()){
                mapKeys.add(prop.toString());
            }
            Collections.sort(mapKeys);
            for (String propkey : mapKeys) {
                sb.append(propkey).append(" = ");
                sb.append(props.getProperty(propkey)).append("\n");
            }
            sb.append("\n\n");
        }
        if (Boolean.FALSE.equals(ContextUtils.isJar())) {
            String filePath = ContextUtils.getLocalFilepath("resources" + File.separator + iniFilename,true);
            File file = new File(filePath);
            try {
                FileUtil.touch(file);
                FileUtil.writeUtf8String(sb.toString(), file);
            } catch (IORuntimeException e) {
                throw new Exception(e.getMessage());
            }
            //更新缓存
            filePath = ContextUtils.getLocalFilepath(iniFilename);
            try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)))) {
                bw.write(sb.toString());
                bw.flush();
            }
        }
    }

    public Boolean isExist(String section) {
        boolean isexist = false;
        if(!sections.isEmpty()){
            for (Map.Entry<?,?> entry : sections.entrySet()) {
                if(entry.getKey().equals(section)) {
                    isexist = true;
                    break;
                }
            }
        }
        return isexist;
    }

    public void delete(String section) {
        sections.remove(section);
    }
/**
    protected void updateJar(String data) throws IOException {
        String entryPath = "BOOT-INF/classes/"+iniFilename;
        String jarFilePath = System.getProperty("java.class.path");
        int firstIndex = jarFilePath.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = jarFilePath.lastIndexOf(File.separator) + 1;
        String jarPath = jarFilePath.substring(firstIndex, lastIndex);
        File file = new File(jarFilePath);
        JarFile jarFile = new JarFile(file);
        List<JarEntry> lists = new LinkedList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        jarFile.close();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            lists.add(jarEntry);
        }

        File newjar = new File(jarPath+File.separator+"new.jar");
        FileOutputStream fos = new FileOutputStream(newjar);
        JarOutputStream jos = new JarOutputStream(fos);
        try{
            for (JarEntry je : lists) {
                if (je.getName().equals(entryPath)) {
                    // 将内容写入文件中
                    jos.putNextEntry(new JarEntry(entryPath));
                    jos.write(data.getBytes());
                } else {
                    //表示将该JarEntry写入jar文件中 也就是创建该文件夹和文件
                    jos.putNextEntry(new JarEntry(je));
                    jos.write(streamToByte(jarFile.getInputStream(je)));
                }
            }
            jos.close();
            fos.close();
//            file.delete();
//            newjar.renameTo(new File(jarFilePath));
        }catch (Exception e){
        }finally {
            jos.close();
            fos.close();
        }
    }

    protected byte[] streamToByte(InputStream inputStream) {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inputStream.close();
        } catch (IOException e) {
        }
        return outSteam.toByteArray();
    }
 */
}
