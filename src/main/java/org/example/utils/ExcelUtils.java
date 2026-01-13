package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel dosyalarından veri okuma ve yazma işlemleri için utility sınıfı
 */
public class ExcelUtils {
    private static final String TEST_DATA_PATH = "src/main/resources/testdata/TestData.xlsx";
    private static final String TEST_RESULTS_PATH = "src/main/resources/testdata/TestResults.xlsx";

    /**
     * Excel dosyasından test verilerini okur
     * @param sheetName Sheet adı
     * @return Test verileri listesi (her satır bir Map)
     */
    public static List<Map<String, String>> readTestData(String sheetName) {
        List<Map<String, String>> testDataList = new ArrayList<>();
        LoggerUtils.logInfo("Excel dosyasından test verileri okunuyor...");

        java.io.InputStream inputStream = null;
        String usedPath = "";

        // Önce relative path'i dene
        java.io.File file = new java.io.File(TEST_DATA_PATH);
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
                usedPath = TEST_DATA_PATH;
                LoggerUtils.logInfo("Excel dosyası relative path'ten okunuyor: " + TEST_DATA_PATH);
            } catch (IOException e) {
                LoggerUtils.logWarn("Relative path'ten okunamadı: " + e.getMessage());
            }
        }

        // Eğer relative path'te yoksa, resource path'i dene
        if (inputStream == null) {
            inputStream = ExcelUtils.class.getClassLoader()
                    .getResourceAsStream("testdata/TestData.xlsx");
            if (inputStream != null) {
                usedPath = "testdata/TestData.xlsx (resource)";
                LoggerUtils.logInfo("Excel dosyası resource path'ten okunuyor: testdata/TestData.xlsx");
            } else {
                throw new RuntimeException("Excel dosyası bulunamadı. Relative path: " + TEST_DATA_PATH +
                        ", Resource path: testdata/TestData.xlsx");
            }
        }

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            LoggerUtils.logInfo("Excel dosyası açıldı. Sheet sayısı: " + workbook.getNumberOfSheets());

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                // Mevcut sheet'leri listele
                StringBuilder availableSheets = new StringBuilder();
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    if (i > 0) availableSheets.append(", ");
                    availableSheets.append(workbook.getSheetName(i));
                }
                throw new RuntimeException("Sheet bulunamadı: " + sheetName +
                        ". Mevcut sheet'ler: " + availableSheets.toString());
            }

            LoggerUtils.logInfo("Sheet bulundu: " + sheetName + ", Toplam satır sayısı: " + sheet.getLastRowNum());

            // İlk satır header'ları içerir
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Sheet'te header satırı bulunamadı!");
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                String headerValue = getCellValueAsString(cell);
                headers.add(headerValue);
            }
            LoggerUtils.logInfo("Header'lar okundu: " + headers);

            // Diğer satırları oku
            int dataRowCount = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = cell != null ? getCellValueAsString(cell) : "";
                    rowData.put(headers.get(j), value);
                }

                // Boş satırları atla
                boolean isEmpty = true;
                for (String value : rowData.values()) {
                    if (value != null && !value.trim().isEmpty()) {
                        isEmpty = false;
                        break;
                    }
                }

                if (!isEmpty) {
                    testDataList.add(rowData);
                    dataRowCount++;
                }
            }

            LoggerUtils.logInfo("Toplam " + dataRowCount + " satır veri okundu");

            if (testDataList.isEmpty()) {
                throw new RuntimeException("Excel dosyasında veri bulunamadı! Sheet: " + sheetName);
            }

        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası okunamadı: " + usedPath, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LoggerUtils.logError("InputStream kapatılırken hata oluştu: " + e.getMessage());
                }
            }
        }

        return testDataList;
    }

    /**
     * Test sonuçlarını Excel dosyasına yazar
     * @param testName Test adı
     * @param testStatus Test durumu (PASS/FAIL)
     * @param executionTime Çalışma süresi (ms)
     * @param errorMessage Hata mesajı (varsa)
     */
    public static void writeTestResult(String testName, String testStatus,
                                       long executionTime, String errorMessage) {
        try {
            Workbook workbook;
            Sheet sheet;

            // Mevcut dosyayı kontrol et
            java.io.File file = new java.io.File(TEST_RESULTS_PATH);
            if (file.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fileInputStream);
                    sheet = workbook.getSheet("TestResults");
                    if (sheet == null) {
                        sheet = workbook.createSheet("TestResults");
                        createResultHeader(sheet);
                    }
                }
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("TestResults");
                createResultHeader(sheet);
            }

            // Yeni satır ekle
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Test adı
            Cell testNameCell = newRow.createCell(0);
            testNameCell.setCellValue(testName);

            // Test durumu
            Cell statusCell = newRow.createCell(1);
            statusCell.setCellValue(testStatus);

            // Çalışma süresi
            Cell timeCell = newRow.createCell(2);
            timeCell.setCellValue(executionTime);

            // Hata mesajı
            Cell errorCell = newRow.createCell(3);
            errorCell.setCellValue(errorMessage != null ? errorMessage : "");

            // Tarih/Saat
            Cell dateCell = newRow.createCell(4);
            dateCell.setCellValue(new java.util.Date().toString());

            // Durum rengini ayarla
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            if ("PASS".equals(testStatus)) {
                font.setColor(IndexedColors.GREEN.getIndex());
            } else {
                font.setColor(IndexedColors.RED.getIndex());
            }
            font.setBold(true);
            style.setFont(font);
            statusCell.setCellStyle(style);

            // Column genişliklerini ayarla
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Dosyayı kaydet
            try (FileOutputStream fileOutputStream = new FileOutputStream(TEST_RESULTS_PATH)) {
                workbook.write(fileOutputStream);
            }
            workbook.close();

            LoggerUtils.logInfo("Test sonucu Excel'e yazıldı: " + testName + " - " + testStatus);
        } catch (IOException e) {
            LoggerUtils.logError("Test sonucu Excel'e yazılamadı: " + e.getMessage());
        }
    }

    /**
     * Sonuç dosyası için header satırı oluşturur
     */
    private static void createResultHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Test Adı", "Test Durumu", "Çalışma Süresi (ms)", "Hata Mesajı", "Tarih/Saat"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);

            CellStyle style = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
        }
    }

    /**
     * Cell değerini string olarak alır
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Numeric değeri string'e çevir (ondalık kısmı olmadan)
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}

