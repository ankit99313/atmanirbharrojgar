package com.atmanirbharrogar.company.work;

public class ShowPost {
    String Category;
    String WorkerTime;
    String JudgeWork;
    String Title;
    String LongDescription;
    String Address;
    String Mobile;
    String Pin;


    public ShowPost() {

    }


   /* public ShowPost(String profession, String needWorkerIn, String judgeWork, String title, String longDescription) {
        this.Profession = profession;
        this.NeedWorkerIn = needWorkerIn;
        this.JudgeWork = judgeWork;
        this.Title = title;
        this.LongDescription = longDescription;
    }*/

    public String getAddress() {
        return Address;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPin() {
        return Pin;
    }

    public String getCategory() {
        return Category;
    }

    public String getWorkerTime() {
        return WorkerTime;
    }

    public String getJudgeWork() {
        return JudgeWork;
    }

    public String getTitle() {
        return Title;
    }

    public String getLongDescription() {
        return LongDescription;
    }


    public void setCategory(String category) {
        Category = category;
    }

    public void setWorkerTime(String workerTime) {
        WorkerTime = workerTime;
    }

    public void setJudgeWork(String judgeWork) {
        JudgeWork = judgeWork;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setPin(String pin) {
        Pin = pin;
    }
}



