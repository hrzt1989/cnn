import java.util.Arrays;
import java.util.Random;

/**
 * Created by ZhangTao on 2017/7/9.
 */
public class ConvolutionUnit implements Unit{
    private float []kernel;//卷积核
    private int rows;//卷积的行数
    private int columns;//卷积的列数
    private int pages;//卷积的页数
    private float bias;//偏移量
//    private float l = 0.001f;//学习步长
    private Data lastData;

//    public void setL(float l) {
//        this.l = l;
//    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }

    public Data Calculate(Data data){
//        System.out.println("Convolution Calculate start");
        lastData = data;
        int r = data.getRows();
        int c = data.getColumns();
        //做完卷积以后的行和列
        int nr = r + 1 - rows, nc = c + 1 - columns;
        float []result = new float [nr * nc];
        Arrays.fill(result, bias);
        float []dataVector = data.getDataVector();
        for(int i = 0; i < nr; ++i){
            for(int j = 0; j < nc; ++j){
                result[i * nc + j] += operationOnce(dataVector, r, c, i, j);
            }
        }
        Data rd = new Data();
        rd.setPages(1);
        rd.setRows(nr);
        rd.setColumns(nc);
        rd.setDataVector(result);
        Log.write("Calculate 输入 ",data.getDataVector());
        Log.write("Calculate kernel ",kernel);
        Log.write("Calculate 输出 ",result);
 //       System.out.println("Convolution Calculate end");
        return rd;
    }
    public void setKernel(float[] kernel) {
        this.kernel = kernel;
    }
    public void init(int rows, int columns, int pages, int bias){
        kernel = new float[pages * rows * columns];
        this.rows = rows;
        this.columns = columns;
        this.pages = pages;
        this.bias = bias;
        Random r =  new Random(System.currentTimeMillis());
        float maxEnd = (float )1 / (pages * rows * columns);
        float minEnd = 0.0f;
        for(int i = 0; i <kernel.length; ++i){
            kernel[i] = r.nextFloat() * (maxEnd - minEnd) + minEnd;
        }
        this.bias = r.nextFloat() * (maxEnd - minEnd) + minEnd;
    }

    /**
     * 一次卷积操作
     * @param data
     * @param r data的行数
     * @param c data的列数
     * @param si 当前需要就算的点的行坐标
     * @param sj 当前需要结算的点的列坐标
     * @return
     */
    private float operationOnce(float []data, int r, int c,
                                int si, int sj){
        float result = 0;
        for(int p = 0; p < pages; ++p){
            for(int i = 0; i < rows; ++i){
                for(int j = 0; j < columns; ++j){
                    result += data[p * r * c + (i + si) * c + (j + sj)] *
                            kernel[p * rows * columns + i * columns + j];
                }
            }
        }
        return result;
    }
    public Data Derivative(Data data){
 //       System.out.println("Convolution Derivative start");
        Log.write("Derivative 输入 ",data.getDataVector());
        Log.write("更新前Derivative kernel ",kernel);
        float []deltKernel = new float[rows * columns * pages];
        float []deltInput = new float[lastData.getRows() * lastData.getColumns() * lastData.getPages()];
        float []lastInputData = lastData.getDataVector();
        float biasTemp = 0;
        Arrays.fill(deltKernel, 0);
        Arrays.fill(deltInput,0);
        float []dataVector = data.getDataVector();
        int rowsTemp = data.getRows();
        int columnsTemp = data.getColumns();
        int lrows = lastData.getRows();
        int lcolumn = lastData.getColumns();
        for(int i = 0; i < rowsTemp; ++i){
            for(int j = 0; j < columnsTemp; ++j){
                biasTemp += dataVector[i * columnsTemp + j];
                for(int p = 0; p < pages; ++p){
                    for(int ki = 0; ki < rows; ++ki){
                        for(int kj = 0; kj < columns; ++kj){
                            deltKernel[p * rows * columns + ki * columns + kj] +=
                                    dataVector[i * columnsTemp + j] *
                                            lastInputData[p * lrows * lcolumn + (i + ki) * lcolumn + j + kj];
                        }
                    }
                }
            }
        }
//        double kl = 0;
//        for(int i = 0; i < pages * rows * columns; ++i){
//            kl += deltKernel[i] * deltKernel[i];
//        }
//        kl = Math.sqrt(kl + biasTemp * biasTemp);
//        Log.write("更新向量 ",deltKernel);
//        kl = 1;
        float l = LearnRatio.getL();
//        if(Math.abs(kl) > 0.000017){
            for(int i = 0; i < pages * rows * columns; ++i){
                kernel[i] -= deltKernel[i] * l;
            }
            bias -= biasTemp * l;
 //       }
        for(int i = 0; i < rowsTemp; ++i){
            for(int j = 0; j < columnsTemp; ++j){
                for(int p = 0; p < pages; ++p){
                    for(int ki = 0; ki < rows; ++ki){
                        for(int kj = 0; kj < columns; ++kj){
                            deltInput[p * lrows * lcolumn + (i + ki) * lcolumn + j + kj] +=
                                    dataVector[i * columnsTemp + j] * kernel[p * rows * columns + ki * columns + kj];
                        }
                    }
                }
            }
        }
        Data result = new Data();
        result.setPages(lastData.getPages());
        result.setRows(lastData.getRows());
        result.setColumns(lastData.getColumns());
        result.setDataVector(deltInput);

        Log.write("更新后Derivative kernel ",kernel);
        Log.write("Derivative 输出 ",deltInput);
//        System.out.println("Convolution Derivative end");
        return result;
    }
    public static void main(String []arg){
        Data upInputdata = new Data();
        upInputdata.setPages(1);
        upInputdata.setRows(3);
        upInputdata.setColumns(3);
        float []upInputDataVector = new float[3 * 3];
        for(int i = 0; i < 9; ++i){
         upInputDataVector[i] = i;
        }
        upInputdata.setDataVector(upInputDataVector);
        ConvolutionUnit convolutionUnit = new ConvolutionUnit();
        convolutionUnit.setBias(1);
        convolutionUnit.setRows(2);
        convolutionUnit.setColumns(2);
        convolutionUnit.setPages(1);
        float []kernel = new float[4];
        Arrays.fill(kernel, 0);
        convolutionUnit.setKernel(kernel);
        Data downInputData = new Data();
        downInputData.setColumns(2);
        downInputData.setRows(2);
        downInputData.setPages(1);
        float []downInputDataVector = new float[4];
        Arrays.fill(downInputDataVector,1);
        downInputData.setDataVector(downInputDataVector);
        Data upResult = convolutionUnit.Calculate(upInputdata);
        Data downResult = convolutionUnit.Derivative(downInputData);
    }
}