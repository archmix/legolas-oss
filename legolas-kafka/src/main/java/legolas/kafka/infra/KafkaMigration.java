package legolas.kafka.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import legolas.kafka.interfaces.KafkaEntry;
import legolas.kafka.interfaces.KafkaServiceId;
import legolas.migration.api.interfaces.Migration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.runtime.core.interfaces.RunningEnvironment;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@MigrationComponent
public class KafkaMigration implements Migration {

  private static final String TOPICS_FILE = "topics.yaml";

  @Override
  public void migrate(final RunningEnvironment runningEnvironment) {
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
      runningEnvironment.get(KafkaServiceId.INSTANCE).get().configuration().
        getString(KafkaEntry.KAFKA_BROKER).get());

    try (final AdminClient adminClient = AdminClient.create(properties)) {
      final CreateTopicsResult result = adminClient.createTopics(loadTopics());
      result.all().get();
    } catch (final Exception e) {
      throw new RuntimeException("Failed to create topics", e);
    }
  }

  private List<NewTopic> loadTopics() throws IOException {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final File file = new File(classLoader.getResource(TOPICS_FILE).getFile());
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    final TopicMigration topicMigration = mapper.readValue(file, TopicMigration.class);
    
    return topicMigration.getAsNewTopic();
  }

  @Override
  public MigrationId id() {
    return () -> "migration.kafka";
  }
}
