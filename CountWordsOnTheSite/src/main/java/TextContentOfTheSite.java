import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class TextContentOfTheSite {
    private static final Logger LOGGER_INFO = LogManager.getLogger("site.found");
    private static final Logger LOGGER_ERROR = LogManager.getLogger("site.error");
    private String url;
    private Map<String, Integer> viewCountWords;

    public TextContentOfTheSite(String url) {
        this.url = url;
        viewCountWords = new TreeMap<>();
        addToViewCountWords(getContentInArrayString(url));
    }

    public Map<String, Integer> getViewCountWords() {
        return viewCountWords;
    }

    private String[] getContentInArrayString(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            LOGGER_INFO.info("страница \"{} \" найдена", url);
        } catch (IOException e) {
            LOGGER_ERROR.error("Сайт не найден", e);
            e.printStackTrace();
        }
        Elements elements = document.getElementsByTag("body");
        String stringPage = elements.text();
        return stringPage.split("[{\\s+}{, }{! }{? }{. }{ (}{) }{\"}{; }{\\[}{\\]}{: " +
                "}{'}{\\s-\\s}{/}{«}{»}]");
    }

    private void addToViewCountWords(String[] arrayWords) {
        for (String str : arrayWords) {
            int countWords = 1;
            if (str.length() == 0) {
                continue;
            } else if (viewCountWords.containsKey(str)) {
                countWords += viewCountWords.get(str);
            }
            viewCountWords.put(str, countWords);
        }
    }
}