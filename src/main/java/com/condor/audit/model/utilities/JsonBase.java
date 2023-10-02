package com.condor.audit.model.utilities;


import com.condor.audit.model.utilities.json.DSLJsoner;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonBase implements Serializable {
    private static final DSLJsoner dslJsoner = new DSLJsoner();
    public String type;
    public Map<String, Object> event;
    public List<String> errors;

    private boolean ignored;

    public static final String
            TAG_NOT_PRESENT = "\u0001\u0003\u0001",
            TAG_PRESENT_BUT_NULL = "\u0001\u0002\u0001";

    public JsonBase() {
        event = new HashMap<>();
        ignored = false;
    }

    public Object get(String key) {
        return get(key, false);
    }

    public Object get(String key, boolean delete) {
        String[] path = key.split("\\.");
        Map<String, Object> node = event;
        Object ret = null;
        String lastKey = null;
        for (String pathElem : path) {
            lastKey = pathElem;
            if (node.containsKey(pathElem)) {
                ret = node.get(pathElem);
                if (ret instanceof LinkedHashMap) {
                    node = (Map<String, Object>) ret;
                }
            } else {
                return TAG_NOT_PRESENT;
            }
        }

        if (delete) {
            node.remove(lastKey);
        }

        if (ret == null) {
            return TAG_PRESENT_BUT_NULL;
        } else {
            return ret;
        }
    }

    public Object getId() {
        return get("requestId");
    }

    public boolean isIgnored() {
        return ignored;
    }

    public boolean isNull(Object object) {
        return JsonBase.TAG_NOT_PRESENT.equals(object) || JsonBase.TAG_PRESENT_BUT_NULL.equals(object);
    }

    public void put(String key, Object value) {
        String[] path = key.split("\\.");
        Map node = event;
        int last = path.length - 1;
        for (int i = 0; i < last; i++) {
            String pathElem = path[i];
            if (node.containsKey(pathElem)) {
                Object ret = node.get(pathElem);
                if (ret instanceof LinkedHashMap) {
                    node = (Map) ret;
                }
            } else {
                Map newNode = new HashMap<>();
                node.put(pathElem, newNode);
                node = newNode;
            }
        }

        node.put(path[last], value);
    }

    public void replaceKey(String ipKey, String opKey) {
        put(opKey, get(ipKey, true));
    }

    public Map<String, Object> getEvent() {
        return event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public boolean notEmpty() {
        return event != null && !event.isEmpty();
    }

    public void info(Logger logger) {
        logger.info("|{}|{}|{}|", getId(), getType(), hasError());
    }

    public void info(Logger logger, String msg) {
        logger.info("|{}|{}|{}|{}|", getId(), getType(), hasError(), msg);
    }

    public boolean hasError() {
        return errors != null && errors.size() != 0;
    }

    public void addError(Logger logger, String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }

        errors.add(error);
//        System.out.printf("|%s|%s|%s|%n", getId(), getType(), error);
        logger.error("|{}|{}|{}|", getId(), getType(), error);
    }

    @Override
    public String toString() {
        return dslJsoner.serialize(this);
    }

    public String getErrorLog() {
        return dslJsoner.serialize(new ErrorMessage(this));
    }

    public PayloadMetric getErrorMetric() {
        return getMetric(PayloadStatus.ERROR);
    }

    public PayloadMetric getMetric(PayloadStatus status) {
        return new PayloadMetric(status, this);
    }

    public PayloadFieldCheck fieldCheck(String key) {
        return new PayloadFieldCheck(get(key));
    }
}