package bachelor.UserService.service;

import bachelor.UserService.dto.CredentialsDto;
import bachelor.UserService.dto.UserDto;

public interface UserService {

    UserDto login(CredentialsDto credentialsDto);
}
