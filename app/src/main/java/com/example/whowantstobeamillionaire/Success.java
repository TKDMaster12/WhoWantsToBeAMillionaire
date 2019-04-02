package com.example.whowantstobeamillionaire;

public class Success {
    private long id;
    private String money;
    private int time;
    private int point;

    //constructor
    public Success(long id, String money, int time, int point) {
        this.id = id;
        this.money = money;
        this.time = time;
        this.point = point;
    }

    //constructor
    public Success(String money, int time) {
        this.money = money;
        this.time = time;
        calculatePoints(money, time);
    }

    //calculate points
    //get amount of money earned divided by the amount of time
    private void calculatePoints(String money, int time){
        int points =0;
        switch (money) {
            case "0":
                points =0;
                break;
            case "100":
                points =10000;
                break;
            case "200":
                points =20000;
                break;
            case "300":
                points =30000;
                break;
            case "500":
                points =40000;
                break;
            case "1,000":
                points =50000;
                break;
            case "2,000":
                points =60000;
                break;
            case "4,000":
                points =70000;
                break;
            case "8,000":
                points =80000;
                break;
            case "16,000":
                points =90000;
                break;
            case "15,000":
                points =100000;
                break;
            case "32,000":
                points =110000;
                break;
            case "64,000":
                points =120000;
                break;
            case "125,000":
                points =130000;
                break;
            case "250,000":
                points =140000;
                break;
            case "500,000":
                points =150000;
                break;
            case "1,000,000":
                points =200000;
                break;
        }

        point = points/time;
    }

    //gets and sets for class variables
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
