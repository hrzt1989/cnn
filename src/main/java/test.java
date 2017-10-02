import java.util.Arrays;

/**
 * Created by ZhangTao on 2017/7/9.
 */
public class test {
    public static void main(String []arg){
        String trainImageFileName = "D:\\machineLearningData\\mnist\\train-images.idx3-ubyte";
        String trainLableFileName = "D:\\machineLearningData\\mnist\\train-labels.idx1-ubyte";

        String testImageFileName = "D:\\machineLearningData\\mnist\\t10k-images.idx3-ubyte";
        String testLableFileName = "D:\\machineLearningData\\mnist\\t10k-labels.idx1-ubyte";
        Log.createLog("D:\\log.txt");
        try {
            Images images = DataUtil.getImages(trainImageFileName);
            byte []lables = DataUtil.getLables(trainLableFileName);
            ConvolutionalNeuralNetwork convolutionalNeuralNetwork = new ConvolutionalNeuralNetwork();
            convolutionalNeuralNetwork.setImages(images);
            convolutionalNeuralNetwork.setLables(Arrays.copyOf(lables, lables.length));
            convolutionalNeuralNetwork.generateNetWork();
            convolutionalNeuralNetwork.training();
            Images testImg = DataUtil.getImages(testImageFileName);
            byte []testLables = DataUtil.getLables (testLableFileName);
            Log.close();
            int sum = 0;
            for(int i = 0; i < testLables.length; ++i){
                Data data = new Data();
                data.setPages(1);
                data.setRows(28);
                data.setColumns(28);
                data.setDataVector(testImg.getImageList()[i]);
                int l = convolutionalNeuralNetwork.classify(data);
                if(l == testLables[i]){
                    ++sum;
                }
                System.out.println("" + l + " " + testLables[i]);
            }
            System.out.println(sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
