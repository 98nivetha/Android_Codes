package com.zebra.demo.utility;
import java.util.ArrayList;
import java.util.List;

public enum AssetAuditType {

    IN_PROGRESS, COMPLETE,
    UPCOMING;

    public static String[] assetAuditTypeList = {
            "In Progress", "Complete",
            "Upcoming"
    };


    public static AssetAuditType getAssetAuditType(int value) {
        for (AssetAuditType type : AssetAuditType.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getAssetAuditType(){
        List<String> temp = new ArrayList<>();
        for (AssetAuditType value : AssetAuditType.values()) {
            temp.add(value.name());
        }
        return temp;
    }

    public  String getAssetAuditTypeName(){
        if(this == AssetAuditType.UPCOMING) {
            return "Upcoming";
        } else if(this == AssetAuditType.IN_PROGRESS) {
            return  "In Progress";
        } else if(this == AssetAuditType.COMPLETE) {
            return "Complete";
        }
        return "";
    }
}
