package es.org.cxn.backapp.config;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration class for setting up the JavaMailSender bean. This class
 * defines a conditional bean for JavaMailSenderImpl, which is only created if
 * the `spring.mail.host` property is set.
 */
@Configuration
public class MailConfig {

    /**
     * Custom condition to check if `spring.mail.host` is non-empty.
     */
    static class MailHostCondition implements Condition {

        /**
         * Default constructor for {@code MailHostCondition}.
         */
        public MailHostCondition() {
            // Default constructor
        }

        /**
         * Checks whether the `spring.mail.host` property is set and non-empty.
         *
         * @param context  the condition context
         * @param metadata metadata of the annotated component
         * @return {@code true} if the `spring.mail.host` property is set and non-empty,
         *         otherwise {@code false}
         */
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String host = context.getEnvironment().getProperty("spring.mail.host");
            return host != null && !host.isEmpty();
        }
    }

    /**
     * The SMTP host for sending emails. This value is retrieved from the
     * application properties using the key {@code spring.mail.host}.
     */
    @Value("${spring.mail.host}")
    private String mailHost;

    /**
     * The SMTP port for sending emails. Defaults to 587 if not specified. This
     * value is retrieved from the application properties using the key
     * {@code spring.mail.port}.
     */
    @Value("${spring.mail.port:587}")
    private int mailPort;

    /**
     * The username for SMTP authentication. This value is retrieved from the
     * application properties using the key {@code spring.mail.username}. If not
     * provided, it defaults to an empty string.
     */
    @Value("${spring.mail.username:}")
    private String mailUsername;

    /**
     * The password for SMTP authentication. This value is retrieved from the
     * application properties using the key {@code spring.mail.password}. If not
     * provided, it defaults to an empty string.
     */
    @Value("${spring.mail.password:}")
    private String mailPassword;

    /**
     * Default constructor for {@code MailConfig}. It is required by Spring for
     * component scanning and bean creation.
     */
    public MailConfig() {
        // Default constructor
    }

    /**
     * Creates a JavaMailSenderImpl bean if the `spring.mail.host` property is set.
     *
     * @return a configured JavaMailSenderImpl instance
     */
    @Bean
    @Primary
    @Conditional(MailHostCondition.class) // Custom condition
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

}