import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by ZhangTao on 2017/7/9.
 */
public class DataUtil {
    public static Images getImages(String filePath) throws Exception{
        int rows = 0, columns = 0, count = 0, magicNum = 0;
        byte []rowsByte = new byte[4];
        byte []columnsByte = new byte[4];
        byte []magicNumByte = new byte[4];
        byte []countByte = new byte[4];
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(magicNumByte);
        fileInputStream.read(countByte);
        fileInputStream.read(rowsByte);
        fileInputStream.read(columnsByte);

        magicNum = byteArrayToInt(magicNumByte);//魔数
        count = byteArrayToInt(countByte);//总数
        rows = byteArrayToInt(rowsByte);//行数
        columns = byteArrayToInt(columnsByte);//列数

        float [][]imageArray = new float[count][];
        int i = 0;
        byte[] imageByte = new byte[rows * columns];
        while(fileInputStream.read(imageByte) != -1){
            imageArray[i] = new float[rows * columns];
            int j = 0;
            for(byte b : imageByte){
                if(b != 0){
                    int hhh = 0;
                }
                int tempi = b;
                int h = tempi & 0x000000ff;
                imageArray[i][j] = h / 255f;
                ++j;
            }
            ++i;
        }
        fileInputStream.close();
        Images images = new Images();
        images.setImageArray(imageArray);
        images.setRows(rows);
        images.setColumns(columns);
        return images;
    }
    public static byte[] getLables(String filePath) throws Exception{
        ArrayList<Byte> lables = new ArrayList<Byte>();
        int count = 0, magicNum = 0;
        byte []magicNumByte = new byte[4];
        byte []countByte = new byte[4];
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(magicNumByte);
        fileInputStream.read(countByte);
        magicNum = byteArrayToInt(magicNumByte);
        count = byteArrayToInt(countByte);
        byte []lableArray = new byte[count];
        fileInputStream.read(lableArray);
        fileInputStream.close();
        return lableArray;
    }
    private static int byteArrayToInt(byte []byteArray){
        int num = 0, temp = 0;
        for(byte b : byteArray){
            num <<= 8;
            temp = b;
            num |= temp & 0x000000ff;
        }
        return num;
    }
}
