import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
/**
 * Created by ZhangTao on 2017/7/10.
 */
public class NeuronLayer implements Layer{
    private List<NeuronUnit> unitList = new ArrayList<>();
    private NeuronLayer previousLayer = null;
    private NeuronLayer nextLayer = null;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void setNextLayer(NeuronLayer nextLayer) {
        this.nextLayer = nextLayer;
    }

    public void setPreviousLayer(NeuronLayer previousLayer) {
        this.previousLayer = previousLayer;
    }

    public NeuronLayer getNextLayer() {
        return nextLayer;
    }

    public NeuronLayer getPreviousLayer() {
        return previousLayer;
    }
    public void addUnit(NeuronUnit unit){
        unitList.add(unit);
    }
    public void Calculation(){
//        int i = 0;
//        for(NeuronUnit unit : unitList){
//            Log.write("正向第" + i + "个神经元");
//            unit.operation();
//            ++i;
//        }
        final CountDownLatch countDownLatch = new CountDownLatch(unitList.size());
        for(final NeuronUnit unit : unitList){
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    unit.operation();
                    countDownLatch.countDown();
                }
            };
            executorService.submit(task);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void BackCalculation(){
//        for(int i = unitList.size() - 1; i >= 0; --i){
//            Log.write("反向第" + i + "个神经元");
//            int ti = i;
//            unitList.get(ti).backOperation();
//        }
        final CountDownLatch countDownLatch = new CountDownLatch(unitList.size());
        for(int i = unitList.size() - 1; i >= 0; --i){
            final int ti = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    unitList.get(ti).backOperation();
                    countDownLatch.countDown();
                }
            };
            executorService.submit(task);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
