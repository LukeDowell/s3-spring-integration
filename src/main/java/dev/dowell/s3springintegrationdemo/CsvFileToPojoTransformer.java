package dev.dowell.s3springintegrationdemo;

import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class CsvFileToPojoTransformer implements Transformer {

  @Override
  public Message<?> transform(Message<?> message) {
    // TODO yoink from mapper in background
    return null;
  }
}
