package bachelor.UserService.config.mongoDB;

import bachelor.UserService.service.AwsKeyManagementService;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;

import java.util.Arrays;
import java.util.List;

public class MongoDBAfterLoadEventListener extends AbstractMongoEventListener<Object> {
    @Autowired
    private AwsKeyManagementService awsKeyManagementService;

    @SneakyThrows
    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {

        Document eventObject = event.getDocument();

        List<String> keysNotToEncrypt = Arrays.asList("_class", "_id", "dataKeys", "dataKeyPairs");

        for (String key :
                eventObject.keySet()) {
            if (!keysNotToEncrypt.contains(key)) {
                Binary bytes = (Binary) eventObject.get(key);

                String value = this.awsKeyManagementService.DecryptText(bytes.getData());
                eventObject.put(key, value);

            }
        }

        super.onAfterLoad(event);

    }
}
