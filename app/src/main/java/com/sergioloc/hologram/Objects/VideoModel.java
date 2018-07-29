package com.sergioloc.hologram.Objects;



public class VideoModel{
    private int id;
    private String name;
    private String code;
    private String image;
    private String tag;
    private boolean fav;

    public VideoModel(int id, String name, String code, String image, String tag, boolean fav){
        this.id=id;
        this.name=name;
        this.code=code;
        this.image=image;
        this.tag=tag;
        this.fav=fav;
    }
    public VideoModel(){
        this.id=0;
        this.name="";
        this.code="";
        this.image="";
    }
    public int getId(){ return id; }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public String getTag() {
        return tag;
    }
    public boolean getFav(){ return fav; }
    public void setFav(boolean fav){
        this.fav=fav;
    }


}
