package com.example.my.labelmanagement.utils.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.SimpleDateFormat;

/**
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 *
 * @author :EchoXBR in  2018/9/21 上午11:43.
 * 功能描述:转换判断类
 */
public class ExcelUtils {


    /**
     * 分多种格式解析单元格的值
     *
     * @param cell
     *         单元格
     *
     * @return 单元格的值
     */
    public static String convertCellToString(Cell cell) {
        //如果为null会抛出异常，应当返回空字符串
        if (cell == null) {
            return "";
        }

        //POI对单元格日期处理很弱，没有针对的类型，日期类型取出来的也是一个double值，所以同样作为数值类型
        //解决日期2006/11/02格式读入后出错的问题，POI读取后变成“02-十一月-2006”格式
        if (cell.toString().contains("-") && checkDate(cell.toString())) {
            String ans = "";
            try {
                ans = new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue());
            } catch (Exception e) {
                ans = cell.toString();
            }
            return ans;
        }

        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }

    /**
     * 判断是否是“02-十一月-2006”格式的日期类型
     */
    private static boolean checkDate(String str) {
        String[] dataArr = str.split("-");
        try {
            if (dataArr.length == 3) {
                int x = Integer.parseInt(dataArr[0]);
                String y = dataArr[1];
                int z = Integer.parseInt(dataArr[2]);
                if (x > 0 && x < 32 && z > 0 && z < 10000 && y.endsWith("月")) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * 获取单元格的内容
     *
     * @param cell 单元格
     *
     * @return 返回单元格内容
     */
    private String getCellContent(XSSFCell cell) {
        StringBuffer buffer = new StringBuffer();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                buffer.append(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔
                buffer.append(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                buffer.append(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                buffer.append(cell.getStringCellValue());
                break;
            default:
                break;
        }
        return buffer.toString();
    }
}
