/**
 * Created by ZhangTao on 2017/7/11.
 */
public interface Layer {
    void Calculation();
    void BackCalculation();
    Layer getNextLayer();
    Layer getPreviousLayer();
}
