package com.example.airlinesautomaticsystem;

import java.sql.Date;


public class employeeData {

    private final Integer employeeId;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String phoneNum;
    private final String position;
    private final String image;
    private final Date date;

    public employeeData(Integer employeeId, String firstName, String lastName, String gender, String phoneNum, String position, String image, Date date){
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.position = position;
        this.image = image;
        this.date = date;
    }

    public Integer getEmployeeId(){
        return employeeId;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getGender(){
        return gender;
    }
    public String getPhoneNum(){
        return phoneNum;
    }
    public String getPosition(){
        return position;
    }
    public String getImage(){
        return image;
    }
    public Date getDate(){
        return date;
    }

}
