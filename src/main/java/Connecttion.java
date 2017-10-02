import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**链接单元，之存储上层或下层某一单元的运算结果
 * Created by ZhangTao on 2017/7/9.
 */
public class Connecttion {
    private Data data;

    private Queue<Data> dataQueue = new LinkedList<>();

    public void addData(Data data){
        dataQueue.add(data);
    }
    public Data popData(){
        return dataQueue.poll();
    }
    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
