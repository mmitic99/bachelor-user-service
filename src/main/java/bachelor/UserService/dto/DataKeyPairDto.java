package bachelor.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataKeyPairDto {
    private String publicKey;   //Base64Encoded
    private String privatePlaintext;  //Base64Encoded
    private String privateCiphertext;  //Base64Encoded
}
