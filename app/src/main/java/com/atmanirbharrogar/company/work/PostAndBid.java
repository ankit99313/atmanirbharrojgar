package com.atmanirbharrogar.company.work;

public class PostAndBid {
    private String Category;
    private String Title;
    private String LongDescription;
    private String JudgeWork;
    private String WorkerTime;
    String Address;
    String Mobile;
    String Pin;

    public PostAndBid(String category,String WorkerTime,String JudgeWork, String title, String longDescription) {
        Category = category;
        this.WorkerTime=WorkerTime;
        this.JudgeWork=JudgeWork;
        Title = title;
        LongDescription = longDescription;
    }

    public PostAndBid(String category,String WorkerTime,String JudgeWork, String title, String longDescription,String Address,String Mobile,String Pin) {
        Category = category;
        this.WorkerTime=WorkerTime;
        this.JudgeWork=JudgeWork;
        Title = title;
        LongDescription = longDescription;
        this.Address=Address;
        this.Mobile=Mobile;
        this.Pin=Pin;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public PostAndBid() {
        super();
    }

    public String getJudgeWork() {
        return JudgeWork;
    }

    public void setJudgeWork(String judgeWork) {
        JudgeWork = judgeWork;
    }

    public String getWorkerTime() {
        return WorkerTime;
    }

    public void setWorkerTime(String workerTime) {
        WorkerTime = workerTime;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }
}
