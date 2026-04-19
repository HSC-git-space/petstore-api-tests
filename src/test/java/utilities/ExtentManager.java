package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter(
                    "reports/ExtentReport.html"
            );
            reporter.config().setReportName("Petstore API Test Report");
            reporter.config().setDocumentTitle("API Test Results");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}