package com.example.app.support;

import com.example.core.common.exception.CustomException;
import com.example.db.common.exception.DbErrorCode;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class TestContainerChecker implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {

        Environment env = testContext.getApplicationContext().getEnvironment();

        String url = env.getProperty("spring.datasource.url", "");
        String username = env.getProperty("spring.datasource.username", "");

        boolean isTestDb = url.contains("/testdb");
        boolean isTestUser = "test".equals(username);

        if (!isTestDb || !isTestUser) {
            throw CustomException.db(DbErrorCode.INVALID_TEST_DB);
        }
    }
}
