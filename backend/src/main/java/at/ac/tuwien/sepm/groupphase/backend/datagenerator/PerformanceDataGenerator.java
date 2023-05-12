package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Optional;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator", "hallDataGenerator"})
public class PerformanceDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_PERFORMANCES_TO_GENERATE = 10;
    private static final LocalDateTime TEST_PERFORMANCE_START = LocalDateTime.of(2023, 1, 1, 10, 0);

    private final PerformanceRepository performanceRepository;
    private final HallRepository hallRepository;
    private final EventRepository eventRepository;

    public PerformanceDataGenerator(PerformanceRepository performanceRepository, HallRepository hallRepository, EventRepository eventRepository) {
        this.performanceRepository = performanceRepository;
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generatePerformance() {
        if (performanceRepository.findAll().size() > 0) {
            LOGGER.debug("performance already generated");
        } else {
            LOGGER.debug("generating {} performance entries", NUMBER_OF_PERFORMANCES_TO_GENERATE);
            int offset = 0;
            for (int i = 0; i < NUMBER_OF_PERFORMANCES_TO_GENERATE; i++) {
                Optional<Hall> hall = hallRepository.findById(offset + 1);
                Optional<Event> event = eventRepository.findById(offset + 1);
                if ((i + 1) % 2 == 0) {
                    offset++;
                }
                if (hall.isEmpty()) {
                    LOGGER.debug("hall {} not found", i);
                    continue;
                }
                if (event.isEmpty()) {
                    LOGGER.debug("event {} not found", i);
                    continue;
                }
                Performance performance = Performance.PerformanceBuilder.aPerformance()
                    .withDatetime(TEST_PERFORMANCE_START.plusDays(i).plusHours(i))
                    .withHall(hall.get())
                    .withEvent(event.get())
                    .build();
                LOGGER.debug("saving performance {}", performance);
                performanceRepository.save(performance);
            }
        }
    }
}
