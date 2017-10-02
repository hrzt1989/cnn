import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZhangTao on 2017/7/10.
 */
public class ConvolutionalNeuralNetwork {
    private Layer firstLayer;
    private Connecttion upInput;
    private List<Connecttion> downInput;
    private List<Connecttion>  output;
    private byte[] lables;
    private Images images;

    public void setUpInput(Connecttion upInput) {
        this.upInput = upInput;
    }

    public void setDownInput(List<Connecttion> downInput) {
        this.downInput = downInput;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setLables(byte[] lables) {
        this.lables = lables;
    }

    public void training(){
        int rows = images.getRows();
        int column = images.getColumns();
        float [][]datas = images.getImageList();
        for(int c = 0; c < 500; ++c){
            for(int i = 0; i < datas.length; ++i){
                Log.write("data" + i + "start");
                Data data = new Data();
                data.setColumns(column);
                data.setRows(rows);
                data.setPages(1);
                data.setDataVector(datas[i]);
                upInput.setData(data);
                Layer layer = firstLayer;
                Layer pLayer = null;
                int kk = 0;
                while(layer != null){
                    Log.write("正向第" + kk + "层开始");
                    layer.Calculation();
                    pLayer = layer;
                    layer = layer.getNextLayer();
                    Log.write("正向第" + kk + "层结束");
                    ++kk;
                }
                float []inputVector = new float[output.size()];
                Log.write("*********************************************************" + lables[i]);
 //               System.out.println("*********************************************************" + lables[i]);
                for(int j = 0; j < output.size(); ++j){
                    inputVector[j] = output.get(j).getData().getDataVector()[0];
//                    System.out.print(inputVector[j] + ",");
                }
//                System.out.println();
                Log.write("结果",inputVector);
//                float []dOutput = derivativeLossFunc(inputVector, lables[i]);
                float err = error2Sum(inputVector, NumberVector.numberVectorOneHot[lables[i]]);
//                System.out.println("误差" + err);
                Log.write("误差" + err);
                LearnRatio.setLearnRatio(err, (float) Math.pow(1.1d, c));
                float []dOutput = derivativeOneHot(inputVector, lables[i]);
                Log.write("导数", dOutput);
                int dk = 0;
                for(Connecttion connecttion : downInput){
                    float []t = new float[1];
                    t[0] = dOutput[dk];
                    Data tempData = new Data();
                    tempData.setPages(1);
                    tempData.setRows(1);
                    tempData.setColumns(1);
                    tempData.setDataVector(t);
                    connecttion.setData(tempData);
                    ++dk;
                }
                kk = 0;
                while(pLayer != null){
                    Log.write("反向第" + kk + "层开始");
                    pLayer.BackCalculation();
                    pLayer = pLayer.getPreviousLayer();
                    Log.write("反向第" + kk + "层结束");
                    ++kk;
                }
                Log.write("data" + i + "end");
            }
        }
    }

    /**
     * softMax求导
     * @param input
     * @param lable
     * @return
     */
    private float []derivativeSoftMax(float []input, byte lable){
        float []result = new float[input.length];
        double []eVector = new double[NumberVector.numberVector.length];
        double [][]subVector = new double[NumberVector.numberVector.length][input.length];
        for(int i = 0; i < NumberVector.numberVector.length; ++i){
            for(int j = 0; j < NumberVector.numberVector[i].length; ++j){
                subVector[i][j] = input[j] - NumberVector.numberVector[i][j];
            }
        }
        double totalCounte = 0.0f;
        for(int i = 0; i < NumberVector.numberVector.length; ++i){
            double temSum = 0;
            for(int j = 0; j < subVector.length; ++j){
                temSum += subVector[i][j] * subVector[i][j];
            }
            eVector[i] = Math.pow(Math.E, temSum);
            totalCounte += eVector[i];
        }
        for(int i = 0; i < result.length; ++i){
            double temSum = 0;
            for(int j = 0; j < eVector.length; ++j){
                temSum += eVector[j] * subVector[j][i];
            }
            result[i] = (float) (subVector[lable][i] - temSum / totalCounte);
        }
 //       System.out.println(eVector[lable] / totalCounte);
        return result;
    }
    private float []derivativeOneHot(float []input, byte lable){
        float []result = new float[input.length];
        for(int i = 0; i < input.length; ++i){
            result[i] = input[i] - NumberVector.numberVectorOneHot[lable][i];
        }
        return result;
    }


    /**
     * 求损失函数对各个输出变量的梯度
     * @param input
     * @return
     */
    private float []derivativeLossFunc(float []input, byte lable){
        int l = input.length;
        int count = NumberVector.numberVector.length;
        double []sumVecotr = new double[l];
        double sum2 = 0;
        double esum = 0;
        float []result = new float[l];
        float []rresult = NumberVector.numberVector[lable];
        for(int i = 0; i < count; ++i){
            if(i == lable){
                for(int j = 0; j < l; ++j){
                    result[j] = input[j] - rresult[j];
                }
                continue;
            }
            float []tempv = NumberVector.numberVector[i];
            for(int j = 0; j < NumberVector.column; ++j){
                double temp = input[j] - tempv[j];
                sumVecotr[j] += temp;
                sum2 += temp * temp;
            }
        }
        sum2 /= 2;
        esum = Math.pow(Math.E, sum2 * -1);
        double denominator = Math.pow(Math.E, -0.000005) + esum;
        for(int i = 0; i < l; ++i){
            result[i] += (esum * sumVecotr[i]) / denominator;
        }
        return result;
    }
    public int classify(Data data){
        upInput.setData(data);
        Layer layer = firstLayer;
        while(layer != null){
            layer.Calculation();
            layer = layer.getNextLayer();
        }
        float []resultVec = new float[output.size()];
        int i = 0;
        for(Connecttion connecttion : output){
            resultVec[i] = connecttion.getData().getDataVector()[0];
            ++i;
        }
        int index = 0;
        for(i = 1; i < resultVec.length; ++i){
            if(resultVec[i] > resultVec[index]){
                index = i;
            }
        }
//       float m = Float.MIN_VALUE;
//        int index = 0;
//        for(int j = 0; j < NumberVector.rows; ++j){
//            float s = error2Sum(resultVec, NumberVector.numberVector[j]);
//            System.out.println(s);
//            if(s > m){
//                m = s;
//                index = j;
//            }
//        }
        return index;
    }
    private float error2Sum(float []vec1, float []vec2){
        float sum = 0;
        for(int i = 0; i < vec1.length; ++i){
            float temp = vec1[i] - vec2[i];
            sum += temp * temp;
        }
        return sum;
    }

    /**
     * 构建卷积神经网络
     */
    public void generateNetWork(){
        NeuronLayer firstLayer = new NeuronLayer();
        NeuronLayer secondLayer = new NeuronLayer();
        NeuronLayer thirdLayer = new NeuronLayer();
        firstLayer.setNextLayer(secondLayer);
        secondLayer.setNextLayer(thirdLayer);
        thirdLayer.setPreviousLayer(secondLayer);
        secondLayer.setPreviousLayer(firstLayer);
        this.firstLayer = firstLayer;
        Connecttion upInput = new Connecttion();
        this.upInput = upInput;
        //从下向上传导
        List<Connecttion> upInputList = new ArrayList();
        upInputList.add(upInput);
        //下采样
        DownSamplingUnit downSamplingUnit = new DownSamplingUnit();
        //第一层神经元
        NeuronUnit neuronUnitL11 = new NeuronUnit();
        NeuronUnit neuronUnitL12 = new NeuronUnit();
        NeuronUnit neuronUnitL13 = new NeuronUnit();
        NeuronUnit neuronUnitL14 = new NeuronUnit();
        //卷积神经元
        ConvolutionUnit convolutionUnitL11 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL12 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL13 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL14 = new ConvolutionUnit();
        convolutionUnitL11.init(11,11,1,0);
        convolutionUnitL12.init(11,11,1,0);
        convolutionUnitL13.init(11,11,1,0);
        convolutionUnitL14.init(11,11,1,0);
        //relu
        ReluUnit reluL11 = new ReluUnit();
        ReluUnit reluL12 = new ReluUnit();
        ReluUnit reluL13 = new ReluUnit();
        ReluUnit reluL14 = new ReluUnit();
        //组装神经元
        neuronUnitL11.addUnit(convolutionUnitL11);
        neuronUnitL11.addUnit(reluL11);
        neuronUnitL11.addUnit(downSamplingUnit);

        neuronUnitL12.addUnit(convolutionUnitL12);
        neuronUnitL12.addUnit(reluL12);
        neuronUnitL12.addUnit(downSamplingUnit);

        neuronUnitL13.addUnit(convolutionUnitL13);
        neuronUnitL13.addUnit(reluL13);
        neuronUnitL13.addUnit(downSamplingUnit);

        neuronUnitL14.addUnit(convolutionUnitL14);
        neuronUnitL14.addUnit(reluL14);
        neuronUnitL14.addUnit(downSamplingUnit);

        firstLayer.addUnit(neuronUnitL11);
        firstLayer.addUnit(neuronUnitL12);
        firstLayer.addUnit(neuronUnitL13);
        firstLayer.addUnit(neuronUnitL14);
        neuronUnitL11.setUpInput(upInputList);
        neuronUnitL12.setUpInput(upInputList);
        neuronUnitL13.setUpInput(upInputList);
        neuronUnitL14.setUpInput(upInputList);
        //第一层输入，upInput
        Connecttion upOutputL11 = new Connecttion();
        Connecttion upOutputL12 = new Connecttion();
        Connecttion upOutputL13 = new Connecttion();
        Connecttion upOutputL14 = new Connecttion();
        neuronUnitL11.setUpOutput(upOutputL11);
        neuronUnitL12.setUpOutput(upOutputL12);
        neuronUnitL13.setUpOutput(upOutputL13);
        neuronUnitL14.setUpOutput(upOutputL14);
        //第二层神经元
        NeuronUnit neuronUnitL21 = new NeuronUnit();
        NeuronUnit neuronUnitL22 = new NeuronUnit();
        NeuronUnit neuronUnitL23 = new NeuronUnit();
        NeuronUnit neuronUnitL24 = new NeuronUnit();
        NeuronUnit neuronUnitL25 = new NeuronUnit();
        NeuronUnit neuronUnitL26 = new NeuronUnit();
        //卷积神经元
        ConvolutionUnit convolutionUnitL21 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL22 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL23 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL24 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL25 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL26 = new ConvolutionUnit();
        convolutionUnitL21.init(6,6,2,0);
        convolutionUnitL22.init(6,6,2,0);
        convolutionUnitL23.init(6,6,2,0);
        convolutionUnitL24.init(6,6,2,0);
        convolutionUnitL25.init(6,6,2,0);
        convolutionUnitL26.init(6,6,2,0);
        //relu
        ReluUnit reluUnitL21 = new ReluUnit();
        ReluUnit reluUnitL22 = new ReluUnit();
        ReluUnit reluUnitL23 = new ReluUnit();
        ReluUnit reluUnitL24 = new ReluUnit();
        ReluUnit reluUnitL25 = new ReluUnit();
        ReluUnit reluUnitL26 = new ReluUnit();
        //组装神经元
        neuronUnitL21.addUnit(convolutionUnitL21);
        neuronUnitL21.addUnit(reluUnitL21);
        neuronUnitL21.addUnit(downSamplingUnit);

        neuronUnitL22.addUnit(convolutionUnitL22);
        neuronUnitL22.addUnit(reluUnitL22);
        neuronUnitL22.addUnit(downSamplingUnit);

        neuronUnitL23.addUnit(convolutionUnitL23);
        neuronUnitL23.addUnit(reluUnitL23);
        neuronUnitL23.addUnit(downSamplingUnit);

        neuronUnitL24.addUnit(convolutionUnitL24);
        neuronUnitL24.addUnit(reluUnitL24);
        neuronUnitL24.addUnit(downSamplingUnit);

        neuronUnitL25.addUnit(convolutionUnitL25);
        neuronUnitL25.addUnit(reluUnitL25);
        neuronUnitL25.addUnit(downSamplingUnit);

        neuronUnitL26.addUnit(convolutionUnitL26);
        neuronUnitL26.addUnit(reluUnitL26);
        neuronUnitL26.addUnit(downSamplingUnit);

        secondLayer.addUnit(neuronUnitL21);
        secondLayer.addUnit(neuronUnitL22);
        secondLayer.addUnit(neuronUnitL23);
        secondLayer.addUnit(neuronUnitL24);
        secondLayer.addUnit(neuronUnitL25);
        secondLayer.addUnit(neuronUnitL26);
        List<Connecttion> upInputL21 = new ArrayList<Connecttion>();
        List<Connecttion> upInputL22 = new ArrayList<Connecttion>();
        List<Connecttion> upInputL23 = new ArrayList<Connecttion>();
        List<Connecttion> upInputL24 = new ArrayList<Connecttion>();
        List<Connecttion> upInputL25 = new ArrayList<Connecttion>();
        List<Connecttion> upInputL26 = new ArrayList<Connecttion>();
        //第二层输入，upInput
        upInputL21.add(upOutputL11);
        upInputL21.add(upOutputL12);

        upInputL22.add(upOutputL12);
        upInputL22.add(upOutputL13);

        upInputL23.add(upOutputL13);
        upInputL23.add(upOutputL14);

        upInputL24.add(upOutputL11);
        upInputL24.add(upOutputL13);

        upInputL25.add(upOutputL12);
        upInputL25.add(upOutputL14);

        upInputL26.add(upOutputL11);
        upInputL26.add(upOutputL14);

        neuronUnitL21.setUpInput(upInputL21);
        neuronUnitL22.setUpInput(upInputL22);
        neuronUnitL23.setUpInput(upInputL23);
        neuronUnitL24.setUpInput(upInputL24);
        neuronUnitL25.setUpInput(upInputL25);
        neuronUnitL26.setUpInput(upInputL26);
        //第二层输出,upOutput
        Connecttion upOutputL21 = new Connecttion();
        Connecttion upOutputL22 = new Connecttion();
        Connecttion upOutputL23 = new Connecttion();
        Connecttion upOutputL24 = new Connecttion();
        Connecttion upOutputL25 = new Connecttion();
        Connecttion upOutputL26 = new Connecttion();

        neuronUnitL21.setUpOutput(upOutputL21);
        neuronUnitL22.setUpOutput(upOutputL22);
        neuronUnitL23.setUpOutput(upOutputL23);
        neuronUnitL24.setUpOutput(upOutputL24);
        neuronUnitL25.setUpOutput(upOutputL25);
        neuronUnitL26.setUpOutput(upOutputL26);
        /**
         * todo 第三层加一个神经元
         */
        //第三层神经元
        NeuronUnit neuronUnitL31 = new NeuronUnit();
        NeuronUnit neuronUnitL32 = new NeuronUnit();
        NeuronUnit neuronUnitL33 = new NeuronUnit();
        NeuronUnit neuronUnitL34 = new NeuronUnit();
        NeuronUnit neuronUnitL35 = new NeuronUnit();
        NeuronUnit neuronUnitL36 = new NeuronUnit();
        NeuronUnit neuronUnitL37 = new NeuronUnit();
        NeuronUnit neuronUnitL38 = new NeuronUnit();
        NeuronUnit neuronUnitL39 = new NeuronUnit();
        NeuronUnit neuronUnitL310 = new NeuronUnit();
        thirdLayer.addUnit(neuronUnitL31);
        thirdLayer.addUnit(neuronUnitL32);
        thirdLayer.addUnit(neuronUnitL33);
        thirdLayer.addUnit(neuronUnitL34);
        thirdLayer.addUnit(neuronUnitL35);
        thirdLayer.addUnit(neuronUnitL36);
        thirdLayer.addUnit(neuronUnitL37);
        thirdLayer.addUnit(neuronUnitL38);
        thirdLayer.addUnit(neuronUnitL39);
        thirdLayer.addUnit(neuronUnitL310);
        //第三层输入，upInput
        List<Connecttion> upInputL3 = new ArrayList<Connecttion>();
        upInputL3.add(upOutputL21);
        upInputL3.add(upOutputL22);
        upInputL3.add(upOutputL23);
        upInputL3.add(upOutputL24);
        upInputL3.add(upOutputL25);
        upInputL3.add(upOutputL26);
        neuronUnitL31.setUpInput(upInputL3);
        neuronUnitL32.setUpInput(upInputL3);
        neuronUnitL33.setUpInput(upInputL3);
        neuronUnitL34.setUpInput(upInputL3);
        neuronUnitL35.setUpInput(upInputL3);
        neuronUnitL36.setUpInput(upInputL3);
        neuronUnitL37.setUpInput(upInputL3);
        neuronUnitL38.setUpInput(upInputL3);
        neuronUnitL39.setUpInput(upInputL3);
        neuronUnitL310.setUpInput(upInputL3);
        //第三层输出，upOutput
        Connecttion upOutputL31 = new Connecttion();
        Connecttion upOutputL32 = new Connecttion();
        Connecttion upOutputL33 = new Connecttion();
        Connecttion upOutputL34 = new Connecttion();
        Connecttion upOutputL35 = new Connecttion();
        Connecttion upOutputL36 = new Connecttion();
        Connecttion upOutputL37 = new Connecttion();
        Connecttion upOutputL38 = new Connecttion();
        Connecttion upOutputL39 = new Connecttion();
        Connecttion upOutputL310 = new Connecttion();
        //卷积神经元
        ConvolutionUnit convolutionUnitL31 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL32 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL33 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL34 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL35 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL36 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL37 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL38 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL39 = new ConvolutionUnit();
        ConvolutionUnit convolutionUnitL310 = new ConvolutionUnit();
        convolutionUnitL31.init(2,2,6,0);
        convolutionUnitL32.init(2,2,6,0);
        convolutionUnitL33.init(2,2,6,0);
        convolutionUnitL34.init(2,2,6,0);
        convolutionUnitL35.init(2,2,6,0);
        convolutionUnitL36.init(2,2,6,0);
        convolutionUnitL37.init(2,2,6,0);
        convolutionUnitL38.init(2,2,6,0);
        convolutionUnitL39.init(2,2,6,0);
        convolutionUnitL310.init(2,2,6,0);
        //组装神经元
        neuronUnitL31.addUnit(convolutionUnitL31);
        neuronUnitL32.addUnit(convolutionUnitL32);
        neuronUnitL33.addUnit(convolutionUnitL33);
        neuronUnitL34.addUnit(convolutionUnitL34);
        neuronUnitL35.addUnit(convolutionUnitL35);
        neuronUnitL36.addUnit(convolutionUnitL36);
        neuronUnitL37.addUnit(convolutionUnitL37);
        neuronUnitL38.addUnit(convolutionUnitL38);
        neuronUnitL39.addUnit(convolutionUnitL39);
        neuronUnitL310.addUnit(convolutionUnitL310);

        neuronUnitL31.setUpOutput(upOutputL31);
        neuronUnitL32.setUpOutput(upOutputL32);
        neuronUnitL33.setUpOutput(upOutputL33);
        neuronUnitL34.setUpOutput(upOutputL34);
        neuronUnitL35.setUpOutput(upOutputL35);
        neuronUnitL36.setUpOutput(upOutputL36);
        neuronUnitL37.setUpOutput(upOutputL37);
        neuronUnitL38.setUpOutput(upOutputL38);
        neuronUnitL39.setUpOutput(upOutputL39);
        neuronUnitL310.setUpOutput(upOutputL310);

        List lastUpOutput = new ArrayList();
        lastUpOutput.add(upOutputL31);
        lastUpOutput.add(upOutputL32);
        lastUpOutput.add(upOutputL33);
        lastUpOutput.add(upOutputL34);
        lastUpOutput.add(upOutputL35);
        lastUpOutput.add(upOutputL36);
        lastUpOutput.add(upOutputL37);
        lastUpOutput.add(upOutputL38);
        lastUpOutput.add(upOutputL39);
        lastUpOutput.add(upOutputL310);
        output = lastUpOutput;
        //从上向下传导
        //第三层
        List<Connecttion> downInputs = new ArrayList<>();
        Connecttion downInputL31 = new Connecttion();
        Connecttion downInputL32 = new Connecttion();
        Connecttion downInputL33 = new Connecttion();
        Connecttion downInputL34 = new Connecttion();
        Connecttion downInputL35 = new Connecttion();
        Connecttion downInputL36 = new Connecttion();
        Connecttion downInputL37 = new Connecttion();
        Connecttion downInputL38 = new Connecttion();
        Connecttion downInputL39 = new Connecttion();
        Connecttion downInpitL310 = new Connecttion();
        downInputs.add(downInputL31);
        downInputs.add(downInputL32);
        downInputs.add(downInputL33);
        downInputs.add(downInputL34);
        downInputs.add(downInputL35);
        downInputs.add(downInputL36);
        downInputs.add(downInputL37);
        downInputs.add(downInputL38);
        downInputs.add(downInputL39);
        downInputs.add(downInpitL310);
        this.downInput = downInputs;
        List<Connecttion> downInputDataL31 = new ArrayList<>();
        List<Connecttion> downInputDataL32 = new ArrayList<>();
        List<Connecttion> downInputDataL33 = new ArrayList<>();
        List<Connecttion> downInputDataL34 = new ArrayList<>();
        List<Connecttion> downInputDataL35 = new ArrayList<>();
        List<Connecttion> downInputDataL36 = new ArrayList<>();
        List<Connecttion> downInputDataL37 = new ArrayList<>();
        List<Connecttion> downInputDataL38 = new ArrayList<>();
        List<Connecttion> downInputDataL39 = new ArrayList<>();
        List<Connecttion> downInputDataL310 = new ArrayList<>();
        downInputDataL31.add(downInputL31);
        downInputDataL32.add(downInputL32);
        downInputDataL33.add(downInputL33);
        downInputDataL34.add(downInputL34);
        downInputDataL35.add(downInputL35);
        downInputDataL36.add(downInputL36);
        downInputDataL37.add(downInputL37);
        downInputDataL38.add(downInputL38);
        downInputDataL39.add(downInputL39);
        downInputDataL310.add(downInpitL310);

        neuronUnitL31.setDownInput(downInputDataL31);
        neuronUnitL32.setDownInput(downInputDataL32);
        neuronUnitL33.setDownInput(downInputDataL33);
        neuronUnitL34.setDownInput(downInputDataL34);
        neuronUnitL35.setDownInput(downInputDataL35);
        neuronUnitL36.setDownInput(downInputDataL36);
        neuronUnitL37.setDownInput(downInputDataL37);
        neuronUnitL38.setDownInput(downInputDataL38);
        neuronUnitL39.setDownInput(downInputDataL39);
        neuronUnitL310.setDownInput(downInputDataL310);

        List<List<Connecttion>> layer3DownOutputs = new ArrayList<>();
        List<List<Connecttion>> lyaer2DownInputs = new ArrayList<>();
        //建立从第三层到第二层的反向链接
        for(int i = 0; i < 10; ++i){
            layer3DownOutputs.add(new ArrayList<Connecttion>());
            for(int j = 0; j < 6; ++j){
                Connecttion connecttion = new Connecttion();
                layer3DownOutputs.get(i).add(connecttion);
                if(lyaer2DownInputs.size() <= j){
                    lyaer2DownInputs.add(new ArrayList<Connecttion>());
                }
                List<Connecttion> unitInput = lyaer2DownInputs.get(j);
                unitInput.add(connecttion);
            }
        }
        neuronUnitL31.setDownOutput(layer3DownOutputs.get(0));
        neuronUnitL32.setDownOutput(layer3DownOutputs.get(1));
        neuronUnitL33.setDownOutput(layer3DownOutputs.get(2));
        neuronUnitL34.setDownOutput(layer3DownOutputs.get(3));
        neuronUnitL35.setDownOutput(layer3DownOutputs.get(4));
        neuronUnitL36.setDownOutput(layer3DownOutputs.get(5));
        neuronUnitL37.setDownOutput(layer3DownOutputs.get(6));
        neuronUnitL38.setDownOutput(layer3DownOutputs.get(7));
        neuronUnitL39.setDownOutput(layer3DownOutputs.get(8));
        neuronUnitL310.setDownOutput(layer3DownOutputs.get(9));

        neuronUnitL21.setDownInput(lyaer2DownInputs.get(0));
        neuronUnitL22.setDownInput(lyaer2DownInputs.get(1));
        neuronUnitL23.setDownInput(lyaer2DownInputs.get(2));
        neuronUnitL24.setDownInput(lyaer2DownInputs.get(3));
        neuronUnitL25.setDownInput(lyaer2DownInputs.get(4));
        neuronUnitL26.setDownInput(lyaer2DownInputs.get(5));
        //第二层与第一层链接
        List<List<Connecttion>> layer2Output = new ArrayList<>();
        List<List<Connecttion>> layer1Input = new ArrayList<>();
        int j = 0, k = 0;
        for(int i = 0; i < 6; ++i){
            layer2Output.add(new ArrayList<Connecttion>());
            if(i < 3){
                for(int c = 0; c < 2 && i < 3; ++c){
                    Connecttion connecttion = new Connecttion();
                    layer2Output.get(i).add(connecttion);
                    if(layer1Input.size() <= j + c){
                        layer1Input.add(new ArrayList<Connecttion>());
                    }
                    List<Connecttion> unitDownInputs = layer1Input.get(j + c);
                    unitDownInputs.add(connecttion);
                }
                ++j;
            }
            if(i >= 3 && i <= 4) {
                for (int c = 0; c < 4; c += 2) {
                    Connecttion connecttion = new Connecttion();
                    layer2Output.get(i).add(connecttion);
                    if(layer1Input.size() <= k + c){
                        layer1Input.add(new ArrayList<Connecttion>());
                    }
                    List<Connecttion> unitDownInputs = layer1Input.get(k + c);
                    unitDownInputs.add(connecttion);
                }
                ++k;
            }
            if(i > 4){
                Connecttion connecttion1 = new Connecttion();
                Connecttion connecttion2 = new Connecttion();
                layer2Output.get(i).add(connecttion1);
                layer2Output.get(i).add(connecttion2);
                layer1Input.get(0).add(connecttion1);
                layer1Input.get(3).add(connecttion2);
            }
        }
        neuronUnitL21.setDownOutput(layer2Output.get(0));
        neuronUnitL22.setDownOutput(layer2Output.get(1));
        neuronUnitL23.setDownOutput(layer2Output.get(2));
        neuronUnitL24.setDownOutput(layer2Output.get(3));
        neuronUnitL25.setDownOutput(layer2Output.get(4));
        neuronUnitL26.setDownOutput(layer2Output.get(5));

        neuronUnitL11.setDownInput(layer1Input.get(0));
        neuronUnitL12.setDownInput(layer1Input.get(1));
        neuronUnitL13.setDownInput(layer1Input.get(2));
        neuronUnitL14.setDownInput(layer1Input.get(3));

        neuronUnitL11.setStop(true);
        neuronUnitL12.setStop(true);
        neuronUnitL13.setStop(true);
        neuronUnitL14.setStop(true);
    }

}
