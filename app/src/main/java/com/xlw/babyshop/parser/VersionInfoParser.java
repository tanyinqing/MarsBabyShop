package com.xlw.babyshop.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xlw.babyshop.model.VersionInfoModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本解析器
 */
public class VersionInfoParser extends BaseJSONParser<VersionInfoModel> {

    /**
     * 将返回的JSON格式数据解析并转换为Java对象
     * @param paramString   服务器端返回的JSON格式字符串
     * @return              返回解析出来的Java对象
     * @throws JSONException    抛出的异常
     */
    @Override
    public VersionInfoModel parseJSON(String paramString) throws JSONException {
        // 如果服务器端正确返回JSON格式内容了
        if (!TextUtils.isEmpty(this.checkResponse(paramString))) {
            // 将其转为JSON对象
            JSONObject jsonObject = new JSONObject(paramString);
            // 获得其中的version name对应的value
            String version = jsonObject.getString("version");
            // 将JSON格式字符串转为Java对象
            Gson gson = new Gson();
            VersionInfoModel versionInfo = gson.fromJson(version,VersionInfoModel.class);
            return versionInfo;
        }
        return null;
    }
}
