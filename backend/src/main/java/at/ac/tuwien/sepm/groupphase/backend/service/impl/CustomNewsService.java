package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomNewsService implements NewsService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;

    public CustomNewsService(NewsRepository newsRepository, EventRepository eventRepository) {
        this.newsRepository = newsRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public List<News> findAll(Integer userId, boolean includingRead) {
        LOGGER.debug("Find all news articles");
        if (includingRead) {
            return newsRepository.findNewsOrderByPublicationDateDesc();
        }
        return newsRepository.findNewsNotReadByUserOrderByPublicationDateDesc(userId);
    }

    @Override
    public News findOne(Integer id, Integer userId) {
        LOGGER.debug("Find news with id {}", id);
        Optional<News> message = newsRepository.findById(id);
        if (message.isPresent()) {
            newsRepository.markNewsAsReadByUser(id, userId);
            return message.get();
        } else {
            throw new NotFoundException(String.format("Could not find news article with id %s", id));
        }
    }

    @Override
    public News publishNews(News news, Integer eventId) {
        LOGGER.debug("Publish new news article {}", news);
        news.setPublicationDate(LocalDate.now());
        news.setId(null);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            news.setEvent(event.get());
        } else {
            throw new NotFoundException(String.format("Could not find event with id %s", eventId));
        }
        return newsRepository.save(news);
    }
}
