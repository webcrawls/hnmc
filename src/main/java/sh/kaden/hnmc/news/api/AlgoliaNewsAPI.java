package sh.kaden.hnmc.news.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlgoliaNewsAPI implements NewsAPI {

    private static final String FRONT_PAGE_URL = "https://hn.algolia.com/api/v1/search_by_date?tags=story";

    private void fetchFrontPage() {
        final URL url;
        try {
            url = new URL(FRONT_PAGE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
