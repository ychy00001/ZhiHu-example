package com.rain.zhihu_example.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Diagrams on 2016/1/7 10:06
 */
public class IOUtil {
    /**
     * 将输入流转换为字符串
     * @param inputStream
     * @return
     */
    public static String converStreamString(InputStream inputStream){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(buff))!=-1){
                baos.write(buff,0,len);
            }
            //byteArray自带缓冲区读到所有的东西
            return new String(baos.toByteArray());
        } catch (IOException e) {
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
