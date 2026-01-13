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
        
        // Dosya yolunu belirle - önce relative path, sonra resource path dene
        java.io.InputStream inputStream = null;
        String usedPath = "";
        
        // Önce relative path'i dene
        java.io.File file = new java.io.File(TEST_DATA_PATH);
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
                usedPath = TEST_DATA_PATH;
                System.out.println("Excel dosyası relative path'ten okunuyor: " + TEST_DATA_PATH);
            } catch (IOException e) {
                System.out.println("Relative path'ten okunamadı: " + e.getMessage());
            }
        }
        
        // Eğer relative path'te yoksa, resource path'i dene
        if (inputStream == null) {
            inputStream = ExcelUtils.class.getClassLoader()
                    .getResourceAsStream("testdata/TestData.xlsx");
            if (inputStream != null) {
                usedPath = "testdata/TestData.xlsx (resource)";
                System.out.println("Excel dosyası resource path'ten okunuyor: testdata/TestData.xlsx");
            } else {
                throw new RuntimeException("Excel dosyası bulunamadı. Relative path: " + TEST_DATA_PATH + 
                    ", Resource path: testdata/TestData.xlsx");
            }
        }
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            System.out.println("Excel dosyası açıldı. Sheet sayısı: " + workbook.getNumberOfSheets());
            
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

            System.out.println("Sheet bulundu: " + sheetName + ", Toplam satır sayısı: " + sheet.getLastRowNum());

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
            System.out.println("Header'lar okundu: " + headers);

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
            
            System.out.println("Toplam " + dataRowCount + " satır veri okundu");
            
            if (testDataList.isEmpty()) {
                throw new RuntimeException("Excel dosyasında veri bulunamadı! Sheet: " + sheetName);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası okunamadı: " + usedPath, e);
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
    public static void writeTestResult(String testName, String testStatus, long executionTime, String errorMessage) {
        try {
            Workbook workbook;
            Sheet sheet;
            FileInputStream fileInputStream = null;
            boolean fileExists = true;

            try {
                fileInputStream = new FileInputStream(TEST_RESULTS_PATH);
                workbook = new XSSFWorkbook(fileInputStream);
                sheet = workbook.getSheet("TestResults");
                if (sheet == null) {
                    sheet = workbook.createSheet("TestResults");
                    createResultHeader(sheet);
                }
            } catch (IOException e) {
                fileExists = false;
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("TestResults");
                createResultHeader(sheet);
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }

            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Test Adı
            Cell cell0 = newRow.createCell(0);
            cell0.setCellValue(testName);

            // Test Durumu
            Cell cell1 = newRow.createCell(1);
            cell1.setCellValue(testStatus);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            if ("PASS".equals(testStatus)) {
                font.setColor(IndexedColors.GREEN.getIndex());
            } else {
                font.setColor(IndexedColors.RED.getIndex());
            }
            style.setFont(font);
            cell1.setCellStyle(style);

            // Çalışma Süresi
            Cell cell2 = newRow.createCell(2);
            cell2.setCellValue(executionTime + " ms");

            // Hata Mesajı
            Cell cell3 = newRow.createCell(3);
            cell3.setCellValue(errorMessage != null ? errorMessage : "");

            // Tarih/Saat
            Cell cell4 = newRow.createCell(4);
            cell4.setCellValue(new java.util.Date().toString());

            // Auto-size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(TEST_RESULTS_PATH)) {
                workbook.write(fileOutputStream);
            }
            workbook.close();

        } catch (IOException e) {
            throw new RuntimeException("Test sonucu yazılamadı: " + TEST_RESULTS_PATH, e);
        }
    }

    private static void createResultHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Test Adı", "Test Durumu", "Çalışma Süresi", "Hata Mesajı", "Tarih/Saat"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }

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
                    return String.valueOf((long) cell.getNumericCellValue());
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

