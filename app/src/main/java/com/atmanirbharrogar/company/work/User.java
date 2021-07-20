package com.atmanirbharrogar.company.work;

public class User {
    private String Email;
    private String Id;
    private String Contact_Number;
    private String Aadhar_Number;
    private String Street_No;
    private String Pincode;
    private String State;
    private String City;
    private String Gender;
    private String Type;
    private String Profession;
    private String Name;
    private String Alternate_Contact_Number;
    private String Experience;
    String OrganisitionName;
    String OrganisitonAdress;
    String WorkMode;
    String Skill;
    String Charge;
    String Birth;


    public User(String email, String id, String street_No, String pincode, String state, String city, String gender, String name, String Birth) {
        Email = email;
        Id = id;
        Street_No = street_No;
        Pincode = pincode;
        State = state;
        City = city;
        Gender = gender;
        Name = name;
        Birth = Birth;
    }

    public User(String Profession,String Experience,String OrganisitionName,String OrganisitonAdress,String WorkMode,String Skill,String Charge)
    {
        this.Profession=Profession;
        this.Experience=Experience;
        this.OrganisitionName=OrganisitionName;
        this.OrganisitonAdress=OrganisitonAdress;
        this.WorkMode=WorkMode;
        this.Skill=Skill;
        this.Charge=Charge;
    }



    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    public String getBirth() {
        return Birth;
    }

    public void setBirth(String birth) {
        Profession = birth;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getOrganisitionName() {
        return OrganisitionName;
    }

    public void setOrganisitionName(String organisitionName) {
        OrganisitionName = organisitionName;
    }

    public String getOrganisitonAdress() {
        return OrganisitonAdress;
    }

    public void setOrganisitonAdress(String organisitonAdress) {
        OrganisitonAdress = organisitonAdress;
    }

    public String getWorkMode() {
        return WorkMode;
    }

    public void setWorkMode(String workMode) {
        WorkMode = workMode;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    public String getCharge() {
        return Charge;
    }

    public void setCharge(String charge) {
        Charge = charge;
    }

    public User(String contact_Number){
    Contact_Number=contact_Number;
}

    public String getEmail() {
        return Email;
    }

    public String getId() {
        return Id;
    }

    public String getStreet_No() {
        return Street_No;
    }

    public String getContact_Number() {
        return Contact_Number;
    }

    public String getPincode() {
        return Pincode;
    }

    public String getState() {
        return State;
    }

    public String getCity() {
        return City;
    }

    public String getGender() {
        return Gender;
    }

    public String getName() {
        return Name;
    }


    public void setContact_Number(String contact_Number) {
        Contact_Number = contact_Number;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setStreet_No(String street_No) {
        Street_No = street_No;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public void setState(String state) {
        State = state;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setName(String name) {
        Name = name;
    }

}
