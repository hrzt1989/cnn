import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ZhangTao on 2017/7/11.
 */
public class NeuronUnit implements Unit{
    private ArrayList<Unit> unitList = new ArrayList<>();
    private List<Connecttion> upInput;
    private Data upInputData;
    private Data douwInputData;
    private Connecttion upOutput;
    private List<Connecttion> downInput;
    private List<Connecttion> downOutput;
    private boolean stop = false;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void operation(){
        upInputData = getUpInputData(upInput);
        upOutput.setData(Calculate(upInputData));
 //       System.out.println("operation end");
    }
    public void backOperation(){
        douwInputData = getDownInputData();
        setDownOutputList(devideDataByPage(Derivative(douwInputData)));
    }
    private void setDownOutputList(List<Data> dataList){
        if(stop){
            return;
        }
        int i = 0;
        for(Data data : dataList){
            downOutput.get(i).setData(data);
            ++i;
        }
    }

    private List<Data> devideDataByPage(Data data){
        int rowsTemp = data.getRows();
        int columnsTemp = data.getColumns();
        int pagesTemp = data.getPages();
        List<Data> dataList = new LinkedList<Data>();
        for(int p = 0; p < pagesTemp; ++p){
            Data dataTemp = new Data();
            float []dataVectorTemp = new float[rowsTemp * columnsTemp];
            System.arraycopy(data.getDataVector(),p * rowsTemp * columnsTemp,
                    dataVectorTemp, 0,rowsTemp * columnsTemp);
            dataTemp.setRows(rowsTemp);
            dataTemp.setColumns(columnsTemp);
            dataTemp.setPages(1);
            dataTemp.setDataVector(dataVectorTemp);
            dataList.add(dataTemp);
        }
        return dataList;
    }
    @Override
    public Data Derivative(Data data){
        Data temp = data.copy();
        int i = unitList.size() -1;
        while(i >= 0){
            Unit unit = unitList.get(i);
            temp = unit.Derivative(temp);
            --i;
        }
        return temp;
    }
    @Override
    public Data Calculate(Data data){
        Data temp = data.copy();
        for(Unit unit : unitList){
            temp = unit.Calculate(temp);
        }
        return temp;
    }
    private Data getUpInputData(List<Connecttion> connecttionList){
        Data d = new Data();
        int pages = 0,r = 0, c = 0;
        r = connecttionList.get(0).getData().getRows();
        c = connecttionList.get(0).getData().getColumns();
        for(Connecttion connecttion : connecttionList){
            pages += connecttion.getData().getPages();
        }
        float []v = new float[pages * r * c];
        int s = 0;
        for(Connecttion connecttion : connecttionList){
            System.arraycopy(connecttion.getData().getDataVector(), 0, v, s, connecttion.getData().getDataVector().length);
            s += connecttion.getData().getDataVector().length;
        }
        d.setDataVector(v);
        d.setPages(pages);
        d.setRows(r);
        d.setColumns(c);
        return d;
    }
    public Data getDownInputData(){
        Data data = new Data();
        for(Connecttion connecttion : downInput){
            data.plus(connecttion.getData());
        }
        return data;
    }

    public void addUnit(Unit unit){
        unitList.add(unit);
    }
    public void setDownInput(List<Connecttion> downInput) {
        this.downInput = downInput;
    }

    public void setDownOutput(List<Connecttion> downOutput) {
        this.downOutput = downOutput;
    }

    public void setUpInput(List<Connecttion> upInput) {
        this.upInput = upInput;
    }

    public void setUpOutput(Connecttion upOutput) {
        this.upOutput = upOutput;
    }

    public List<Connecttion> getDownOutput() {
        return downOutput;
    }

    public Connecttion getUpOutput() {
        return upOutput;
    }

    public List<Connecttion> getDownInput() {
        return downInput;
    }

    public List<Connecttion> getUpInput() {
        return upInput;
    }
}
