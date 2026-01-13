package org.example.listeners;

import com.aventstack.extentreports.ExtentTest;
import org.example.base.BaseTest;
import org.example.utils.LoggerUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener sınıfı - Test durumlarını dinler ve raporlara ekler
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtils.logInfo("Test başlatıldı: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtils.logInfo("Test başarılı: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtils.logError("Test başarısız: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            LoggerUtils.logError("Hata: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtils.logWarn("Test atlandı: " + result.getMethod().getMethodName());
    }
}

