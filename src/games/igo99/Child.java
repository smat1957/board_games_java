package igo99;

public class Child {

    private int z;       // 手の場所
    private int games;   // この手を探索した回数
    private double rate; // この手の勝率
    private int next;    // この手を打ったあとのノード

    public Child(){        
    }
    
    void setNext(int next){
        this.next=next;
    }
    int getNext(){
        return next;
    }
    
    void setRate(double rate){
        this.rate=rate;
    }
    double getRate(){
        return rate;
    }
    
    void setZ(int z){
        this.z=z;
    }
    int getZ(){
        return z;
    }
    
    void setGames(int games){
        this.games=games;
    }
    int getGames(){
        return games;
    }
    
    public Child myClone() {
        return this;
    }        
}
