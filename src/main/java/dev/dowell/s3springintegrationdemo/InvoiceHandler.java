package dev.dowell.s3springintegrationdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvoiceHandler implements MessageHandler {

  private final InvoiceRepository invoiceRepository;
  private final ObjectMapper objectMapper;

  public InvoiceHandler(InvoiceRepository invoiceRepository, ObjectMapper objectMapper) {
    this.invoiceRepository = invoiceRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    log.info("Received message! {}", message);
  }
}
