package com.xlw.babyshop.tasks;

import android.os.AsyncTask;

import com.xlw.babyshop.exception.AndroidMarsShopException;
import com.xlw.babyshop.utils.HttpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xinliwei on 2015/7/18.
 *
 * 执行文件下载的异步任务类
 */
public class DownloadAsyncTask extends AsyncTask<String,Integer,String> {

    private String savePath;    // 下载文件保存路径
    private CallBack downloadCallback;

    public DownloadAsyncTask(String savePath) {
        this.savePath = savePath;
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = HttpUtil.openHttpConnection(params[0]);
            int total = is.available();     // 文件总长度
            int sectionByte = total / 19;   // 进度条总长度为20,即每1/20更新一次
            int multiple = 1;               // sectionByte的倍数
            if(is != null){
                File file = new File(savePath);
                fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int charb = -1;
                int count = 0;
                while((charb = is.read(b))!=-1){
                    fos.write(b, 0, charb);
                    count += charb;
                    // 计算下载的百分比并报告进度
                    if(count >= sectionByte * multiple){
                        publishProgress(multiple); 	// 框架会自动调用onProgressUpdate()方法
                        multiple++;
                    }
                }
            }
            fos.flush();
            publishProgress(20);
        } catch (AndroidMarsShopException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }
                if(fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "更新结束";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // 回调方法,更新UI界面进度条
        downloadCallback.updateProgressbar(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        // 回调方法,处理下载结束事宜
        downloadCallback.apkDownloadDone(result);
    }

    public void setDownloadCallback(CallBack downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    public interface CallBack{
        void updateProgressbar(Integer value);
        void apkDownloadDone(String result);
    }
}
