import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String urlSite = "https://www.simbirsoft.com";
        TextContentOfTheSite content = new TextContentOfTheSite(urlSite);
        printWordsAndThisCount(content);
    }
    private static void printWordsAndThisCount(TextContentOfTheSite content) {
        for (Map.Entry<String, Integer> pair : content.getViewCountWords().entrySet()) {
            System.out.println(pair.getKey() + "  " + pair.getValue());
        }

        System.out.printf("%nНа сайте было найдено %d уникальных слов",
                content.getViewCountWords().size());
    }
}