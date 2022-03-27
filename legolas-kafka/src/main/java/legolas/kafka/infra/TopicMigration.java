package legolas.kafka.infra;

import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.stream.Collectors;

public class TopicMigration {

  private List<Topic> topics;

  public List<Topic> getTopics() {
    return topics;
  }

  public void setTopics(final List<Topic> topics) {
    this.topics = topics;
  }

  public List<NewTopic> getAsNewTopic() {
    return this.topics.stream()
      .map(t -> new NewTopic(t.getName(), t.getPartitions(), t.getReplicationFactor()))
      .collect(Collectors.toList());
  }

  static class Topic {

    private String name;
    private Integer partitions;
    private Short replicationFactor;

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    public Integer getPartitions() {
      if (partitions == null || partitions < 1) {
        return 1;
      }
      return partitions;
    }

    public void setPartitions(final Integer numPartitions) {
      this.partitions = numPartitions;
    }

    public Short getReplicationFactor() {
      if (replicationFactor == null || replicationFactor < 1) {
        return 1;
      }
      return replicationFactor;
    }

    public void setReplicationFactor(final Short replicationFactor) {
      this.replicationFactor = replicationFactor;
    }
  }
}
