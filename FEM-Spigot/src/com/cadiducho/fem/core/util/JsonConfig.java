package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import java.io.BufferedWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JsonConfig implements Cloneable {

    private final File file;
    private final Map<String, Object> map;
    private static final FEMCore p = FEMCore.getInstance();

    public JsonConfig(File file2) {
        file = file2;
        List<String> list = getLines(file2);
        String list2 = joinList(list);
        Object ob = JSONValue.parse(list2);
        if (ob instanceof JSONObject) {
            map = (JSONObject) ob;
        } else {
            if (ob != null) {
                p.log("Atención: La config tiene un json inválido. (List)");
                p.log(file2.getName() + " - " + ob);
            }
            map = new HashMap<>();
        }
    }

    public void set(String s, Object o) {
        if (o == null) {
            ArrayList<String> remv = new ArrayList<>();
            remv.add(s);
            for (String key : map.keySet()) {
                if (key.startsWith(s)) {
                    remv.add(key);
                }
            }
            for (String st : remv) {
                map.remove(st);
            }
        } else {
            map.put(s, o);
        }
    }

    public Object get(String s) {
        return map.get(s);
    }

    public boolean contains(String s) {
        for (String st : map.keySet()) {
            if (st.equals(s) || st.startsWith(s + ".")) {
                return true;
            }
        }
        return false;
    }

    public String getString(String s) {
        return getString(s, "");
    }

    public String getString(String s, String def) {
        if (!contains(s)) {
            return def;
        }
        return (String) get(s);
    }

    public Boolean getBoolean(String s) {
        return getBoolean(s, false);
    }

    public Boolean getBoolean(String s, Boolean def) {
        if (!contains(s)) {
            return def;
        }
        return (Boolean) get(s);
    }

    public Long getLong(String s) {
        return getLong(s, 0L);
    }

    public Long getLong(String s, Long def) {
        if (!contains(s)) {
            return def;
        }
        return (Long) get(s);
    }

    public Double getDouble(String s) {
        return getDouble(s, 0.0D);
    }

    public Double getDouble(String s, Double def) {
        if (!contains(s)) {
            return def;
        }
        return (Double) get(s);
    }

    public Integer getInteger(String s) {
        return getInteger(s, 0);
    }

    public Integer getInteger(String s, Integer def) {
        if (!contains(s)) {
            return def;
        }
        return (Integer) get(s);
    }

    public Short getShort(String s) {
        return getShort(s, Short.parseShort("0"));
    }

    public Short getShort(String s, Short def) {
        if (!contains(s)) {
            return def;
        }
        return (Short) get(s);
    }

    public JSONArray getList(String s) {
        if (!contains(s)) {
            return new JSONArray();
        }
        return (JSONArray) get(s);
    }

    public List<String> getStringList(String s) {
        if (!contains(s)) {
            return new ArrayList<>();
        }
        return (List<String>) get(s);
    }

    public void save() {
        save(file);
    }

    public void save(File fi) {
        try {
            writeLargerTextFile(fi, Arrays.asList(JSONValue.toJSONString(map).split("\n", -1)));
        } catch (Exception ex) {
            p.log("Fallo al escribir un archivo de config.");
            p.debugLog("Error: " + ex.getMessage());
        }
    }

    public List<String> listKeys(String s, Boolean deep) {
        String s2 = s.endsWith(".") ? s : s + ".";
        if (deep) {
            ArrayList<String> rtrn = new ArrayList<>();
            for (String k : map.keySet()) {
                if (k.startsWith(s2)) {
                    rtrn.add(k.replaceFirst(s2, ""));
                }
            }
            return rtrn;
        } else {
            List<String> rtrn = new ArrayList<>();
            for (String k : map.keySet()) {
                if (k.startsWith(s2)) {
                    String a = (k.replaceFirst(s2, "").contains(".") ? k.replaceFirst(s2, "").split("\\.")[0] : k.replaceFirst(s2, ""));
                    rtrn.add(a);
                }
            }
            return rtrn;
        }
    }

    public List<String> listKeys(Boolean deep) {
        if (deep) {
            return new ArrayList<>(map.keySet());
        } else {
            List<String> rtrn = new ArrayList<>();
            for (String k : map.keySet()) {
                String s = k.contains(".") ? k.split("\\.")[0] : k;
                if (!rtrn.contains(s)) {
                    rtrn.add(s);
                }
            }
            return rtrn;
        }
    }
    
    //Internal
    
    private static ArrayList<String> getLines(File file) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            if (!file.exists()) {
                return null;
            }
            Path path = Paths.get(file.getAbsolutePath());
            Charset ENCODING = StandardCharsets.UTF_8;
            try (Scanner scanner = new Scanner(path, ENCODING.name())) {
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
                }
            }
            return lines;
        } catch (Exception ex) {
            p.log("Fallo al obtener las lineas del archivo " + file.getName());
            p.debugLog("Error: " + ex.getMessage());
            
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    private static String joinList(String seperator, Object[] list) {
        StringBuilder buf = new StringBuilder();
        for (Object each : list) {
            if (buf.length() > 0) {
                buf.append(seperator);
            }

            if ((each instanceof Collection)) {
                buf.append(joinList(seperator, ((Collection) each).toArray()));
            } else {
                try {
                    buf.append(each.toString());
                } catch (Exception e) {
                }
            }
        }
        return buf.toString();
    }

    private static String joinList(@SuppressWarnings("rawtypes") Collection c) {
        return joinList(", ", c.toArray());
    }
    
    private static void writeLargerTextFile(File file, List<String> aLines) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
            for (String line : aLines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}