package bachelor.UserService.service;

public interface EncryptionService {

    byte[] encrypt(String message, String key);

    String decrypt(byte[] message, String key);
}
