import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by ZhangTao on 2017/7/11.
 */
public class Data {
    float []dataVector;
    int pages;
    int rows;
    int columns;

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setDataVector(float[] dataVector) {
        this.dataVector = dataVector;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public float[] getDataVector() {
        return dataVector;
    }

    public int getPages() {
        return pages;
    }
    public Data copy(){
        Data d = new Data();
        d.setRows(rows);
        d.setColumns(columns);
        d.setPages(pages);
        float []v = Arrays.copyOf(dataVector,dataVector.length);
        d.setDataVector(v);
        return d;
    }
    public void plus(Data data){
        if(dataVector == null || dataVector.length == 0){
            this.dataVector = Arrays.copyOf(data.dataVector, data.dataVector.length);
            this.rows = data.rows;
            this.columns = data.columns;
            this.pages = data.pages;
        }else if(this.rows == data.rows && this.columns == data.columns && this.pages == data.pages){
            for(int i = 0; i < this.dataVector.length; ++i){
                this.dataVector[i] += data.dataVector[i];
            }
        }
    }
}
