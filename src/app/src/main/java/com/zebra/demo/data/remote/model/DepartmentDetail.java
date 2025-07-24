package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DepartmentDetail {
     public String Deptcode;
     public String DeptDesc;
     public String rstatus;

     @NonNull
     @Override
     public String toString() {
          return DeptDesc == null ? "" : DeptDesc;
     }
}
