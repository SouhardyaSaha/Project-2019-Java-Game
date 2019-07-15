package com.project.game.Tools;

public class PVector {
    float x, y;

    PVector(float x_, float y_){
        x = x_;
        y = y_;
    }

    void add(PVector V){
        y += V.y;
        x += V.x;
    }

    void normalize(){
        float m = mag();
        if(m != 0)
            div(m);
    }

    private float mag(){
        return (float)Math.sqrt(x*x + y*y);
    }

    private void div(float m){
        x /= m;
        y /= m;
    }

    void mult(float m){
        x *= m;
        y *= m;
    }

    static PVector sub(PVector u, PVector v){
        PVector dir;
        dir = new PVector(u.x-v.x, u.y-v.y);
        return dir;
    }

    void limit(float topspeed){
        if(mag() > topspeed){
            normalize();
            mult(topspeed);
        }

    }
}
