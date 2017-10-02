import java.io.*;
import java.util.concurrent.Executors;

/**
 * Created by ZhangTao on 2017/8/5.
 */
public class ttt {
    public static void main(String []arg) {
        File file = new File("D:\\result.txt");
        try {
            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String temp = null;
            int sum = 0;
            while((temp = bufferedReader.readLine()) != null){
                String []a = temp.split(" ");
                if(a[0].equals(a[1])){
                    ++sum;
                }else{
                    System.out.println(temp);
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
