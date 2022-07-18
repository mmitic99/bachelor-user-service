package bachelor.UserService.controller;

import bachelor.UserService.dto.CredentialsDto;
import bachelor.UserService.dto.DataKeyDto;
import bachelor.UserService.dto.DataKeyPairDto;
import bachelor.UserService.dto.UserDto;
import bachelor.UserService.exception.BadRequestException;
import bachelor.UserService.service.AwsKeyManagementService;
import bachelor.UserService.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AwsKeyManagementService awsKeyManagementService;

    @PostMapping("login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto){
        return ResponseEntity.ok(userService.login(credentialsDto));
    }

    @GetMapping("create-admin")
    public ResponseEntity<Object> createAdmin(){
        userService.createAdmin();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/data-key")
    public ResponseEntity<DataKeyDto> getKey(@PathVariable String id){
        return ResponseEntity.ok(awsKeyManagementService.GenerateDataKeyForUser(id));
    }

    @GetMapping("{id}/data-key-pair")
    public ResponseEntity<DataKeyPairDto> getKeyPair(@PathVariable String id){
        return ResponseEntity.ok(awsKeyManagementService.GenerateDataKeyPairForUser(id));
    }

    @GetMapping("decryptKey")
    public ResponseEntity<String> decryptKey(@RequestBody DataKeyDto key){
        if(key.getCiphertext().length() != 224 && key.getCiphertext().length() != 1840){
            throw new BadRequestException("Key not valid");
        }
        return ResponseEntity.ok(awsKeyManagementService.DecryptKey(Base64.getDecoder().decode(key.getCiphertext())));
    }
}
