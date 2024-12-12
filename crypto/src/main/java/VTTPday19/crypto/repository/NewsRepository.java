package VTTPday19.crypto.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import VTTPday19.crypto.model.Articles;
import jakarta.json.JsonObject;

@Repository
public class NewsRepository {
    @Autowired@Qualifier("redis-object")
    private RedisTemplate <String, Object> template;

    //HSET {mapName} {id} {jsonStr}
    public void saveArticles(Articles art, JsonObject json){
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        hashOps.put("mapName", art.getId(), json.toString());
    }

    public String getArticle(String id){
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        return (String) hashOps.get("mapName", id);
    }



}   
