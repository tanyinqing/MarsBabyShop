package com.xlw.babyshop.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.xlw.babyshop.model.CommonDNS;
import com.xlw.babyshop.utils.NetUtil;

/**
 * Created by xinliwei on 2015/7/18.
 *
 * 用于检查是否能上互联网的异步任务类
 */
public class NetCheckAsyncTask extends AsyncTask<Void,Void,Boolean>{

    Context mContext;
    CallBack mCallBack;

    public NetCheckAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return NetUtil.isNetworkReachable(mContext, CommonDNS.BAIDU_DNS_IP, CommonDNS.PORT);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(mCallBack != null){
            mCallBack.handleNetCheckResponse(aBoolean);
        }
    }

    public void setCallBack(CallBack callBack){
        mCallBack = callBack;
    }

    // 定义一个回调接口
    public interface CallBack{
        void handleNetCheckResponse(Boolean aBoolean);
    }
}
