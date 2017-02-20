package org.openlmis.example;


import org.openlmis.example.i18n.ExposedMessageSourceImpl;
import org.openlmis.example.web.NotificationValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration for the Sprint Boot application (this service).
 */
@SpringBootApplication
@ComponentScan("org.openlmis.example*")
@ImportResource("applicationContext.xml")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * Configures the {@link LocaleResolver} bean for Spring.
   * @return the locale resolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.ENGLISH);
    return slr;
  }

  /**
   * Configures the message source bean for Spring - source of translatable
   * messages.
   * @return the message source
   */
  @Bean
  public ExposedMessageSourceImpl messageSource() {
    ExposedMessageSourceImpl messageSource = new ExposedMessageSourceImpl();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setUseCodeAsDefaultMessage(true);
    return messageSource;
  }


  /**
   * Associate a NotificationValidator with the beforeCreateNotification event
   * (NOTE that this should work out of the box. In reality, though,
   * it requires the configuration offered by ValidatorRegistrar.java).
   * @return the validator for notifications
   */
  @Bean
  public NotificationValidator beforeCreateNotificationValidator() {
    return new NotificationValidator();
  }

  /**
   * Associate a NotificationValidator with the beforeSaveNotification event
   * (NOTE that this should work out of the box. In reality, though,
   * it requires the configuration offered by ValidatorRegistrar.java)
   * @return the validator for notifications
   */
  @Bean
  public NotificationValidator beforeSaveNotificationValidator() {
    return new NotificationValidator();
  }
}