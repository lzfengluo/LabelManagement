package com.example.my.labelmanagement.utils.xls;




import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class JxlExcelHelper<T> extends ExcelHelper<T> {


    private static JxlExcelHelper instance = null; // 单例对象
    ReflectUtil reflectUtil = new ReflectUtil<T>();
    private File file; // 操作文件


    /**
     * 私有化构造方法
     *
     * @param file 文件对象
     */
    private JxlExcelHelper(File file) {

        super();
        this.file = file;
    }

    /**
     * 获取单例对象并进行初始化
     *
     * @param file 文件对象
     *
     * @return 返回初始化后的单例对象
     */
    public static JxlExcelHelper getInstance(File file) {
        System.out.print("====JxlExcelHelper====");
        if (instance == null) {
            // 当单例对象为null时进入同步代码块
            synchronized (JxlExcelHelper.class) {
                // 再次判断单例对象是否为null,防止多线程访问时多次生成对象
                if (instance == null) {
                    instance = new JxlExcelHelper(file);
                }
            }
        } else {
            // 如果操作的文件对象不同,则重置文件对象
            if (!file.equals(instance.getFile())) {
                instance.setFile(file);
            }
        }
        return instance;
    }

    /**
     * 获取单例对象并进行初始化
     *
     * @param filePath 文件路径
     *
     * @return 返回初始化后的单例对象
     */
    public static JxlExcelHelper getInstance(String filePath) {
        return getInstance(new File(filePath));
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public List readExcel(Class<T> clazz, String[] fieldNames,
                          int sheetNo, boolean hasTitle) throws Exception {
        T target = clazz.newInstance();
        List dataModels = new ArrayList();
        // 获取excel工作簿
        Workbook workbook = Workbook.getWorkbook(file);
        try {
            Sheet sheet = workbook.getSheet(sheetNo);
            int start = hasTitle ? 1 : 0; // 如果有标题则从第二行开始
            for (int i = start; i < sheet.getRows(); i++) {
                // 生成实例并通过反射调用setter方法
                for (int j = 0; j < fieldNames.length; j++) {
                    String fieldName = fieldNames[j];
                    if (fieldName == null || ExcelHelper.UID.equals(fieldName)) {
                        continue; // 过滤serialVersionUID属性
                    }
                    // 获取excel单元格的内容
                    Cell cell = (Cell) sheet.getCell(j, i);
                    if (cell == null) {
                        continue;
                    }
                    String content = cell.getContents();
                    T tagret = clazz.newInstance();
                    // 如果属性是日期类型则将内容转换成日期对象
                    if (isDateType(clazz, fieldName)) {
                        // 如果属性是日期类型则将内容转换成日期对象
                        reflectUtil.invokeSetter(target, fieldName,
                                DateUtils.parse(content));
                    } else {
                        Field field = clazz.getDeclaredField(fieldName);
                        reflectUtil.invokeSetter(target, fieldName,
                                parseValueWithType(content, field.getType()));
                    }
                }
                dataModels.add(target);
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return dataModels;
    }


    @Override
    public void writeExcel(Class clazz, List dataModels,
                           String[] fieldNames, String[] titles) throws Exception {
        WritableWorkbook workbook = null;
        try {
            // 检测文件是否存在,如果存在则修改文件,否则创建文件
            if (file.exists()) {
                Workbook book = Workbook.getWorkbook(file);
                workbook = Workbook.createWorkbook(file, book);
            } else {
                workbook = Workbook.createWorkbook(file);
            }
            // 根据当前工作表数量创建相应编号的工作表
            int sheetNo = workbook.getNumberOfSheets() + 1;
            String sheetName = DateUtils.format(new Date(), "yyyyMMddHHmmssSS");
            WritableSheet sheet = workbook.createSheet(sheetName, sheetNo);
            // 添加表格标题
            for (int i = 0; i < titles.length; i++) {
                // 设置字体加粗
                WritableFont font = new WritableFont(WritableFont.ARIAL, 10,
                        WritableFont.BOLD);
                WritableCellFormat format = new WritableCellFormat(font);
                // 设置自动换行
                format.setWrap(true);
                Label label = new Label(i, 0, titles[i], format);
                sheet.addCell(label);
                // 设置单元格宽度
                sheet.setColumnView(i, titles[i].length() + 10);
            }
            // 添加表格内容
            for (int i = 0; i < dataModels.size(); i++) {
                // 遍历属性列表
                for (int j = 0; j < fieldNames.length; j++) {
                    T target = (T) dataModels.get(i);
                    // 通过反射获取属性的值域
                    String fieldName = fieldNames[j];
                    if (fieldName == null || ExcelHelper.UID.equals(fieldName)) {
                        continue; // 过滤serialVersionUID属性
                    }
                    ReflectUtil reflectUtil = new ReflectUtil<T>();
                    Object result = reflectUtil.invokeGetter(target, fieldName);
                    Label label = new Label(j, i + 1,
                            StringUtil.toString(result));
                    // 如果是日期类型则进行格式化处理
                    if (isDateType(clazz, fieldName)) {
                        label.setString(DateUtils.format((Date) result));
                    }
                    sheet.addCell(label);
                }
            }
        } finally {
            if (workbook != null) {
                workbook.write();
                workbook.close();
            }
        }
    }
}
