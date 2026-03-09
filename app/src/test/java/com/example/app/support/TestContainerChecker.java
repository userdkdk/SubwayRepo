package com.example.app.support;

import com.example.core.common.exception.CustomException;
import com.example.db.common.exception.DbErrorCode;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class TestContainerChecker implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {
        Class<?> testClass = testContext.getTestClass();

        if (!requiresDatabaseCheck(testClass)) {
            return;
        }

        Environment env = testContext.getApplicationContext().getEnvironment();

        String url = env.getProperty("spring.datasource.url", "");
        String username = env.getProperty("spring.datasource.username", "");

        boolean isTestDb = url.contains("/testdb");
        boolean isTestUser = "test".equals(username);

        if (!isTestDb || !isTestUser) {
            throw CustomException.db(DbErrorCode.INVALID_TEST_DB);
        }
    }

    private boolean requiresDatabaseCheck(Class<?> testClass) {
        if (AnnotatedElementUtils.hasAnnotation(testClass, WebMvcTest.class)) {
            return false;
        }

        return AnnotatedElementUtils.hasAnnotation(testClass, SpringBootTest.class)
                || AnnotatedElementUtils.hasAnnotation(testClass, DataJpaTest.class)
                || AnnotatedElementUtils.hasAnnotation(testClass, JdbcTest.class);
    }
}
