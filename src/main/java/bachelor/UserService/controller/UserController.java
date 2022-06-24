package bachelor.UserService.controller;

import bachelor.UserService.dto.CredentialsDto;
import bachelor.UserService.dto.DataKeyDto;
import bachelor.UserService.dto.DataKeyPairDto;
import bachelor.UserService.dto.UserDto;
import bachelor.UserService.service.AwsKeyManagementService;
import bachelor.UserService.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}/data-key")
    public ResponseEntity<DataKeyDto> getKey(@PathVariable String id){
        return ResponseEntity.ok(awsKeyManagementService.GenerateDataKey(id));
    }

    @GetMapping("{id}/data-key-pair")
    public ResponseEntity<DataKeyPairDto> getKeyPair(@PathVariable String id){
        return ResponseEntity.ok(awsKeyManagementService.GenerateDataKeyPair(id));
    }
}
