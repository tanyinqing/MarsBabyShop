package com.xlw.babyshop.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.tandong.sa.activity.SmartActivity;
import com.tandong.sa.appInfo.AppInfo;
import com.xlw.babyshop.application.XlwApplication;
import com.xlw.babyshop.model.RequestModel;
import com.xlw.babyshop.model.VersionInfoModel;
import com.xlw.babyshop.parser.BaseJSONParser;
import com.xlw.babyshop.parser.VersionInfoParser;
import com.xlw.babyshop.tasks.DownloadAsyncTask;
import com.xlw.babyshop.tasks.NetCheckAsyncTask;
import com.xlw.babyshop.utils.Logger;
import com.xlw.babyshop.utils.ToastUtil;
import com.xlw.babyshop.utils.VolleyUtil;
import com.xlw.marsbabyshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class WelcomeActivity extends SmartActivity implements VolleyUtil.ResponseCallback {

    private final String TAG = "WelcomeActivity.class";

    private AppInfo appInfo;    // SmartAndroid包中的AppInfo类,包含APP应用的信息

    private String clientVersion;       // 客户端版本信息
    private VersionInfoModel serverVersionInfoModel;  // 从服务器获取的版本信息.Version为自定义的版本信息数据类
    private RequestModel requestModel;  // 请求对象模型

    private ProgressDialog mProgressDialog; // 进度条
    private int progressVaue;               // 进度条当前的值
    private File file;                      // 下载的apk文件
    DownloadAsyncTask downloadAsyncTask;    // 执行apk下载的异步任务类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 获取当前App应用版本名
        appInfo = new AppInfo(this);
        clientVersion = appInfo.getVerName(this);

        // 或者
//        AppInfoUtil appInfoUtil = new AppInfoUtil(this);
//        clientVersion = appInfoUtil.getClientVersion();

        // 在欢迎页面中显示当前版本号
        ((TextView) findViewById(R.id.welcome_version)).setText(clientVersion);

        // 检测版本更新
        checkVersion();
    }

    // 检测版本更新
    private void checkVersion() {
        // 这里执行异步任务类
        NetCheckAsyncTask netCheckTask = new NetCheckAsyncTask(this);
        netCheckTask.setCallBack(new NetCheckAsyncTask.CallBack() {
            @Override
            public void handleNetCheckResponse(Boolean aBoolean) {
                if (aBoolean) {
                    // 如果可以联上互联网,就进行版本检查
                    //没有服务端就不检查版本了
                    //checkServerAppVersion();
                    gotoHome();
                } else {
                    // 如果无法联上互联网
                    ToastUtil.showLongMsg(WelcomeActivity.this, "无法连接互联网");
                    gotoHome();     // 跳转到主Activity
                }
            }
        });
        netCheckTask.execute();     // 执行异步任务类
    }

    // 连接服务器,检查版本号
    private void checkServerAppVersion() {
        // 创建一个版本信息的JSON解析器,用来解析服务器端返回的JSON格式数据
        BaseJSONParser<VersionInfoModel> jsonParser = new VersionInfoParser();
        // 建立请求对象模型
        String requestUrl = this.getResources().getString(R.string.url_version);    // "version"

        requestModel = new RequestModel();
        // 设置请求的url
        requestModel.setRequestUrl(requestUrl);
        // 设置模型的解析器
        requestModel.setJsonParser(jsonParser);

        // 向服务器端请求版本信息
        // "http://10.0.3.2:8084/marshop/" + "version"
        String versioninfoUrl = this.getResources().getString(R.string.app_host).concat(requestUrl);

        VolleyUtil volleyUtil = new VolleyUtil();
        volleyUtil.setResponseCallback(this);       // 设置回调监听器
        volleyUtil.requestString(versioninfoUrl);   // 向服务器异步请求版本信息
    }

    // 当异步请求版本信息从服务器端返回时,回调此方法
    @Override
    public void responseString(String responseText) {
        Logger.d(TAG, responseText);
        try {
            if (invilidateLogin(responseText)) {
//                return Status.Login;
                ToastUtil.showShortMsg(this, "请先登录");
                return;
            }
            // 解析
            serverVersionInfoModel = (VersionInfoModel) requestModel.jsonParser.parseJSON(responseText);
        } catch (JSONException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
        if (serverVersionInfoModel != null) {
            String v = serverVersionInfoModel.getVersion();

            Logger.d(TAG, "获取当前服务器版本号为 ：" + v);
            if (clientVersion.equals(v)) {
                // 清理工作
                File updateFile = new File(XlwApplication.getInstance().cacheDir, "marsbaby.apk");
                if (updateFile.exists()) {
                    //当不需要的时候，清除之前的下载文件，避免浪费用户空间
                    updateFile.delete();
                }
                gotoHome();     // 然后跳转到主Activity
            } else {
                // 如果服务器版本号与当前客户端版本号不一致
                showUpdateDialog(); // 显示一个提示下载新版本的对话框
            }
        } else {
            gotoHome();
        }
    }

    @Override
    public void responseJSONObject(JSONObject jsonObject) {
    }

    @Override
    public void responseJsonArray(JSONArray jsonArray) {
    }

    /**
     * 初始化进度条
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this); // 下载进度条初始化
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.downning));
        mProgressDialog.setMax(20);     // 最大刻度为20
        mProgressDialog.setProgress(0);
        mProgressDialog.show();
    }

    /**
     * 进入主页
     */
    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    // 显示提示用户下载新版本的对话框
    private void showUpdateDialog() {
        Logger.d(TAG, "更新版本提示");

        new AlertDialog.Builder(WelcomeActivity.this)
                .setTitle("升级提醒")
                .setMessage("亲，有新的版本赶快升级!")
                .setCancelable(false)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downApk();  // 从服务器下载新版本的Apk
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logger.d(TAG, "不更新直接进入主界面");
                        gotoHome();
                    }
                }).show();
    }

    /**
     * 从服务器下载新的Apk
     */
    private void downApk() {
        // 显示下载进度条
        initProgressDialog();

        // 创建下载的apk文件对象
        file = new File(XlwApplication.getInstance().cacheDir, "marsbaby.apk");

        // 执行异步下载,构造器参数为下载apk保存路径,而execute()参数为下载URL
        downloadAsyncTask = new DownloadAsyncTask(file.getAbsolutePath());
        // 设置下载过程中的回调方法
        downloadAsyncTask.setDownloadCallback(new DownloadAsyncTask.CallBack() {
            @Override
            public void updateProgressbar(Integer value) {
                // 在下载过程中,每下载1/20,就更新一次进度条
                progressVaue = value;
                mProgressDialog.setProgress(progressVaue);
            }

            @Override
            public void apkDownloadDone(String result) {
                // 下载结束
                // 如果更新结束，进度条重不可见
                mProgressDialog.setProgress(0);
                mProgressDialog.dismiss();

                // 安装apk
                installApk();
            }
        });
        downloadAsyncTask.execute(serverVersionInfoModel.getUrl());

//        ThreadPoolManager.getInstance().addTask(downLoadTask);
    }

    /**
     * 安装Apk
     */
    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    /**
     * 验证是否需要登录
     *
     * @param result
     * @return
     * @throws JSONException
     */
    private boolean invilidateLogin(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        String responseCode = jsonObject.getString("response");
        // 如果从服务器端返回的是"notlogin",说明用户未登录,返回true
        if ("notlogin".equals(responseCode)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadAsyncTask != null) {
            downloadAsyncTask.cancel(true);
            downloadAsyncTask = null;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        file = null;
    }
}