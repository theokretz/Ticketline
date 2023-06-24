package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/news")
public class NewsEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final ResourceLoader resourceLoader;

    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper, ResourceLoader resourceLoader) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.resourceLoader = resourceLoader;
    }


    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get list of news articles without details", security = @SecurityRequirement(name = "apiKey"))
    public List<SimpleNewsDto> findAll(Authentication authentication, @Valid boolean includingRead) {
        LOGGER.info("GET /api/v1/news?includingRead={}", includingRead);

        return newsMapper.newsToSimpleNewsDto(newsService.findAll((Integer) authentication.getPrincipal(), includingRead));

    }


    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get detailed information about a specific message", security = @SecurityRequirement(name = "apiKey"))
    public DetailedNewsDto getDetailedNews(@Valid @PathVariable Integer id, Authentication authentication) {
        LOGGER.info("GET /api/v1/news/{}", id);
        try {
            News news = newsService.findOne(id, (Integer) authentication.getPrincipal());
            return newsMapper.newsToDetailedNewsDto(news);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find news" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    private boolean isImage(MultipartFile file) {
        String[] allowedContentTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
        // Check if the file's content type is one of the allowed image types
        for (String contentType : allowedContentTypes) {
            if (contentType.equals(file.getContentType())) {
                return true;
            }
        }
        return false;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Publish new news article", security = @SecurityRequirement(name = "apiKey"))
    public DetailedNewsDto create(@Valid @ModelAttribute DetailedNewsDto newsDto, @RequestParam(value = "image", required = false) MultipartFile image) {
        LOGGER.info("POST /api/v1/news body: {}", newsDto);
        String filePath = null;
        newsDto.setImagePath(null);
        if (image != null && !image.isEmpty()) {
            if (!isImage(image)) {
                LOGGER.warn("Unable to save image, wrong file type");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong file type (only jpg, png and gif allowed)");
            }
            try {
                // get file extension of image
                String originalFilename = image.getOriginalFilename();
                String fileExtension = null;
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    int dotIndex = originalFilename.lastIndexOf('.');
                    if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                        fileExtension = originalFilename.substring(dotIndex + 1);
                    }
                }
                // create image, overwrite newsDto.imagePath
                String uuid = UUID.randomUUID().toString();
                Resource resource = resourceLoader.getResource("classpath:");
                // get absolute path of /backend
                Path path = Paths.get(resource.getFile().getAbsolutePath()).getParent().getParent();
                String imgPath = "" + uuid + "." + fileExtension;
                filePath = path + "/src/newsImages/" + imgPath;
                File imgFile = new File(filePath);
                // create newsCover directory if it doesn't exist
                if (!imgFile.getParentFile().exists()) {
                    if (!imgFile.getParentFile().mkdirs()) {
                        LOGGER.warn("Unable to create directory for image");
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create directory for image");
                    }
                }
                image.transferTo(imgFile);
                newsDto.setImagePath(imgPath);
            } catch (IOException e) {
                LOGGER.warn("Unable to save image" + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save image");
            }
        }
        try {
            return newsMapper.newsToDetailedNewsDto(
                newsService.publishNews(newsMapper.detailedNewsDtoToNews(newsDto), newsDto.getEventId()));
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find event" + e.getMessage());
            // delete image if event not found
            if (image != null && !image.isEmpty() && filePath != null) {
                try {
                    File file = new File(filePath);
                    file.delete();
                } catch (Exception ex) {
                    LOGGER.warn("Unable to delete image" + ex.getMessage());
                }
            }
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }

    }
}

