import java.util.Arrays;

/**
 * Created by ZhangTao on 2017/7/11.
 */
public class ReluUnit implements Unit{
    private Data pData;
    @Override
    public Data Calculate(Data data){
 //       System.out.println("ReluUnit Calculate start");
        pData = data.copy();
        float []v = pData.getDataVector();
        for(int i = 0; i < v.length; ++i){
            v[i] = max(0, v[i]);
        }
 //       System.out.println("ReluUnit Calculate end");
        return pData.copy();
    }
    float max(float a, float b){
        return a > b ? a : b;
    }
    @Override
    public Data Derivative(Data data){
 //       System.out.println("ReluUnit Derivative start");
        for(int i = 0; i < pData.getDataVector().length; ++i){
            if(Math.abs(pData.getDataVector()[i]) < 0.000001){
                data.getDataVector()[i] = 0.0f;
            }
        }
//        System.out.println("ReluUnit Derivative end");
        return data;
    }
    public static void main(String []arg){
        Data data = new Data();
        data.setPages(1);
        data.setRows(4);
        data.setColumns(4);
        float []dataVector = new float[1 * 4 * 4];
        Arrays.fill(dataVector,-1f);
        data.setDataVector(dataVector);
        ReluUnit reluUnit = new ReluUnit();
        Data result = reluUnit.Calculate(data);
    }
}
