package igo99;
import static igo99.Constants.*;

public class Node {

    int child_num;           // 子局面の数
    Child[] child = new Child[CHILD_MAX];
    int child_games_sum;     // 子局面の回数の合計    
    
    public Node(){
        for(int i=0; i<CHILD_MAX; i++){
            child[i] = new Child();
        }
    }
    
    public Node myClone() {
        return this;
    }    
}
