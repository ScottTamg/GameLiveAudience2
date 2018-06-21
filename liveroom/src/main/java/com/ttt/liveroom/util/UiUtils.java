package com.ttt.liveroom.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.DecimalFormat;
import java.util.List;

public class UiUtils {

    /**
     * 检测手机是否安装了微信客户端
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 数字大于10000转换为1.0W
     *
     * @param num
     * @return
     */
    public static String NumTransform(int num) {
        if (num > 9999) {
            DecimalFormat decimalFormat = new DecimalFormat(".0");
            return decimalFormat.format(num / 10000f) + "W";
        } else {
            return String.valueOf(num);
        }
    }
}
