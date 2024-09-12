package io.github.gabrmsouza.subscription.infrastructure.kafka;

import io.github.gabrmsouza.subscription.AbstractEmbeddedKafkaTest;
import io.github.gabrmsouza.subscription.infrastructure.json.Json;
import io.github.gabrmsouza.subscription.infrastructure.kafka.connect.MessageValue;
import io.github.gabrmsouza.subscription.infrastructure.kafka.connect.Operation;
import io.github.gabrmsouza.subscription.infrastructure.kafka.connect.ValuePayload;
import io.github.gabrmsouza.subscription.infrastructure.kafka.event.EventMsg;
import io.github.gabrmsouza.subscription.infrastructure.mediator.EventMediator;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventCdcListenerTest extends AbstractEmbeddedKafkaTest {

    @SpyBean
    private EventCdcListener eventCdcListener;

    @MockBean
    private EventMediator eventMediator;

    @Value("${kafka.consumers.events.topics}")
    private String eventsTopics;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testEventTopics() throws Exception {
        // given
        final var expectedMainTopic = "subscription.subscription.events";

        // when
        final var actualTopics = admin().listTopics().listings().get(10, TimeUnit.SECONDS).stream()
                .map(TopicListing::name)
                .collect(Collectors.toSet());

        // then
        assertTrue(actualTopics.contains(expectedMainTopic));
    }

    @Test
    public void givenValidResponsesFromHandlerShouldFinishOk() throws Exception {
        // given
        final var expectedMainTopic = "subscription.subscription.events";

        final var actualEvent = new EventMsg(1L);

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(actualEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return t.callRealMethod();
        }).when(eventCdcListener).onMessage(any(), any());

        doNothing().when(eventMediator).mediate(actualEvent.eventId());

        // when
        producer().send(new ProducerRecord<>(eventsTopics, message)).get(10, TimeUnit.SECONDS);

        assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(eventCdcListener, times(1)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
    }
}