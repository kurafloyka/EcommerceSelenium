package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Test verileri için Excel dosyaları oluşturan utility sınıfı
 */
public class ExcelDataGenerator {
    private static final String TEST_DATA_PATH = "src/main/resources/testdata/TestData.xlsx";

    /**
     * Test verileri Excel dosyasını oluşturur
     */
    public static void createTestDataFile() {
        try (Workbook workbook = new XSSFWorkbook()) {
            // ECommerceData sheet'i oluştur
            Sheet ecommerceSheet = workbook.createSheet("ECommerceData");

            // Header satırı
            Row headerRow = ecommerceSheet.createRow(0);
            String[] headers = {"Email", "Password", "SearchKeyword", "MainCategory", "SubCategory", "PageNumber", "ProductIndex"};
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Column genişliklerini ayarla
            for (int i = 0; i < headers.length; i++) {
                ecommerceSheet.autoSizeColumn(i);
            }

            // Dosyayı kaydet
            try (FileOutputStream fileOut = new FileOutputStream(TEST_DATA_PATH)) {
                workbook.write(fileOut);
            }

            System.out.println("Test data Excel dosyası oluşturuldu: " + TEST_DATA_PATH);
            System.out.println("Bu dosyayı düzenleyerek test verilerinizi ekleyebilirsiniz.");
            System.out.println("Not: Sadece header'lar oluşturuldu, test verilerini manuel olarak eklemeniz gerekmektedir.");
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası oluşturulamadı: " + TEST_DATA_PATH, e);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    public static void main(String[] args) {
        createTestDataFile();
    }
}

