package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"eventDataGenerator"})
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TEST_NEWS_TITLE = " News about";

    private static final int NUMBER_OF_NEWS_TO_GENERATE_FOR_EACH_EVENT = 3;
    private static final String TEST_NEWS_TEXT = " Great News!\nThis is the text of the news about ";

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;

    public NewsDataGenerator(NewsRepository newsRepository, EventRepository eventRepository) {
        this.newsRepository = newsRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generateNews() {
        if (newsRepository.findAll().size() > 0) {
            LOGGER.debug("news already generated");
        } else {
            LOGGER.debug("news {} news entries", NUMBER_OF_NEWS_TO_GENERATE_FOR_EACH_EVENT);
            List<News> newsList = new ArrayList<>();
            eventRepository.findAll().forEach(event -> {
                for (int i = 1; i <= NUMBER_OF_NEWS_TO_GENERATE_FOR_EACH_EVENT; i++) {
                    News news = News.NewsBuilder.aNews()
                        .withContent(TEST_NEWS_TEXT + event.getName() + " " + i)
                        .withPublicationDate(LocalDate.of(2023, 4, i))
                        .withTitle(TEST_NEWS_TITLE + event.getName() + " " + i)
                        .withEvent(event)
                        .build();
                    newsList.add(news);
                }
            });
            LOGGER.debug("saving news {}", newsList);
            newsRepository.saveAll(newsList);
        }
    }

}
