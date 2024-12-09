package VTTPday19.practiceExam.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PracExamRepository {

    @Autowired @Qualifier("redis-object")
    private RedisTemplate<String, Object> template;

    //HSET {toDoList} {id} {jsonStr}
    public void saveJsonStr(String id, String jsonStr){
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        hashOps.put("toDoList", id, jsonStr);
    }

    //HVALS {toDoList}
    public List<Object> getJsonStr (){
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        List<Object> listObj = hashOps.values("toDoList");
        return listObj;
    }



    
}