/**
 * Created by ZhangTao on 2017/7/11.
 */
public interface Unit {
    /**
     * 计算
     * @param data
     * @return
     */
    Data Calculate(Data data);

    /**
     * 求导
     * @param data
     * @return
     */
    Data Derivative(Data data);
}
