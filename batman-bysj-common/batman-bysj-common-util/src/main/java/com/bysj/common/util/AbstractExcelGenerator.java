package com.bysj.common.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.AttributedString;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
/**
 * @author victor.qin
 * @date 2018/4/16 17:57
 */
public abstract class AbstractExcelGenerator <BEAN, SUMMARY, RESULT, CONDITION>{
    /**
     * 标题(包含 sheet 名)
     */
    public abstract String title();

    /**
     * 说明文字(可选, 如非 null, 则添加在 title 下)
     * LinkedList 方便进行任意位置添加
     * 另: 如果 condition 继承了 SearchCondition 类 则提供默认的条件的转换
     */
    protected LinkedList<String> descriptions(CONDITION condition) {
        return new LinkedList<>();
    }

    /**
     * 详情单元格数据获取方式
     */
    protected Map<String, Function<BEAN, Object>> detailGetter(List<String> fields, CONDITION condition) {
        return Collections.emptyMap();
    }

    /**
     * summary 单元格数据获取方式
     */
    protected abstract Map<String, Function<SUMMARY, Object>> summaryGetter(List<String> fields, CONDITION condition);

    /**
     * 数据
     */
    protected abstract RESULT data(List<String> fields, CONDITION condition);

    /**
     * 详情
     */
    protected abstract List<BEAN> detail(RESULT data);

    /**
     * 合计
     */
    protected abstract SUMMARY summary(RESULT data);

