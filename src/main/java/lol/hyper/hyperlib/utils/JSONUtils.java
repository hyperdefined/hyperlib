package lol.hyper.hyperlib.utils;

import lol.hyper.hyperlib.HyperLib;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JSONUtils {

    /**
     * HttpClient for requests.
     */
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Get a JSONArray from a URL.
     *
     * @param url The URL to get JSONArray from.
     * @return The response JSONArray. Returns null if there was some issue.
     */
    public static JSONArray requestJSONArray(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", Constants.USER_AGENT)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new JSONArray(response.body());
            } else {
                HyperLib.getPluginLogger().error("JSONArray request failed to {}, returned HTTP code {}", url, response.statusCode());
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Get a JSONObject from a URL.
     *
     * @param url The URL to get JSON from.
     * @return The response JSONArray. Returns null if there was some issue.
     */
    public static JSONObject requestJSONObject(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", Constants.USER_AGENT)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new JSONObject(response.body());
            } else {
                HyperLib.getPluginLogger().error("JSONObject request failed to {}, returned HTTP code {}", url, response.statusCode());
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
