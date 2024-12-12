package VTTPday19.crypto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import VTTPday19.crypto.model.Articles;
import VTTPday19.crypto.repository.NewsRepository;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepo;
    
    
    public static final String GET_URL = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN";

    // @Value("${crypto.api-key}")
    // private String apiKey;

    public static String getJsonString(){
        String url = UriComponentsBuilder
        .fromUriString(GET_URL)
        .toUriString();

        RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        try{
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req,String.class);
            String payload = resp.getBody();
            return payload;

        }catch(HttpClientErrorException | HttpServerErrorException ex){ //catches the 404 that 'fake country' sends back
            System.out.println("something wrong");
            System.out.println(url);
            return ex.getResponseBodyAsString(); //returns the responsebody of error json, so it can be used in validation method
        }
    }




    public List<Articles> getArticles(){
        JsonObject jsonObj = Articles.getJsonObj(getJsonString());
        JsonArray jArray = jsonObj.getJsonArray("Data");
        
        List<Articles> artList = new ArrayList<>();
        for(int i = 0; i < jArray.size(); i++){
            JsonObject jObject = jArray.getJsonObject(i);

            Articles temp = new Articles();

            temp.setId(jObject.getString("id"));
            String epochStr = Integer.toString(jObject.getInt("published_on"));
            temp.setPublished_on(Articles.epochSecondsToDate(epochStr));
            temp.setTitle(jObject.getString("title"));
            temp.setUrl(jObject.getString("url"));
            temp.setImageurl(jObject.getString("imageurl"));
            temp.setBody(jObject.getString("body"));
            temp.setTags(jObject.getString("tags"));
            temp.setCategories(jObject.getString("categories"));

            artList.add(temp);
        }
        return artList;
    }


    public void saveArticles(List<Articles> filtered){
        for(Articles art : filtered){
            JsonObject jObj = Articles.toJson(art);
            newsRepo.saveArticles(art, jObj);
        }
    }


    public JsonObject getArticle(String id){
        String jsonStr = newsRepo.getArticle(id);
        return Articles.getJsonObj(jsonStr);
    }


}
