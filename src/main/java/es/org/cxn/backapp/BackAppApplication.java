package es.org.cxn.backapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main executable app class.
 * <p>
 * Spring Boot application.
 * </p>
 *
 * @author Santi
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

    /**
     * Default constructor for {@link BackAppApplication}.
     * <p>
     * This constructor is provided by the Java compiler and does not require
     * additional functionality. It allows the creation of instances of the
     * application class.
     * </p>
     */
    public BackAppApplication() {
        // Default constructor
    }
}
