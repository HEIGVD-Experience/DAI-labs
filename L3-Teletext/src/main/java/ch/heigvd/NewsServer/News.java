package ch.heigvd.NewsServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.stream.Collectors;

public class News {
    private String type;
    private String news;
    private LocalDateTime date;
    private boolean isBreakingNews;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    News(String type, String[] news, boolean isBreakingNews) {
        this.type = type;
        this.news = Arrays.stream(news, 2, news.length)
                    .collect(Collectors.joining(" "));
        this.date = LocalDateTime.now();
        this.isBreakingNews = isBreakingNews;
    }

    public String getType() {
        return type;
    }

    public String getNews() {
        return news;
    }

    public boolean isBreakingNews() {
        return isBreakingNews;
    }

    @Override
    public String toString() {
        return (isBreakingNews ? "BREAKING NEWS" : "NEWS         ") + " - " + date.format(DTF) + " - " + news;
    }
}
