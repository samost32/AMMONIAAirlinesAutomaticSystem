package com.example.airlinesautomaticsystem;

public class EmployeeInfo {
    private String date;
    private String loginTime;
    private String logoutTime;

    public EmployeeInfo(String date, String loginTime, String logoutTime) {
        this.date = date;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public String getDate() {
        return date;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }
}