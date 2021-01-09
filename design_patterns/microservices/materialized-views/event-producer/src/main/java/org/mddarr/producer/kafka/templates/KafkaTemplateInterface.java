package org.mddarr.producer.kafka.templates;

import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaTemplateInterface<T> {
    KafkaTemplate<String, T> getKafkaTemplate();
}
