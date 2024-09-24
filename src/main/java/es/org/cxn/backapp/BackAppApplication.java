
package es.org.cxn.backapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main executable app class.
 *
 * @author Santi
 *
 *
 *         Spring Boot application
 */
@SpringBootApplication
public class BackAppApplication {

    /**
     * The application main method.
     *
     * @param args the application initial arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(BackAppApplication.class, args);
    }
}
