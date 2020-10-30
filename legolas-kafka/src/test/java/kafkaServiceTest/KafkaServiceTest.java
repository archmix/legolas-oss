package kafkaServiceTest;

import legolas.async.api.interfaces.Promise;
import legolas.config.api.interfaces.Configuration;
import legolas.kafka.interfaces.KafkaServiceId;
import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.runtime.core.interfaces.RuntimeEnvironment;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static legolas.kafka.interfaces.KafkaEntry.KAFKA_BROKER;
import static org.junit.Assert.assertThat;

public class KafkaServiceTest {

  @Test
  public void shouldStartKafkaAndMigrate() throws ExecutionException, InterruptedException {
    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    final Promise<RunningEnvironment> promise = RuntimeEnvironment.TEST.start(executorService);
    final RunningEnvironment environment = promise.get();

    final Configuration configuration = environment.get(KafkaServiceId.INSTANCE).get().configuration();
    assertThat(configuration.getString(KAFKA_BROKER).get(), CoreMatchers.notNullValue());

    final HashMap<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getString(KAFKA_BROKER).get());
    final AdminClient adminClient = AdminClient.create(properties);

    final Set<String> topics = adminClient.listTopics().names().get();
    assertThat(topics, CoreMatchers.hasItems("test", "test.new-topic"));

    final Map<String, TopicDescription> topicDescriptionMap = adminClient.describeTopics(Arrays.asList("test.new-topic", "test")).all().get();
    final TopicDescription topicDescriptionNewTopic = topicDescriptionMap.get("test.new-topic");
    final TopicDescription topicDescriptionTest = topicDescriptionMap.get("test");

    assertThat(topicDescriptionNewTopic.partitions().size(), CoreMatchers.equalTo(3));
    assertThat(topicDescriptionTest.partitions().size(), CoreMatchers.equalTo(2));
  }

}
