/**
 * Created by ZhangTao on 2017/8/1.
 */
public class LearnRatio {
    private static float step = 0.0001f;
    private static float L;
    public static void  setLearnRatio(float e, float s){
        double r = Math.pow(Math.E, e *  -1);
        L =  (float) (step / (s * r + 1));
    }
    public static float getL(){
        return L > 0.000007f ? L : 0.000007f;
    }
}
