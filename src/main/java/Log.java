import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhangTao on 2017/7/30.
 */
public class Log {
    static private FileOutputStream fileOutputStream;
    static private boolean stop = true;
    static private Lock lock = new ReentrantLock();
    static public void createLog(String path){
        try {
            if(stop){
                return;
            }
            fileOutputStream = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    static public void write(String s){
        try {
            if(stop){
                return;
            }
            lock.lock();
            fileOutputStream.write((s + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
    static public void write(String s, float[]data){
        try {
            if(stop){
                return;
            }
            lock.lock();
            fileOutputStream.write((s + " ").getBytes());
            for(int i = 0; i < data.length; ++i){
                Float temp = data[i];
                fileOutputStream.write((temp.toString() + ",").getBytes());
            }
            fileOutputStream.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
    static public void close(){
        if(fileOutputStream != null){
            try {
                if(stop){
                    return;
                }
                lock.lock();
                fileOutputStream.close();
                fileOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }
    }


}
