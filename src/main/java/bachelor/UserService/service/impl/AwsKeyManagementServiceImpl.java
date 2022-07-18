package bachelor.UserService.service.impl;

import bachelor.UserService.dto.DataKeyDto;
import bachelor.UserService.dto.DataKeyPairDto;
import bachelor.UserService.exception.BadRequestException;
import bachelor.UserService.model.DataKey;
import bachelor.UserService.model.DataKeyPair;
import bachelor.UserService.model.User;
import bachelor.UserService.repository.UserRepository;
import bachelor.UserService.service.AwsKeyManagementService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@AllArgsConstructor
public class AwsKeyManagementServiceImpl implements AwsKeyManagementService {

    private final KmsClient kmsClient;
    private final UserRepository userRepository;

    @Override
    public String GetKeyByAlias(String alias) {
        var aliasResponse = kmsClient.listAliases(ListAliasesRequest.builder().limit(100).build());
        if (aliasResponse == null || aliasResponse.aliases() == null) {
            throw new BadRequestException("Aliases are empty.");
        }
        var foundAlias = aliasResponse.aliases().stream().filter(aliasR -> aliasR.aliasName().equals("alias/" + alias)).findFirst().orElse(null);
        if (foundAlias != null) {
            return foundAlias.targetKeyId();
        }
        throw new BadRequestException("Alias not found");
    }

    @Override
    public byte[] EncryptText(String textToEncrypt, String keyId) {
        if (textToEncrypt == null || textToEncrypt.isBlank()) {
            return null;
        }

        SdkBytes input = SdkBytes.fromByteArray(textToEncrypt.getBytes(StandardCharsets.UTF_8));
        EncryptRequest encryptRequest = EncryptRequest.builder().keyId(keyId).plaintext(input).build();

        EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
        return encryptResponse.ciphertextBlob().asByteArray();
    }

    @Override
    public String DecryptText(byte[] encryptedText) {
        if (encryptedText == null) {
            return "";
        }
        SdkBytes input = SdkBytes.fromByteArray(encryptedText);
        DecryptRequest decryptRequest = DecryptRequest.builder().ciphertextBlob(input).build();

        DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);


        if (decryptResponse != null) {
            return new String(decryptResponse.plaintext().asByteArray());
        }

        throw new BadRequestException("Decrypt error");
    }

    @Override
    public String DecryptKey(byte[] encryptedText) {
        if (encryptedText == null) {
            return "";
        }
        SdkBytes input = SdkBytes.fromByteArray(encryptedText);
        DecryptRequest decryptRequest = DecryptRequest.builder().ciphertextBlob(input).build();

        DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);


        if (decryptResponse != null) {
            return Base64.getEncoder().encodeToString(decryptResponse.plaintext().asByteArray());
        }

        throw new BadRequestException("Decrypt error");
    }

    @SneakyThrows
    @Override
    public DataKeyDto GenerateDataKeyForUser(String id) {
        if (ObjectId.isValid(id)) {
            ObjectId objectId = new ObjectId(id);

            DataKeyDto dataKeyDto = GenerateDataKey();

            User user = userRepository.findById(objectId).orElseThrow(() -> new BadRequestException("Invalid user"));
            user.addKey(DataKey.builder().ciphertext(dataKeyDto.getCiphertext()).build());
            userRepository.save(user);

            return DataKeyDto.builder().plaintext(dataKeyDto.getPlaintext()).ciphertext(dataKeyDto.getCiphertext()).build();
        }

        /*
        cipher key decoding

        System.out.println(c.getBytes());
        System.out.println(DecryptKey(Base64.getDecoder().decode(c)));
        */
        throw new BadRequestException("User id is invalid");
    }

    @SneakyThrows
    @Override
    public DataKeyPairDto GenerateDataKeyPairForUser(String id) {
        if (ObjectId.isValid(id)) {
            ObjectId objectId = new ObjectId(id);

            String keyId = GetKeyByAlias("bachelor-order");

            var response = kmsClient.generateDataKeyPair(GenerateDataKeyPairRequest.builder().keyId(keyId).keyPairSpec(DataKeyPairSpec.RSA_2048).build());

            var publicKey = Base64.getEncoder().encodeToString(response.publicKey().asByteArray());
            var plain = Base64.getEncoder().encodeToString(response.privateKeyPlaintext().asByteArray());
            var cipher = Base64.getEncoder().encodeToString(response.privateKeyCiphertextBlob().asByteArray());

            User user = userRepository.findById(objectId).orElseThrow(() -> new BadRequestException("Invalid user"));
            user.addKeyPair(DataKeyPair.builder().publicKey(publicKey).privateCiphertext(cipher).build());
            userRepository.save(user);

            return DataKeyPairDto.builder().publicKey(publicKey).privatePlaintext(plain).privateCiphertext(cipher).build();


        /*
        cipher key decoding

        System.out.println(c.getBytes());
        System.out.println(DecryptKey(Base64.getDecoder().decode(c)));
        */
        }
        throw new BadRequestException("User id is invalid");

    }

    @Override
    public DataKeyDto GenerateDataKey() {

        String keyId = GetKeyByAlias("bachelor-order");

        var response = kmsClient.generateDataKey(GenerateDataKeyRequest.builder().keyId(keyId).keySpec(DataKeySpec.AES_128).build());

        var plain = Base64.getEncoder().encodeToString(response.plaintext().asByteArray());
        var cipher = Base64.getEncoder().encodeToString(response.ciphertextBlob().asByteArray());

        return DataKeyDto.builder().plaintext(plain).ciphertext(cipher).build();
    }
}
