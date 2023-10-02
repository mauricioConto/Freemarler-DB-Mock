package com.condor.audit.model.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMessage {
    public String requestId;
    public String entity;
    public Map<String, String> externalIds;
    public List<String> errorMsg;

    public ErrorMessage() {
        externalIds = new HashMap<>();
        errorMsg = new ArrayList<>();
    }

    public ErrorMessage(JsonBase jb) {
        this();
        errorMsg.addAll(jb.errors);
        requestId = jb.fieldCheck("requestId").stringValue();
        entity = jb.getType();
        for (String externalIdName : BaseConstants.externalIds.getOrDefault(entity, List.of())) {
            externalIds.put(externalIdName, jb.fieldCheck(externalIdName).stringValue());
        }
    }
}
