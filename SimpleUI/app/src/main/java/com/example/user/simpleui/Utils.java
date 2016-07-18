package com.example.user.simpleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 2016/7/18.
 */
public class Utils {
    //工具0718
    public static void writeFile(Context context, String fileName, String content) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);

            fileOutputStream.write(content.getBytes());//用bytes存
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            byte[] buffer = new byte[2048];//創造buffer字串

            fileInputStream.read(buffer, 0, buffer.length);
            fileInputStream.close();
            return new String(buffer);//buffer(byte)會自動轉為String
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";//發生錯誤
    }
}

