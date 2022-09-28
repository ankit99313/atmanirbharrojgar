package com.atmanirbharrogar.company.work;


public class Person {
    int Image;
    String Name;
    String Rating;
    String id;
    String url;
    String City;
    String Mobile;
    String AlternateMobile;
    String Profession;
    String State;
    String Gender;
    String urlToImage;
    String Address;

    public Person()
    {

    }

    public Person(String Name, String Rating, String id, String url, String City, String Mobile, String Profession, String State,String Gender,String AlternateMobile,String urlToImage,String Address) {

        this.Name=Name;
        this.Rating=Rating;
        this.id=id;
        this.url=url;
        this.City=City;
        this.Mobile=Mobile;
        this.AlternateMobile=AlternateMobile;
        this.Profession=Profession;
        this.State=State;
        this.Gender=Gender;
        this.urlToImage=urlToImage;
        this.Address=Address;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int Image) {
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile()
    {
        return Mobile;

    }
    public void setMobile(String Mobile) {
        this.Mobile =Mobile ;
    }

    public String getCity()
    {
        return City;

    }
    public void setCity(String City) {
        this.City =City;
    }

    public String getProfession()
    {
        return Profession;

    }
    public void setProfession(String Profession) {
        this.Profession =Profession ;
    }


    public String getGender()
    {
        return Gender;

    }
    public void setGender(String Gender) {
        this.Gender =Gender ;
    }

    public String getAlternateMobile()
    {
        return AlternateMobile;

    }
    public void setAlternateMobile(String AlternateMobile) {
        this.AlternateMobile =AlternateMobile ;
    }

    public String geturlToImage()
    {
        return  urlToImage;

    }
    public void  seturlToImage(String  urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getState()
    {
        return  State;

    }
    public void  setState(String  State) {
        this.State = State;
    }

    public String getAddress()
    {
        return  Address;

    }
    public void  setAddress(String  Address) {
        this.Address = Address;
    }




}
