package bachelor.UserService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataKeyPair{
    private String publicKey;   //Base64Encoded
    private String privateCiphertext;  //Base64Encoded
}
