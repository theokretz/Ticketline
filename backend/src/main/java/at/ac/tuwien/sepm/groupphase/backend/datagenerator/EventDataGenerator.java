package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

@Profile("generateData")
@Component
public class EventDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 5;
    private static final String TEST_EVENT_NAME = "EventName";
    private static final String TEST_TYPE = "Type";
    private static final Duration TEST_DURATION = Duration.ofHours(1);
    private static final String TEST_DESCRIPTION = "Description";

    private final EventRepository eventRepository;

    public EventDataGenerator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generateEvent() {
        if (eventRepository.findAll().size() > 0) {
            LOGGER.debug("event already generated");
        } else {
            LOGGER.debug("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Event event = Event.EventBuilder.aEvent()
                    .withName(TEST_EVENT_NAME + " " + i)
                    .withType(TEST_TYPE + " " + i)
                    .withLength(TEST_DURATION)
                    .withDescription(TEST_DESCRIPTION + " " + i)
                    .build();
                LOGGER.debug("saving event {}", event);
                eventRepository.save(event);
            }
        }
    }
}
