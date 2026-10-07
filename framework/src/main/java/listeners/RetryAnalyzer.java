package listeners;

import config.ConfigConstants;
import config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reusable retry analyzer for automatically rerunning flaky mobile tests when enabled.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryAnalyzer.class);
    private int count = 0;

    @Override
    public boolean retry(ITestResult result) {
        boolean retryEnabled = ConfigReader.getBoolean(ConfigConstants.RETRY_FAILED_TESTS, false);
        if (!retryEnabled) {
            return false;
        }

        int maxRetryCount = ConfigReader.getInt(ConfigConstants.MAX_RETRY_COUNT, 1);
        if (count < maxRetryCount) {
            count++;
            LOGGER.warn("Retrying test '{}' [Attempt {} of {}]", result.getName(), count, maxRetryCount);
            return true;
        }
        return false;
    }
}
