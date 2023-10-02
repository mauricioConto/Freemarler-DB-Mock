package com.condor.audit.model.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseConstants {

    public final static Map<String, List<String>> externalIds;

    static {
        externalIds = new HashMap<>();
        externalIds.put("person", List.of(
                externalIdName("programManager"),
                externalIdName("person")));
        externalIds.put("account", List.of(
                externalIdName("product"),
                externalIdName("account")));
        externalIds.put("customer", List.of(
                externalIdName("account"),
                externalIdName("person"),
                externalIdName("customer")));
        externalIds.put("card", List.of(
                externalIdName("customer"),
                "card_token",
                externalIdName("card")));
        externalIds.put("cardSequence", List.of(
                externalIdName("card"),
                externalIdName("cardSequence")));
    }


    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String externalIdName(String entityName) {
        return String.format("external%sId", capitalize(entityName));
    }

    public static String internalIdName(String entityName) {
        return String.format("%s_id", entityName.replaceAll("([A-Z])", "_$1").toLowerCase());
    }
}
