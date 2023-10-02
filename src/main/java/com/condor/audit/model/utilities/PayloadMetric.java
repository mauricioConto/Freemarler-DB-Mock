package com.condor.audit.model.utilities;



import com.condor.audit.model.utilities.json.DSLJsoner;

import java.io.Serializable;
import java.util.TreeSet;

public class PayloadMetric implements Serializable {
    private static final DSLJsoner dsl = new DSLJsoner();

    public String requestId;
    public PayloadStatus payloadStatus;
    public String requestDateTime;
    public String entity;
    public String externalId;

    private TreeSet<PayloadStatus> seen;

    public PayloadMetric() {
    }

    public PayloadMetric(PayloadStatus payloadStatus, JsonBase jsonBase) {
        this.payloadStatus = payloadStatus;
        this.requestId = jsonBase.fieldCheck("requestId").stringValue();
        this.requestDateTime = jsonBase.fieldCheck("requestDateTime").stringValue();
        this.entity = jsonBase.getType();
        this.externalId = jsonBase.fieldCheck(
                BaseConstants.externalIdName(jsonBase.getType())
        ).stringValue();
    }

    public String getRequestId() {
        return requestId;
    }

    public void addStatus(PayloadStatus payloadStatus) {
        if (seen == null) {
            seen = new TreeSet<>();
            seen.add(this.payloadStatus);
        }

        seen.add(payloadStatus);
        this.payloadStatus = seen.last();
    }

    public TreeSet<PayloadStatus> getSeen() {
        return seen;
    }

    @Override
    public String toString() {
        return dsl.serialize(this);
    }


}
