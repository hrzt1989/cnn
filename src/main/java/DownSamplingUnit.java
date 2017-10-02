import java.util.Arrays;

/**
 * Created by ZhangTao on 2017/7/9.
 */
public class DownSamplingUnit implements Unit{
    @Override
    public Data Calculate(Data data){
 //       System.out.println("DownSamplingUnit Calculate start");
        int c = data.getColumns();
        int nr = data.getRows() / 2, nc = data.getColumns() / 2;
        float []result = new float[nr * nc];
        float []dv = data.getDataVector();
        for(int i = 0; i < nr; ++i){
            for(int j = 0; j < nc; ++j){
                float temp = dv[i * 2 * c + j * 2] +
                        dv[i * 2 * c + j * 2 + 1] +
                        dv[(i * 2 + 1) * c + j * 2] +
                        dv[(i * 2 + 1) * c + j * 2 + 1];
                result[i * nc + j] = temp / 4;
            }
        }
        Data rd = new Data();
        rd.setRows(nr);
        rd.setColumns(nc);
        rd.setPages(1);
        rd.setDataVector(result);
//        System.out.println("DownSamplingUnit Calculate end");
        return rd;
    }
    @Override
    public Data Derivative(Data data){
//        System.out.println("DownSamplingUnit Derivative start");
        Data result = new Data();
        int r = data.getRows();
        int c = data.getColumns();
        int nr = data.getRows() * 2;
        int nc = data.getColumns() * 2;
        int pages = data.getPages();
        float []datavector = new float[nr * nc * pages];
        float []vector = data.getDataVector();
        Arrays.fill(datavector,0.0f);
        for(int p = 0; p < pages; ++p){
            for(int i = 0; i < r; ++i){
                for(int j  = 0; j < c; ++j){
                    datavector[i * 2 * nc + j * 2 ] = vector[i * c + j] * 0.25f;
                    datavector[(i * 2 + 1) * nc + j * 2 ] = vector[i * c + j] * 0.25f;
                    datavector[i * 2  * nc + j * 2 + 1] = vector[i * c + j] * 0.25f;
                    datavector[(i * 2 + 1)  * nc + j * 2 + 1] = vector[i * c + j] * 0.25f;
                }
            }
        }
        result.setRows(nr);
        result.setColumns(nc);
        result.setPages(pages);
        result.setDataVector(datavector);
//        System.out.println("DownSamplingUnit Derivative end");
        return result;
    }
}
