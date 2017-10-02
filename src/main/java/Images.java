import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ZhangTao on 2017/7/9.
 */
public class Images {
    private int rows;//图片的高度
    private int columns;//图片的宽度
    private float [][]imageArray;//图片表示为一维数组，此变量表示为一共有多少张图片

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setImageArray(float [][]imageArray) {
        this.imageArray = imageArray;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public float [][]getImageList() {
        return imageArray;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    /**
     * 分割前count张图片
     * @param count
     * @return
     */
    public Images split(int count){
        Images images = new Images();
        images.setRows(this.rows);
        images.setColumns(this.columns);
        float [][]imagesArr = new float[count][];
        System.arraycopy(imageArray,0,imagesArr,0,count);
        images.setImageArray(imagesArr);
        return images;
    }

}
