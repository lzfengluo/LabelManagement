package com.example.my.labelmanagement.utils;



import com.example.my.labelmanagement.been.TagInfoBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 张智超
 * @date 2019/3/20
 */
public class CheckUtils {

    /**
     * 判断IP4地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.){2}((\\d|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.)([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))$";
            // 判断ip地址是否与正则表达式匹配
            // 返回判断信息
            return text.matches(regex);
        }
        return false;
    }

    /**
     * 判断IP6地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ip6Check(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
//            String regex = "([a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}|[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){0,7}::[a-fA-F0-9]{0,4}(:[a-fA-F0-9]{1,4}){0,7})";
            String regex = "^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$";
            // 判断ip地址是否与正则表达式匹配
            // 返回判断信息
            return text.matches(regex);
        }
        return false;
    }
    private static Pattern pattern = Pattern.compile("^[A-Za-z0-9_-][A-Za-z0-9_-|\\s-]*$");
    /**
     * 判断是否符合格式
     */
    public static boolean stringCheck(String s) {
        if (s.isEmpty()) {
            return true;
        }
        Matcher matcher = pattern.matcher(s);
        //正则改为2个字符|null+16位数字（8位日期+8位序列）
        return matcher.matches();

    }

    public static String checkTag(List<TagInfoBean> tagInfoBeanList) {
        for (TagInfoBean tagInfoBean : tagInfoBeanList) {
            String sn = tagInfoBean.getSN();
            if (!stringCheck(sn)) {
                return tagInfoBean.toString() + "\nSN校验失败";
            }
            String model = tagInfoBean.getModel();
            if (!stringCheck(model)) {
                return tagInfoBean.toString() + "\nModel校验失败";
            }
            String type = tagInfoBean.getType();
            if (!("Server".equals(type) || "Storage".equals(type) || "Network".equals(type) || "Other".equals(type))) {
                return tagInfoBean.toString() + "\nType校验失败";
            }
            String vendor = tagInfoBean.getVendor();
            if (!stringCheck(vendor)) {
                return tagInfoBean.toString() + "\nVendor校验失败";
            }
            try {
                double occupiedHeight = Double.parseDouble(tagInfoBean.getOccupiedHeight());
//                occupiedHeight = (double) Math.round(occupiedHeight * 100) / 100;
                if (occupiedHeight < 1 || occupiedHeight > 54) {
                    return tagInfoBean.toString() + "\nOccupied Height(U)校验失败";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return tagInfoBean.toString() + "\nOccupied Height(U)校验失败\n" + e.toString();
            }

            try {
                double lifecycle = Double.parseDouble(tagInfoBean.getLifecycle());
                if (lifecycle < 1 || lifecycle > 20) {
                    return tagInfoBean.toString() + "\nLifecycle(year)校验失败";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return tagInfoBean.toString() + "\nLifecycle(year)校验失败\n" + e.toString();
            }

            try {
                double weight = Double.parseDouble(tagInfoBean.getWeight());
                if (weight < 1 || weight > 5000) {
                    return tagInfoBean.toString() + "\nWeight校验失败";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return tagInfoBean.toString() + "\nWeight校验失败\n" + e.toString();
            }

            try {
                double ratedPower = Double.parseDouble(tagInfoBean.getRatedPower());
                if (ratedPower < 1 || ratedPower > 10000) {
                    return tagInfoBean.toString() + "\nRatedPower校验失败";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return tagInfoBean.toString() + "\nRatedPower校验失败\n" + e.toString();
            }

            String owner = tagInfoBean.getOwner();
            if (!stringCheck(owner)) {
                return tagInfoBean.toString() + "\nOwner校验失败";
            }

            String partNum = tagInfoBean.getPartNum();
            if (!stringCheck(partNum)) {
                return tagInfoBean.toString() + "\nP/N校验失败";
            }
        }

        return "";
    }
}
