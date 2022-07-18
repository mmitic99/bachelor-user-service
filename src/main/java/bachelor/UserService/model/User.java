package bachelor.UserService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("users")
public class User {
    @Id
    private ObjectId id;
    private String username;
    private String password;
    private List<DataKey> dataKeys;
    private List<DataKeyPair> dataKeyPairs;
    private String key;

    public List<DataKey> addKey(DataKey key){
        if(dataKeys == null){
            dataKeys = new ArrayList<>();
        }
        dataKeys.add(key);
        return dataKeys;
    }

    public List<DataKeyPair> addKeyPair(DataKeyPair key){
        if(dataKeyPairs == null){
            dataKeyPairs = new ArrayList<>();
        }
        dataKeyPairs.add(key);
        return dataKeyPairs;
    }
}