    /**
     * 标题样式 根据需要覆盖
     */
    protected CellStyle titleStyle(SXSSFWorkbook workbook) {
        // 标题字体
        Font titleFont = workbook.createFont();
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 15);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setFont(titleFont);
        return titleStyle;
    }

    /**
     * 表头样式 根据需要覆盖
     */
    protected CellStyle headStyle(SXSSFWorkbook workbook) {
        // 表头字体
        Font tableHeadFont = workbook.createFont();
        tableHeadFont.setColor(HSSFColor.BLACK.index);
        tableHeadFont.setBold(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setFont(tableHeadFont);
        return titleStyle;
    }

    /**
     * 详情样式 根据需要覆盖
     */
    protected CellStyle descriptionStyle(SXSSFWorkbook workbook) {
        return null;
    }

    /**
     * 内容的数据格式
     */
    protected Map<String, CellStyle> detailStyle(List<String> fields, CONDITION condition, Workbook workbook) {
        return new HashMap<>();
    }

    /**
     * 合计样式 根据需要覆盖
     */
    protected Map<String, CellStyle> summaryStyles(List<String> fields, CONDITION condition, Workbook workbook) {
        return detailStyle(fields, condition, workbook);
    }

    /**
     * 排好序的列 (定义的列 key 和对应的表头名, 列 key 默认将作为详情的属性名获取对应单元格内容)
     *
     * @param fields
     * @return
     */
    protected abstract LinkedHashMap<String, String> sortedFieldDict(List<String> fields);

    public final byte[] generateExcelFile(List<String> fields, CONDITION condition) throws IOException {

        Map<String, Function<BEAN, Object>> detailGetter = detailGetter(fields, condition);

        // 生成 excel
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        workbook.setCompressTempFiles(true);
        SXSSFSheet sheet = workbook.createSheet(title());

        // 记录列宽
        Map<String, Integer> columnWidths = new ConcurrentHashMap<>();

        // 默认数字格式
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        DataFormat numberFormat = workbook.createDataFormat();
        numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00")); // 两位小数

        // 默认 description 格式
        CellStyle descriptionStyle = workbook.createCellStyle();
        descriptionStyle.setAlignment(HorizontalAlignment.LEFT);
        descriptionStyle.setVerticalAlignment(VerticalAlignment.TOP);
        descriptionStyle.setWrapText(true);

        // 表头字典(提前获取总列数)
        LinkedHashMap<String, String> sortedFieldDict = sortedFieldDict(fields);

        int index = 0;
        //生成标题
        if (!StringUtils.isEmpty(title())) {
            SXSSFRow titleRow = sheet.createRow(index);
            // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(index, index, 0, sortedFieldDict.size() - 1));
            SXSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(titleStyle(workbook));
            titleCell.setCellValue(title());
            index++;
        }

        // 生成描述
        List<String> descriptions = descriptions(condition);
        List<Integer> descriptionLines = new ArrayList<>();
        if (!CollectionUtils.isEmpty(descriptions)) {
            CellStyle style = Optional.ofNullable(descriptionStyle(workbook))
                    .orElse(descriptionStyle);
            for (String description : descriptions) {
                SXSSFRow descriptionRow = sheet.createRow(index);
                sheet.addMergedRegion(new CellRangeAddress(
                        index, index, 0, sortedFieldDict.size() - 1));
                SXSSFCell descriptionCell = descriptionRow.createCell(0);

                descriptionCell.setCellStyle(style);
                descriptionCell.setCellValue(description);
                descriptionRow.setRowStyle(style);

                descriptionLines.add(index);
                index++;
            }

            // 描述和内容间空一行
            sheet.addMergedRegion(new CellRangeAddress(
                    index, index, 0, sortedFieldDict.size() - 1));
            index++;

        }

        // 生成表头行
        if (!MapUtils.isEmpty(sortedFieldDict)) {
            SXSSFRow tableHeadRow = sheet.createRow(index);
            int column = 0;
            for (Map.Entry<String, String> entry : sortedFieldDict.entrySet()) {
                SXSSFCell headCell = tableHeadRow.createCell((short) column);
                String value = entry.getValue();
                headCell.setCellValue(value);
                headCell.setCellStyle(headStyle(workbook));
                columnWidths.put(entry.getKey(), length(value));
                column++;
            }
            index++;
        }

        // 生成表格内容
        RESULT data = data(fields, condition);
        if (null == sortedFieldDict || sortedFieldDict.isEmpty()) {
            throw new RuntimeException("生成表格列数为 0");
        }
        List<BEAN> details = detail(data);
        if (!CollectionUtils.isEmpty(details) && !MapUtils.isEmpty(sortedFieldDict)) {
            Map<String, CellStyle> detailStyles = detailStyle(fields, condition, workbook);
            // 每行数据
            for (BEAN detail : details) {
                SXSSFRow dataRow = sheet.createRow(index);
                int column = 0;
                // 一行中的单元格
                for (Map.Entry<String, String> entry : sortedFieldDict.entrySet()) {
                    SXSSFCell cell = dataRow.createCell(column);
                    column++;
                    Object value = null;
                    String fieldKey = entry.getKey();
                    Function<BEAN, Object> getter = detailGetter.get(fieldKey);
                    if (null != getter) {
                        value = getter.apply(detail);
                    } else {
                        // 新增对反射的支持
                        value = getFieldValueByReflection(detail, fieldKey);
                    }
                    if (value == null) {continue;}
                    setCell(cell, value, fieldKey, columnWidths, detailStyles, numberStyle);
                }
                index++;
            }
        }

        // 设置合计
        SUMMARY summary = summary(data);
        if (null != summary) {

            // 详情和合计间空一行
            sheet.addMergedRegion(new CellRangeAddress(
                    index, index, 0, sortedFieldDict.size() - 1));
            index++;

            SXSSFRow dataRow = sheet.createRow(index);
            int column = 0;
            Map<String, CellStyle> summaryStyles = summaryStyles(fields, condition, workbook);
            for (Map.Entry<String, String> entry : sortedFieldDict.entrySet()) {
                SXSSFCell cell = dataRow.createCell(column);
                Object value;
                String fieldKey = entry.getKey();
                Function<SUMMARY, Object> getter = summaryGetter(fields, condition).get(fieldKey);
                // 第一格是 "合计" 字样
                if (null != getter) {
                    value = getter.apply(summary);
                } else {
                    // 新增对反射的支持
                    value = getFieldValueByReflection(summary, fieldKey);
                }
                if (null == value && column == 0) {value = "合计";}
                if (value != null) {setCell(cell, value, fieldKey, columnWidths, summaryStyles, numberStyle);}
                column++;
            }
        }

        // 列宽调整
        int column = 0;
        for (Map.Entry<String, String> entry : sortedFieldDict.entrySet()) {
            sheet.setColumnWidth(column, Optional.ofNullable(columnWidths.get(entry.getKey()))
                    .map(value -> value > 200 ? 200 * 1.2f * 256 : value * 1.2f * 256)
                    .map(Math::round)
                    .orElse(15 * 256));
            column++;
        }

        // descriptions 行高调整
        for (Integer line : descriptionLines) {
            adjustRowHeightForRowWithMergedCells(sheet.getRow(line));
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream(/*details.size() * 64*/);
        workbook.write(os);
        return os.toByteArray();
    }

    private Object getFieldValueByReflection(Object detail, String fieldKey) {
        Object returnValue = null;
        if (detail instanceof Map) {
            returnValue = ((Map) detail).get(fieldKey);
        } else {
            try {
                returnValue = PropertyUtils.getProperty(detail, fieldKey);
            } catch (IllegalAccessException | InvocationTargetException |
                    NoSuchMethodException ignored) {
            }
        }
        return returnValue;
    }

    private void setCell(SXSSFCell cell, Object value, String fieldKey, Map<String, Integer> columnWidths,
                         Map<String, CellStyle> detailStyles, CellStyle numberStyle) {
        CellStyle cellStyle = detailStyles.get(fieldKey);
        Integer width = columnWidths.get(fieldKey);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
            // 数字是有默认格式的
            cell.setCellStyle(Optional.ofNullable(cellStyle)
                    .orElse(numberStyle));
        } else if ("-".equals(value) || "∞".equals(value)) {
            cell.setCellValue((String) value);
            cell.setCellStyle(Optional.ofNullable(cellStyle)
                    .orElse(numberStyle));
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
            cell.setCellStyle(cellStyle);
        } else {
            cell.setCellValue(value.toString());
            cell.setCellStyle(cellStyle);
        }
        int length = length(value.toString());
        // 记录最大宽度
        if (null == width || length > width) {columnWidths.put(fieldKey, length);}
    }

    private static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private static Boolean isCellMerged(Cell cell) {
        Sheet sheet = cell.getSheet();
        for (CellRangeAddress mergedRegionRange : sheet.getMergedRegions()) {
            Integer cellColumn = cell.getColumnIndex();
            Integer cellRow = cell.getRowIndex();
            if (mergedRegionRange.containsColumn(cellColumn) && mergedRegionRange.containsRow(cellRow)) {
                return true;
            }
        }
        return false;
    }

    private static List<List<RowAndColumn>> getCellsInRowsInsideRegionRange(Cell cell) {
        Sheet sheet = cell.getSheet();
        List<List<RowAndColumn>> mergedRowList = new ArrayList<>();
        List<RowAndColumn> mergedCellsList = new ArrayList<>();

        //Nejdříve musíme zjistit sloučenou sekci dané buňky
        for (CellRangeAddress mergedRegionRange : sheet.getMergedRegions()) {
            Integer cellColumn = cell.getColumnIndex();
            Integer cellRow = cell.getRowIndex();
            if (mergedRegionRange.containsColumn(cellColumn) && mergedRegionRange.containsRow(cellRow)) {

                //Protože CellRangeAddress nemá moc metod, musíme si pomoci sami a získat z ní buňky a řádky
                // 只查询对应的合并单元格
                for (int index = mergedRegionRange.getFirstRow(); index <= mergedRegionRange.getLastRow(); index++) {
                    Row row = sheet.getRow(index);
                    for (Cell iteratedCell : row) {
                        Integer iteratedCellColumn = iteratedCell.getColumnIndex();
                        Integer iteratedCellRow = iteratedCell.getRowIndex();
                        if (mergedRegionRange.containsColumn(iteratedCellColumn) && mergedRegionRange.containsRow(iteratedCellRow)) {
                            int column = mergedRegionRange.getFirstColumn();
                            while (column < mergedRegionRange.getLastColumn()) {
                                //Rozdělování jednotlivých řádků
                                mergedCellsList.add(new RowAndColumn(row.getRowNum(), column));
                                column++;
                            }
                        }
                    }
                    //Vložíme List buněk daného řádku do Listu řádků
                    if (!mergedCellsList.isEmpty()) {
                        mergedRowList.add(mergedCellsList);
                    }

                    //A vyresetujeme list buněk (začneme tak nanovo novým řádkem)
                    mergedCellsList = new ArrayList<>();
                }
                //Vrátíme výsledný List řádků, obsahující Listy buněk ve sloučené sekci.
                if (!mergedRowList.isEmpty()) {
                    return mergedRowList;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    // via. https://stackoverflow.com/questions/37744922/apache-poi-and-auto-row-height-with-merged-cells?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    // by Bc. Jan Bokšay, Val Blant

    /**
     * 自动调整合并单元格后 自动换行的的行高
     *
     * @see <a href="https://stackoverflow.com/questions/37744922/apache-poi-and-auto-row-height-with-merged-cells?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa">
     */
    private static void adjustRowHeightForRowWithMergedCells(Row row) {
        Sheet sheet = row.getSheet();
        Cell longestTextCell = null;

        // 内容最长的单元格
        //Potřebujeme získat buňku s nejdelším textem
        for (Cell iteratedCell : row) {
            String iteratedTextString = iteratedCell.getStringCellValue();

            if (longestTextCell != null && StringUtils.isNotBlank(longestTextCell.getStringCellValue())) {
                // 修正对中文的解析
                if (length(iteratedTextString) > length(longestTextCell.getStringCellValue())) {
                    longestTextCell = iteratedCell;
                }
            } else {
                longestTextCell = iteratedCell;
            }

        }

        //Z textově nejobsáhlejší buňky potřebujeme dostat údaje
        String longestText;

        if (longestTextCell != null && StringUtils.isNotBlank(longestTextCell.getStringCellValue())) {
            longestText = longestTextCell.getStringCellValue();

            //Protože textově nejobsáhlejší buňka nemusí nutně být sloučeného typu, je zapotřebí to všude ošetřit
            Boolean isLongestTextCellMerged = isCellMerged(longestTextCell);
            Float longestCellWidthInPixels = 0f;
            Float longestMergedCellWidthInPixels = 0f;

            //Získat šířku nesloučené nejobsáhlejší buňky je jednoduché
            if (!isLongestTextCellMerged) {
                Integer longestCellColumnIndex = longestTextCell.getColumnIndex();
                longestCellWidthInPixels = sheet.getColumnWidthInPixels(longestCellColumnIndex);

            } else {

                //Musíme přijít na šířku sloučené buňky namísto buňky uvnitř sloučené buňky
                List<List<RowAndColumn>> cellsInMergedRegion = getCellsInRowsInsideRegionRange(longestTextCell);
                longestMergedCellWidthInPixels = 0f;

                //Projdeme řádky
                for (List<RowAndColumn> iteratedCell2List : cellsInMergedRegion) {
                    Float iteratedMergedCell2WidthInPixels = 0f;

                    //Projdeme jednotlivé buňky ve sloučené buňce na řádku a sečteme jejich šířky
                    for (RowAndColumn iteratedCell2 : iteratedCell2List) {
                        Float iteratedCell2ColumnWidthInPixels = sheet.getColumnWidthInPixels(iteratedCell2.getColumn());

                        iteratedMergedCell2WidthInPixels = iteratedMergedCell2WidthInPixels + iteratedCell2ColumnWidthInPixels;
                    }

                    //Získáme šířku nejširší sloučené buňky na řádku
                    if (iteratedMergedCell2WidthInPixels > longestMergedCellWidthInPixels) {
                        longestMergedCellWidthInPixels = iteratedMergedCell2WidthInPixels;
                    }

                    //Resetujeme sčítání
                    iteratedMergedCell2WidthInPixels = 0f;
                }
            }

            //Uložíme si nejširší buňku dle toho, zda je sloučená či nikoliv
            Float longestWidthInPixels;
            if (isLongestTextCellMerged) {
                longestWidthInPixels = longestMergedCellWidthInPixels;
            } else {
                longestWidthInPixels = longestCellWidthInPixels;
            }

            //Potřebujeme font
            Workbook wb = sheet.getWorkbook();
            Short fontIndex = longestTextCell.getCellStyle().getFontIndex();
            Font excelFont = wb.getFontAt(fontIndex);

            //Potřebujeme i jeho styl
            Integer excelFontStyle = java.awt.Font.PLAIN;
            if (excelFont.getBold()) {excelFontStyle = java.awt.Font.BOLD;}
            if (excelFont.getItalic()) {excelFontStyle = java.awt.Font.ITALIC;}

            //Potřebujeme získat skutečný font i s velikostí
            java.awt.Font currentFont = new java.awt.Font(excelFont.getFontName(), excelFontStyle, excelFont.getFontHeightInPoints());

            //Získáme řetězec s vlastností
            AttributedString attributedString = new AttributedString(longestText);
            attributedString.addAttribute(TextAttribute.FONT, currentFont);

            //Použijeme LineBreakMeasurer k zjištění kolik řádků bude text potřebovat
            FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);
            LineBreakMeasurer measurer = new LineBreakMeasurer(attributedString.getIterator(), fontRenderContext);

            Integer nextPosition = 0;
            Integer lineCount = 0;

            while (measurer.getPosition() < longestText.length()) {
                nextPosition = measurer.nextOffset(longestWidthInPixels);

                //Také musíme ošetřit případ manuálně zadaných LineBreaků pro všechny možné techtle mechtle :-S
                String textLine = StringUtils.substring(longestText, measurer.getPosition(), nextPosition);
                Boolean containsNewLine = StringUtils.containsIgnoreCase(textLine, "\r") || StringUtils.containsIgnoreCase(textLine, "\\r") || StringUtils.containsIgnoreCase(textLine, "\n") || StringUtils.containsIgnoreCase(textLine, "\\n");

                if (containsNewLine) {

                    if (StringUtils.containsIgnoreCase(textLine, "\r\n") || StringUtils.containsIgnoreCase(textLine, "\\r\\n")) {
                        lineCount = lineCount + StringUtils.countMatches(textLine, "\n");
                    } else {

                        if (StringUtils.containsIgnoreCase(textLine, "\r") || StringUtils.containsIgnoreCase(textLine, "\\r")) {
                            lineCount = lineCount + StringUtils.countMatches(textLine, "\r");
                        }
                        if (StringUtils.containsIgnoreCase(textLine, "\n") || StringUtils.containsIgnoreCase(textLine, "\\n")) {
                            lineCount = lineCount + StringUtils.countMatches(textLine, "\n");
                        }

                    }

                    lineCount = lineCount + StringUtils.countMatches(textLine, "\\r?\\n");
                }

                lineCount++;
                measurer.setPosition(nextPosition);
            }

            //Máme počet řádků, zbývá konečný dopočet výšky řádku a jeho použití
            if (lineCount > 1) {

                Float fontHeight = currentFont.getLineMetrics(longestText, fontRenderContext).getHeight();

                //Pro jistotu přidáme jeden řádek navíc, člověk nikdy neví...
                lineCount = lineCount + 1;

                //Potřebujeme získat poslední řádek
                Row lastRow = null;

                if (isCellMerged(longestTextCell)) {
                    List<List<RowAndColumn>> mergedCellsInRows = getCellsInRowsInsideRegionRange(longestTextCell);
                    Integer lastRowInMergedSectionIndex = mergedCellsInRows.size() - 1;
                    List<RowAndColumn> lastRowInMergedSection = mergedCellsInRows.get(lastRowInMergedSectionIndex);
                    lastRow = sheet.getRow(lastRowInMergedSection.get(0).getRow());
                } else {
                    lastRow = longestTextCell.getRow();
                }

                //Je potřeba ošetřit velikosti, pokud má sloučená buňka vícero řádků
                Float cellsMergedAboveHeight = 0f;
                if (isCellMerged(longestTextCell)) {
                    if (getCellsInRowsInsideRegionRange(longestTextCell).size() > 1) {
                        List<List<RowAndColumn>> mergedCellsInRows = getCellsInRowsInsideRegionRange(longestTextCell);
                        for (List<RowAndColumn> rowsWithCells : mergedCellsInRows) {
                            if (!lastRow.equals(rowsWithCells.get(0).getRow())) {
                                cellsMergedAboveHeight = cellsMergedAboveHeight + sheet.getRow(rowsWithCells.get(0).getRow()).getHeight();
                            }
                        }
                    }
                }
                //Vzorec je ((Velikost fontu krát počet řádků plus (počet řádků krát volný prostor mezi řádky)) krát přepočet Excelu) mínus výška sloučených buněk nad posledním řádkem.
                Short finalRowHeight = (short) (((fontHeight * lineCount + (lineCount * 15)) * 10) - cellsMergedAboveHeight);

                //A výsledek nastavíme na poslední řádek, protože jinak to przní sloupce vlevo a vpravo od vyšších řádků
                lastRow.setHeight(finalRowHeight);

            }
        }
    }

    private static class RowAndColumn {
        private Integer row;
        private Integer column;

        public RowAndColumn(Integer row, Integer column) {
            this.row = row;
            this.column = column;
        }

        public Integer getRow() {
            return row;
        }

        public void setRow(Integer row) {
            this.row = row;
        }

        public Integer getColumn() {
            return column;
        }

        public void setColumn(Integer column) {
            this.column = column;
        }
    }
}
