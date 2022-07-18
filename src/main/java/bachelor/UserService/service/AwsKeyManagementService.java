package bachelor.UserService.service;

import bachelor.UserService.dto.DataKeyDto;
import bachelor.UserService.dto.DataKeyPairDto;

public interface AwsKeyManagementService {
    String GetKeyByAlias(String alias);

    byte[] EncryptText(String textToEncrypt, String keyId);

    String DecryptText(byte[] encryptedText);

    String DecryptKey(byte[] encryptedText);

    DataKeyDto GenerateDataKeyForUser(String id);

    DataKeyPairDto GenerateDataKeyPairForUser(String id);

    DataKeyDto GenerateDataKey();
}
