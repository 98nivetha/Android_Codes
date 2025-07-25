package com.zebra.demo.utility;
import java.util.ArrayList;
import java.util.List;

public enum EmployeeStatus {

    NULL, TRUE,
    FALSE;

    public static String[] employeeStatusList = {
            "NULL", "TRUE",
            "FALSE"
    };


    public static EmployeeStatus getEmployeeStatus(int value) {
        for (EmployeeStatus type : EmployeeStatus.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getEmployeeStatusNameList(){
        List<String> temp = new ArrayList<>();
        for (EmployeeStatus value : EmployeeStatus.values()) {
            temp.add(value.name());
        }
        return temp;
    }

    public  int getEmployeeStatus(){
        if(this == EmployeeStatus.NULL) {
            return 0;
        } else if(this == EmployeeStatus.TRUE) {
            return  1;
        } else if(this == EmployeeStatus.FALSE) {
            return 2;
        }
        return 0;
    }
}
