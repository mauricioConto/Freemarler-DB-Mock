package com.condor.audit.model.utilities;

public class PayloadFieldCheck {
    private final Object object;
    private final boolean isNull;

    public PayloadFieldCheck(Object object) {
        this.object = object;
        isNull = JsonBase.TAG_NOT_PRESENT.equals(object) || JsonBase.TAG_PRESENT_BUT_NULL.equals(object);
    }

    public Integer integerValue() {
        return Integer.valueOf(String.valueOf(object));
    }

    public boolean isNull() {
        return isNull;
    }

    public boolean isNullOrBlank() {
        return isNull || stringValue().isEmpty();
    }

    public boolean isNotNull() {
        return !isNull;
    }

    public boolean isObjectFalse() {
        return !((Boolean) object);
    }

    public boolean isObjectTrue() {
        return (Boolean) object;
    }

    public Object get() {
        return object;
    }


    public String stringValue() {
        if (isNull) {
            return null;
        } else {
            return String.valueOf(object).trim();
        }
    }


}
