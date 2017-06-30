package com.JSONTest;

import android.util.Log;

import com.crackUtil.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AqCxBoM on 2017/4/10.
 */

public class myJson {
    static public String TAG = "myJson";
    static public void LogI(String ct)
    {
        Log.i(TAG, ct);
    }
    static public String initJson()
    {
        try {
            JSONObject jsonResult = new JSONObject();

            //构造电信结构
            JSONObject jsonEgame = new JSONObject();
            jsonEgame.put("TwiceDlg_STACK_CLASSPATH", "cn.egame.terminal.paysdk.ah");
            jsonEgame.put("TwiceDlg_STACK_METHODNAME", "e");
            jsonEgame.put("PayingDlg_STACK_CLASSPATH", "cn.egame.terminal.paysdk.cv");
            jsonEgame.put("PayingDlg_STACK_METHODNAME", "a");
            jsonEgame.put("BillFinishDlg_STACK_CLASSPATH", "");
            jsonEgame.put("BillFinishDlg_STACK_METHODNAME", "");
            jsonEgame.put("PayingString", "支付中");
            jsonEgame.put("ConfirmPay", "确认支付");
            jsonEgame.put("Confirm", "确定");
            jsonEgame.put("MoneyPrefix", "");
            jsonEgame.put("MemorySuffix", "元/次");
            jsonEgame.put("ZhifuChenggong", "支付成功");
            jsonEgame.put("ZhifuShibai", "支付失败");

            //构造咪咕结构
            JSONObject jsonMigu = new JSONObject();
            jsonMigu.put("TwiceDlg_STACK_CLASSPATH", "cn.cmgame2_0.global_manager.ChargeDataManager$1");
            jsonMigu.put("TwiceDlg_STACK_METHODNAME", "completed");
            jsonMigu.put("PayingDlg_STACK_CLASSPATH", "cn.cmgame2_0.utils.UIUtil");
            jsonMigu.put("PayingDlg_STACK_METHODNAME", "showProgressDialog");
            jsonMigu.put("BillFinishDlg_STACK_CLASSPATH", "cn.cmgame2_0.billing_model.view.C.billing.a.a$1");
            jsonMigu.put("BillFinishDlg_STACK_METHODNAME", "onBillingFinish");
            jsonMigu.put("PayingString", "");
            jsonMigu.put("ConfirmPay", "确认支付");
            jsonMigu.put("Confirm", "确定");
            jsonMigu.put("MoneyPrefix", "￥");
            jsonMigu.put("MemorySuffix", "");
            jsonMigu.put("Zhifu", "支付");
            jsonMigu.put("Chenggong", "成功");

            JSONObject jsonsdk_callback = new JSONObject();
            jsonsdk_callback.put("mainclass", "cn.cmpay.base.wrapper.SDKCallbackWrapper");

            JSONObject jsonsdk_base = new JSONObject();
            jsonsdk_base.put("force", false);
            jsonsdk_base.put("ui", true);
            jsonsdk_base.put("mainclass", "cn.cmpay.base.SDKBase");

            JSONObject jsonfiles = new JSONObject();
            jsonfiles.put("name", "data_pay_base");
            jsonfiles.put("sig", "547b68ebb2ad8a3207f96c5b8b25938c");

            JSONArray jaryFiles = new JSONArray();
            jaryFiles.put(jsonfiles);
            jsonsdk_base.put("files", jaryFiles);

            JSONObject jsonsdk_pay = new JSONObject();
            jsonsdk_pay.put("force", false);
            jsonsdk_pay.put("ui", true);
            jsonsdk_pay.put("mainclass", "cn.cmpay.pay.CommonPay");

            JSONObject jsonpayfiles1 = new JSONObject();
            jsonpayfiles1.put("name", "sdk_pay_base");
            jsonpayfiles1.put("sig", "b1881e280ca8fdb7ed3cb233f9747733");

            //中括号"[]"中的是数组形式
            JSONArray jaryPayFiles = new JSONArray();
            jaryPayFiles.put(jsonpayfiles1);
            jsonsdk_pay.put("files", jaryPayFiles);

            jsonResult.put("version", "0.2");
            jsonResult.put("sdk_base", jsonsdk_base);
            jsonResult.put("sdk_pay", jsonsdk_pay);
            jsonResult.put("sdk_callback", jsonsdk_callback);

            //新增
            jsonResult.put("jarKey", "sdk_config");
            jsonResult.put("xmlKey", "sdk_config");
            jsonResult.put("LOGSERVERADDR", "http://cnsdk.zhuqueok.com/ab_api/plugin_log.php?");
            jsonResult.put("EgameCfg", jsonEgame);
            jsonResult.put("MiguCfg", jsonMigu);

            LogUtils.DOLOG(jsonResult.toString());
            return jsonResult.toString();
            //{"sdk_pay":{"ui":true,"files":[{"sig":"b1881e280ca8fdb7ed3cb233f9747733","name":"sdk_pay_base"},{"sig":"649af7fbee43900cf7c15a071209c243","name":"sdk_pay_cmcc"},{"sig":"d787f4dd56fa7ddd7cb0703664e30d64","name":"sdk_pay_dep"},{"sig":"fb787466cda963a1db939f43cc701e00","name":"sdk_pay_general"},{"sig":"5d05a503c28f5e6e723c7f8c2a24cbe8","name":"sdk_pay_others"},{"sig":"4c581ab47c04c9f716e83a93f588a0ac","name":"sdk_pay_union"}],"mainclass":"com.cmnpay.base.SDKBase","force":false},"sdk_base":{"ui":true,"files":[{"sig":"547b68ebb2ad8a3207f96c5b8b25938c","name":"sdk_base"}],"mainclass":"com.cmnpay.base.SDKBase","force":false},"version":"0.1"}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    static public void JsonAnalysis(String json)
    {
        if(json.startsWith("error")){//这里可以做一下检测，如果不是json格式的就直接返回
            return ;
        }
        Pet pet=new Pet();//准备返回的pet对象
        try {
            JSONObject jsonObject=new JSONObject(json);//我们需要把json串看成一个大的对象
            JSONArray jsonArray=jsonObject.getJSONArray("pet");//这里获取的是装载有所有pet对象的数组
            int nPetSize = jsonArray.length();
            int n = 0;
            while(n < nPetSize)
            {
                JSONObject jsonpet = jsonArray.getJSONObject(n);
                int id = jsonpet.getInt("petid");
                String petname = jsonpet.getString("petname");
                String pettype = jsonpet.getString("pettype");

                LogUtils.DOLOG("pet: " + n, "petid: " + id + " petname: " + petname + " pettype: " + pettype);
                n++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    static public Pet JsonToPet(String json)
    {
        if(json.startsWith("error")){//这里可以做一下检测，如果不是json格式的就直接返回
            return null;
        }
        Pet pet=new Pet();//准备返回的pet对象
        try {
            JSONObject jsonObject=new JSONObject(json);//我们需要把json串看成一个大的对象
            JSONArray jsonArray=jsonObject.getJSONArray("pet");//这里获取的是装载有所有pet对象的数组
            JSONObject jsonpet = jsonArray.getJSONObject(0);//获取这个数组中第一个pet对象

            int petid=jsonpet.getInt("petid");//获取pet对象的参数
            String petname=jsonpet.getString("petname");
            String pettype=jsonpet.getString("pettype");

            pet.setPetid(petid);//把参数添加到pet对象当中。
            pet.setPetname(petname);
            pet.setPettype(pettype);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogI("json To Pet:"+pet.toString());//打印出pet对象参数。
        return pet;
    }
    static public String  petToJson(Pet pet)
    {
        String jsonresult = "";//定义返回字符串
        JSONObject object = new JSONObject();//创建一个总的对象，这个对象对整个json串
        try {
            JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象
            JSONObject jsonObj = new JSONObject();//pet对象，json形式
            jsonObj.put("petid", pet.getPetid());//向pet对象里面添加值
            jsonObj.put("petname", pet.getPetname());
            jsonObj.put("pettype", pet.getPettype());
            // 把每个数据当作一对象添加到数组里
            jsonarray.put(jsonObj);//向json数组里面添加pet对象

            Pet pet2 = new Pet(100, "name1", "type1");
            JSONObject jsonObj2 = new JSONObject();//pet对象，json形式
            jsonObj2.put("petid", pet2.getPetid());//向pet对象里面添加值
            jsonObj2.put("petname", pet2.getPetname());
            jsonObj2.put("pettype", pet2.getPettype());
            jsonarray.put(jsonObj2);//向json数组里面添加pet对象


            object.put("pet", jsonarray);//向总对象里面添加包含pet的数组
            jsonresult = object.toString();//生成返回字符串
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LogI("生成的json串为:"+jsonresult);
        return jsonresult;
    }
}
