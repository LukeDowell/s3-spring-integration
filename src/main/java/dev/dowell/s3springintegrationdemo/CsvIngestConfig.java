package dev.dowell.s3springintegrationdemo;

import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizer;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizingMessageSource;
import org.springframework.integration.aws.support.filters.S3RegexPatternFileListFilter;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.PollableChannel;

/**
 * When enabled, this application will no longer listen to messages emitted from
 * htzd-payment-background. Instead, this application will consume the Csv input on it's
 * own.
 */
@Configuration
public class CsvIngestConfig {

  // TODO https://github.com/spring-projects/spring-integration-aws/blob/main/src/test/java/org/springframework/integration/aws/inbound/S3InboundChannelAdapterTests.java

  @Autowired
  private AmazonS3 amazonS3;

  @Bean
  public S3InboundFileSynchronizer s3InboundFileSynchronizer() {
    S3InboundFileSynchronizer synchronizer = new S3InboundFileSynchronizer(amazonS3);
    synchronizer.setDeleteRemoteFiles(true);
    synchronizer.setPreserveTimestamp(true);
    synchronizer.setRemoteDirectory(S3_BUCKET);
    synchronizer.setFilter(new S3RegexPatternFileListFilter(".*\\.test$"));
    Expression expression = PARSER.parseExpression("#this.toUpperCase() + '.a'");
    synchronizer.setLocalFilenameGeneratorExpression(expression);
    return synchronizer;
  }

  @Bean
  @InboundChannelAdapter(value = "s3FilesChannel", poller = @Poller(fixedDelay = "100"))
  public S3InboundFileSynchronizingMessageSource s3InboundFileSynchronizingMessageSource() {
    S3InboundFileSynchronizingMessageSource messageSource =
        new S3InboundFileSynchronizingMessageSource(s3InboundFileSynchronizer());
    messageSource.setAutoCreateLocalDirectory(true);
    messageSource.setLocalDirectory(LOCAL_FOLDER);
    messageSource.setLocalFilter(new AcceptOnceFileListFilter<File>());
    return messageSource;
  }

  @Bean
  public PollableChannel s3FilesChannel() {
    return new QueueChannel();
  }

  @Bean
  public IntegrationFlow csvFlow(
      CsvFileToPojoTransformer csvFileToPojoTransformer,
      InvoiceHandler invoiceHandler) {

    return IntegrationFlows.from(s3InboundStreamingMessageSource())
        .transform(csvFileToPojoTransformer)
        .split()
        .handle(invoiceHandler)
        .get();
  }
}
