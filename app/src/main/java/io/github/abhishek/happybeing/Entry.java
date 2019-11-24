package io.github.abhishek.happybeing;

public class Entry {

    // internal variable of the entry
    private int ID;
    private String TEXT;
    private String DATE_TIME;
    private byte[] PHOTO;
    private int FEALING;
    private String LOCATION;
    private int SHAKESCORE;

    //constructor
    public Entry(){}

    //trivial functions
    public int getId(){

        return this.ID;
    }

    public String getText(){

        return this.TEXT;
    }

    public String getDate_Time(){

        return this.DATE_TIME;
    }

    public byte[] getPhoto(){

        return this.PHOTO;
    }

    public int getFealing(){

        return this.FEALING;
    }

    public int getShakescore(){

        return this.SHAKESCORE;
    }

    public String getLocation(){

        return this.LOCATION;
    }


    public void setId(int id){

        this.ID=id;
    }

    public void setText(String text){

        this.TEXT=text;
    }

    public void setDate_Time(String datetime){

        this.DATE_TIME=datetime;
    }

    public void setPhoto(byte[] photo){

        this.PHOTO=photo;
    }

    public void setFealing(int fealing){

        this.FEALING=fealing;
    }

    public void setShakescore(int score){

        this.SHAKESCORE=score;
    }

    public void setLocation(String loc){

        this.LOCATION=loc;
    }
}


