package kafkaServiceTest;

import legolas.config.api.interfaces.Configuration;
import legolas.kafka.interfaces.KafkaServiceId;
import legolas.provided.infra.LegolasExtension;
import legolas.runtime.core.interfaces.RunningEnvironment;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static legolas.kafka.interfaces.KafkaEntry.KAFKA_BROKER;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@ExtendWith(LegolasExtension.class)
public class KafkaServiceTest {

  @Test
  public void shouldStartKafkaAndMigrate(RunningEnvironment environment) throws ExecutionException, InterruptedException {
    final Configuration configuration = environment.get(KafkaServiceId.INSTANCE).get().configuration();
    assertThat(configuration.getString(KAFKA_BROKER).get(), notNullValue());

    final HashMap<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getString(KAFKA_BROKER).get());
    final AdminClient adminClient = AdminClient.create(properties);

    final Set<String> topics = adminClient.listTopics().names().get();
    assertThat(topics, hasItems("test", "test.new-topic"));

    final Map<String, TopicDescription> topicDescriptionMap = adminClient.describeTopics(Arrays.asList("test.new-topic", "test")).allTopicNames().get();
    final TopicDescription topicDescriptionNewTopic = topicDescriptionMap.get("test.new-topic");
    final TopicDescription topicDescriptionTest = topicDescriptionMap.get("test");

    assertThat(topicDescriptionNewTopic.partitions().size(), equalTo(3));
    assertThat(topicDescriptionTest.partitions().size(), equalTo(2));
  }
}
