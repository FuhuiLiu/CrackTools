package com.JSONTest;

/**
 * Created by AqCxBoM on 2017/4/10.
 */

public class Pet {

    public int petid;//编号

    public String petname;//名称

    public String pettype;//类型

    public Pet(){}
    public Pet(int n, String name, String type)
    {
        petid = n;
        petname = name;
        pettype = type;
    }

    int getPetid(){ return petid; }
    String getPetname(){ return petname; }
    String getPettype(){ return pettype; }
    void setPetid(int n){ petid = n; }
    void setPetname(String name){ petname = name; }
    void setPettype(String type){ pettype = type; }
}