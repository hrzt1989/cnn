/**
 * Created by ZhangTao on 2017/7/10.
 */
public class NumberVector {
    static public float [][]numberVector = {
            {-10,-10,-10,10,10,10,10,10,10,10},
            {10,10,-10,10,-10,10,-10,10,10,-10},
            {10,10,-10,10,10,-10,10,10,-10,10},
            {10,-10,10,10,10,10,-10,-10,10,-10},
            {-10,10,10,-10,10,10,10,-10,10,10},
            {10,10,-10,10,-10,10,10,-10,10,10},
            {10,10,10,-10,-10,-10,10,10,10,10},
            {10,10,10,-10,10,-10,10,-10,10,10},
            {10,-10,10,10,-10,10,-10,10,10,-10},
            {-10,10,-10,10,10,-10,10,10,10,-10}
    };
    static public float [][]numberVectorOneHot = {
            {1,0,0,0,0,0,0,0,0,0},
            {0,1,0,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,0,0,0},
            {0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,0,0},
            {0,0,0,0,0,0,1,0,0,0},
            {0,0,0,0,0,0,0,1,0,0},
            {0,0,0,0,0,0,0,0,1,0},
            {0,0,0,0,0,0,0,0,0,1}
    };
    static public int rows = 10;
    static public int column = 10;
    static public int []number = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
}
