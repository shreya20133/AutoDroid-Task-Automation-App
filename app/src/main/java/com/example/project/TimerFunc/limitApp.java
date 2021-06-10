package com.example.project.TimerFunc;

import java.io.Serializable;

public class limitApp implements Serializable {
    String ApkName;
    String PackageName;
    String limit;
    public limitApp(String ApkName,String PackageName,String limit){
        this.ApkName = ApkName;
        this.PackageName = PackageName;
        this.limit = limit;
    }

    public String getApkName() {
        return ApkName;
    }

    public void setApkName(String apkName) {
        ApkName = apkName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
