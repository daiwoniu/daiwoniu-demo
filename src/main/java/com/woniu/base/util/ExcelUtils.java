package com.woniu.base.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Excel帮助类
 * Created by liguoxiang on 15-9-7.
 */
public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    private static final int MAXROW = 65500;
    private static final int MAXCELL = 250;


    /**
     * 读取指定单元格内容
     * @param readFilePath
     * @param sheetIndex
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static String readExcel(String readFilePath, int sheetIndex,
                                    int rowIndex, int cellIndex){
        Workbook workbook  = null;
        try{
            File file = new File(readFilePath);
            if(!file.exists()){
                return null;
            }
//            FileInputStream fs=new FileInputStream(readFilePath);

            workbook  = WorkbookFactory.create(file); // new XSSFWorkbook(fs);
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if(sheet == null){
                return null;
            }
            int lastRowIndex = sheet.getLastRowNum();
            if(rowIndex < 0 || rowIndex > lastRowIndex || cellIndex < 0 || cellIndex > MAXCELL){
                return null;
            }
            for (int i = rowIndex; i < lastRowIndex; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    return null;
                }
                Cell cell = row.getCell(cellIndex);
                if(cell == null){
                    return null;
                }
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String value = cell.getStringCellValue();

                Cell cell2 = row.getCell(4);
                if(cell2 == null){
                    cell2 = row.createCell(4);
                }
                cell2.setCellType(Cell.CELL_TYPE_STRING);
                cell2.setCellValue(value + "--------");
            }

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return null;
            }
            Cell cell = row.getCell(cellIndex);
            if(cell == null){
                return null;
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String value = cell.getStringCellValue();
            workbook.write(new FileOutputStream(readFilePath));
            workbook.close();
            return value;
        }catch (Exception ex){
            System.out.println(ex);
            logger.error("读取Excel[{}]文件，sheet页[{}]，rowIndex[{}],cellIndex[{}]时异常:", readFilePath, sheetIndex, rowIndex, cellIndex, ex);
        }finally {
            try{
                if(workbook != null){
                    workbook.close();
                }
            }catch (Exception ex){
                System.out.println(ex);
                logger.error("关闭读取Excel[{}]文件，sheet页[{}]，rowIndex[{}],cellIndex[{}]时异常:", readFilePath, sheetIndex, rowIndex, cellIndex, ex);
            }
        }
        return null;
    }

    /**
     * 读取excle内容，读取指定sheet页中第一列的内容
     * @param readFilePath excel文件路径
     * @param sheetIndex 读取的sheet页索引
     * @param resultList 返回读取结果
     * @return 是否成功
     */
    public static boolean readExcel(String readFilePath, int sheetIndex,
                                    int startRowIndex, int endRowIndex,
                                    int startCellIndex, int endCellIndex,
                                    List<Map<Integer, String>> resultList){
        Workbook workbook  = null;
        try{
            File file = new File(readFilePath);
            if(!file.exists()){
                return false;
            }
            if(resultList == null){
                resultList = new ArrayList<>();
            }
            workbook  = new XSSFWorkbook(file); //WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if(sheet == null){
                return false;
            }
            int lastRowIndex = sheet.getLastRowNum();

            sheetIndex = sheetIndex < 0 ? 0 : sheetIndex;
            startRowIndex = startRowIndex < 0 ? 0 : startRowIndex;
            startCellIndex = startCellIndex < 0 ? 0 : startCellIndex;
            endRowIndex = endRowIndex < 0 ? lastRowIndex : endRowIndex;
            endCellIndex = endCellIndex < 0 ? MAXCELL : endCellIndex;

            if(endRowIndex >= 0){
                int rowindex = startRowIndex;
                for (; rowindex <= endRowIndex; rowindex++) {
                    Row row = sheet.getRow(rowindex);
                    if (row == null) {
                        continue;
                    }
                    int cellindex = startCellIndex;
                    Map<Integer, String> data = new HashMap<>();
                    for (; cellindex <= endCellIndex; cellindex++) {
                        String value = null;
                        Cell cell = row.getCell(cellindex);
                        if(cell != null){
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            value = cell.getStringCellValue();
                        }
                        data.put(cellindex, value);
                    }
                    resultList.add(data);
                }
            }
            workbook.close();
            return true;
        }catch (Exception ex){
            logger.error("读取Excel[{}]文件，sheet页[[]]时异常:", readFilePath, sheetIndex, ex);
            return false;
        }finally {
            try{
                if(workbook != null){
                    workbook.close();
                }
            }catch (Exception ex){
                logger.error("关闭读取Excel[{}]文件，sheet页[[]]时异常:", readFilePath, sheetIndex, ex);
            }
        }
    }

    /**
     * 读取excle内容，读取指定sheet页中第一列的内容
     * @param readFilePath excel文件路径
     * @param sheetIndex 读取的sheet页索引
     * @param resultList 返回读取结果
     * @return 是否成功
     */
    public static boolean readExcel(String readFilePath, int sheetIndex, List<String> resultList){
        Workbook workbook  = null;
        try{
            File file = new File(readFilePath);
            if(!file.exists()){
                return false;
            }
            sheetIndex = sheetIndex < 0 ? 0 : sheetIndex;
            workbook  = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if(sheet == null){
                return false;
            }
            if(resultList == null){
                resultList = new ArrayList<>();
            }
            int lastRowIndex = sheet.getLastRowNum();
            if(lastRowIndex > 0){
                for (int r = 1; r <= lastRowIndex; r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    Cell cell = row.getCell(0);
                    if(cell == null){
                        continue;
                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    if(!Utils.isBlankEmpty(value)){
                        resultList.add(value);
                    }
                }
            }
            workbook.close();
            return true;
        }catch (Exception ex){
            logger.error("读取Excel[{}]文件，sheet页[[]]时异常:", readFilePath, sheetIndex, ex);
            return false;
        }finally {
            try{
                if(workbook != null){
                    workbook.close();
                }
            }catch (Exception ex){
                logger.error("关闭读取Excel[{}]文件，sheet页[[]]时异常:", readFilePath, sheetIndex, ex);
            }
        }
    }


    /**
     * 写入指定单元格内容
     * @param writeFilePath
     * @param sheetIndex
     * @param rowIndex
     * @param cellIndex
     * @param value
     * @return
     */
    public static boolean writeExcel(String writeFilePath, int sheetIndex, int rowIndex, int cellIndex, String value){
        OutputStream out = null;
        SXSSFWorkbook sxssf = null;
        try{
            XSSFWorkbook xssfWorkbook = null;
            File file = new File(writeFilePath);
            if(!file.exists()){
                logger.error("写入已存在的excel文件[{}]时失败，文件不存在。", writeFilePath);
                return false;
            }
            if(rowIndex < 0 || cellIndex < 0 || cellIndex > MAXCELL){
                return false;
            }
            FileInputStream fs=new FileInputStream(writeFilePath);
            xssfWorkbook = new XSSFWorkbook(fs);
            sxssf = new SXSSFWorkbook(xssfWorkbook, 100);
            Sheet sheet = sxssf.getSheetAt(sheetIndex);
            if(sheet == null){
                logger.error("写入已存在的excel文件[{}]时失败，Sheet页[{}]不存在。", writeFilePath, sheetIndex);
                return false;
            }
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            Cell cell = row.getCell(cellIndex);
            if(cell == null){
                cell = row.createCell(cellIndex);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value);

            out = new FileOutputStream(writeFilePath);
            sxssf.write(out);
            return true;
        }catch (Exception ex){
            logger.error("写入已存在的excel文件[{}]，sheet页[{}]，rowIndex[{}],cellIndex[{}],内容[{}]时异常:", writeFilePath, sheetIndex, rowIndex, cellIndex, value, ex);
            System.out.println(ex);
        }finally {
            try{
                if(sxssf != null){
                    sxssf.close();
                }
            }catch (Exception ex){
                System.out.println(ex);
                logger.error("关闭写入已存在的excel文件[{}]，sheet页[{}]，rowIndex[{}],cellIndex[{}],内容[{}]时异常:", writeFilePath, sheetIndex, rowIndex, cellIndex, value, ex);
            }
        }
        return false;
    }

    /**
     * 写入已存在的excel文件，从指定行的索引开始写入
     * @param writeFilePath excel文件路径
     * @param writeSheetIndex 写入sheet的索引, 小于0，新建sheet开始写入，
     * @param sheetName 如果创建sheet，sheet的名字，
     * @param writeRowIndex 开始写入行的索引, 如果开始行之后有数据或小于0，从最后一个数据行之后开始写入，
     * @param excelData 写入数据，从第一列开始，顺序写入
     * @param isCreateFile 如果文件不存在是否创建
     * @return 是否成功
     */
    public static int writeExcel(String writeFilePath, int writeSheetIndex, String sheetName, int writeRowIndex, List<List<String>> excelData, boolean isCreateFile){
        if(excelData == null || excelData.isEmpty()){
            return -1;
        }
        OutputStream out = null;
        SXSSFWorkbook sxssf = null;
        try{
            XSSFWorkbook xssfWorkbook = null;
            File file = new File(writeFilePath);
            if(!file.exists()){
                if(isCreateFile){
                    File file2 = new File(file.getParent());
                    if(!file2.exists()){
                        file2.mkdirs();
                    }
                    sxssf = new SXSSFWorkbook(100);
                }else{
                    logger.error("写入已存在的excel文件[{}]时失败，文件不存在。", writeFilePath);
                    return -1;
                }
            }else {
                FileInputStream fs=new FileInputStream(writeFilePath);
                xssfWorkbook = new XSSFWorkbook(fs);
                sxssf = new SXSSFWorkbook(xssfWorkbook, 100);
            }

            Sheet sheet = null;
            int lastRowNum = 0;
            if(writeSheetIndex < 0){
                sheet = sxssf.createSheet(sheetName);
                if(sheet == null){
                    logger.error("写入已存在的excel文件[{}]时失败，创建Sheet页失败。", writeFilePath, writeSheetIndex);
                }else{
                    sheet.setDefaultColumnWidth(40);
                    sheet.setDefaultRowHeight((short)(20*20));
                }
                writeRowIndex = writeRowIndex < 0 ? 0 : writeRowIndex;
            }else{
                sheet = sxssf.getSheetAt(writeSheetIndex);
                if(sheet == null){
                    logger.error("写入已存在的excel文件[{}]时失败，Sheet页[{}]不存在。", writeFilePath, writeSheetIndex);
                }
                if(xssfWorkbook != null && writeRowIndex < 0){
                    Sheet stmp = xssfWorkbook.getSheetAt(writeSheetIndex);
                    if(stmp != null){
                        lastRowNum = stmp.getLastRowNum();
                        writeRowIndex = writeRowIndex < lastRowNum ? lastRowNum : writeRowIndex;
                    }
                }
            }
            if(sheet == null){
                return -1;
            }
            CellStyle cellStyle = getDataStyle(sxssf, null);
            Iterator<List<String>> rowDatas = excelData.iterator();
            if(rowDatas.hasNext()){
                do {
                    writeRowIndex++;
                    List<String> celDatas = rowDatas.next();
                    writeRow(sheet, writeRowIndex, celDatas.iterator(), cellStyle);
                }while (rowDatas.hasNext());
            }
            out = new FileOutputStream(writeFilePath);
            sxssf.write(out);
            return writeRowIndex;
        }catch (Exception ex){
            logger.error("写入已存在的excel文件[{}]时异常:", writeFilePath, ex);
        }finally {
            try{
                if(sxssf != null){
                    sxssf.close();
                }
            }catch (Exception ex){
                logger.error("关闭写入已存在的excel文件[{}]时异常:", writeFilePath, ex);
            }
        }
        return -1;
    }

    /**
     * 写入新的excel文件，从头开始写入
     * @param writeFilePath excel文件路径
     * @param writeSheetName excel的sheet名
     * @param excelHeader excel第一行列头
     * @param excelData excle从第二行开始的数据
     * @return 是否成功
     */
    public static boolean writeExcel(String writeFilePath, String writeSheetName,
                                     LinkedHashMap<String, String> excelHeader, List<Map<String, String>> excelData){
        OutputStream outputStrem = null;
        Workbook workbook = null;
        try{
            Map<String, CellStyle> cellStyleMap = new HashMap<>();

            workbook = new XSSFWorkbook(); // 07
//            workbook = new HSSFWorkbook(); // 03
            if (Utils.isBlankEmpty(writeSheetName)){
                writeSheetName = "sheet1";
            }
            Sheet sheet = workbook.createSheet(writeSheetName);
            sheet.setDefaultColumnWidth(40);
            sheet.setDefaultRowHeight((short)(20*20));
            int rowIndex = 1;

            Set<String> headerName = new HashSet<>();
            if(excelHeader != null && !excelHeader.isEmpty()){
                // 写入列头
                writeRow(workbook, sheet, rowIndex, excelHeader.values().iterator(), getHeaderStyle(workbook, cellStyleMap), cellStyleMap);
                rowIndex++;
                headerName = excelHeader.keySet();
            }

            if(excelData != null && !excelData.isEmpty()){
                CellStyle dataStyle = getDataStyle(workbook, cellStyleMap);
                for (Map<String, String> row : excelData){
                    if(row == null || row.isEmpty()){
                        continue;
                    }
                    Collection<String> cellData = new ArrayList<>();
                    if(headerName.isEmpty()){
                        cellData = row.values();
                    }else{
                        // 按列头顺序写入数据
                        for (String name : headerName){
                            if(row.containsKey(name)){
                                cellData.add(row.get(name));
                            }else{
                                cellData.add("");
                            }
                        }
                    }
                    // 写入一行数据
                    writeRow(workbook, sheet, rowIndex, cellData.iterator(), dataStyle, cellStyleMap);
                    rowIndex++;
                }
            }

            File file = new File(writeFilePath);
            if(file.exists()){
                file.delete();
            }
            outputStrem = new FileOutputStream(file);
            workbook.write(outputStrem);
            return true;
        }catch (Exception ex){
            logger.error("写入Excel[{}]文件时异常:", writeFilePath, ex);
            return false;
        }finally {
            try{
                if(outputStrem != null){
                    outputStrem.close();
                }
                if(workbook != null){
                    workbook.close();
                }
            }catch (Exception ex){
                logger.error("关闭写入Excel[{}]文件时异常:", writeFilePath, ex);
            }
        }
    }

    /**
     * sheet中写入一行数据
     * @param sheet sheet对象
     * @param rowIndex 写入行的索引，如果小于0，不写入
     * @param cellData 写入的数据，如果数据是null或空，不写入
     */
    private static void writeRow(Sheet sheet, int rowIndex, Iterator<String> cellData, CellStyle cellStyle){
        if(sheet==null){
            return;
        }
        if(rowIndex < 0){
            return;
        }
        if(cellData==null || !cellData.hasNext()){
            return;
        }
        Row row = sheet.createRow(rowIndex);
        int c = 0;
        do{
            String cellValue = cellData.next();
            if(Utils.isBlankEmpty(cellValue)){
                cellValue = " ";
            }
            Cell cell = row.createCell(c);
            if(cellStyle != null){
                cell.setCellStyle(cellStyle);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(cellValue);
            c++;
        }while (cellData.hasNext());
    }
    /**
     * sheet中写入一行数据
     * @param sheet sheet对象
     * @param rowIndex 写入行的索引，如果小于0，不写入
     * @param cellData 写入的数据，如果数据是null或空，写入一个空行
     */
    private static void writeRow(Workbook workbook, Sheet sheet, int rowIndex, Iterator<String> cellData, CellStyle cellStyle, Map<String, CellStyle> cellStyleMap){
        if(sheet==null){
            return;
        }
        if(rowIndex < 0){
            return;
        }
        Row row = sheet.createRow(rowIndex);
        if(cellData==null){
            return;
        }
        int c = 0;
        CellStyle dataLeftStyle = getDataLeftStyle(workbook, cellStyleMap);
        while (cellData.hasNext()){
            String cellValue = cellData.next();
            if(Utils.isBlankEmpty(cellValue)){
                cellValue = " ";
            }
            Cell cell = row.createCell(c);
            if(cellStyle != null){
                cell.setCellStyle(cellStyle);
            }
            if(c==0 && rowIndex > 1){
                Integer level = Utils.toInteger(cellValue);
                cellValue = getSonLevel(level);
                cell.setCellStyle(dataLeftStyle);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(cellValue);
            c++;
        }
    }
    private static String getSonLevel(Integer level){
        if(level > 1){
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < level; i++) {
                sb.append("  -  ");
            }
            sb.append(" 孙公司");
            return sb.toString();
        }else if(level == 1){
            return " - 子公司";
        }
        return "根公司";
    }

    private static CellStyle getHeaderStyle(Workbook workbook, Map<String, CellStyle> cellStyleMap){
        if(cellStyleMap.containsKey("headerStyle") && cellStyleMap.get("headerStyle") != null){
            return cellStyleMap.get("headerStyle");
        }
        Font font = workbook.createFont();
        font.setBold(true); // 粗体
        font.setFontHeight((short)(9*20));

        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index); // 背景颜色
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左右居中
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyleMap.put("headerStyle", cellStyle);
        return cellStyle;
    }
    private static CellStyle getDataStyle(Workbook workbook, Map<String, CellStyle> cellStyleMap){
        if(cellStyleMap != null && cellStyleMap.containsKey("dataStyle") && cellStyleMap.get("dataStyle") != null){
            return cellStyleMap.get("dataStyle");
        }
        Font font = workbook.createFont();
//        font.setBold(true); // 粗体
        font.setFontHeight((short)(10*20));

        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setFillBackgroundColor(HSSFColor.GOLD.index); // 背景颜色
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左右居中
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        if(cellStyleMap != null){
            cellStyleMap.put("dataStyle", cellStyle);
        }
        return cellStyle;
    }
    private static CellStyle getDataLeftStyle(Workbook workbook, Map<String, CellStyle> cellStyleMap){
        if(cellStyleMap.containsKey("dataLeftStyle") && cellStyleMap.get("dataLeftStyle") != null){
            return cellStyleMap.get("dataLeftStyle");
        }
        Font font = workbook.createFont();
//        font.setBold(true); // 粗体
        font.setFontHeight((short)(10*20));

        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setFillBackgroundColor(HSSFColor.GOLD.index); // 背景颜色
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 左右居中
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyleMap.put("dataLeftStyle", cellStyle);
        return cellStyle;
    }
}
