package bachelor.UserService.service.impl;

import bachelor.UserService.dto.CredentialsDto;
import bachelor.UserService.dto.UserDto;
import bachelor.UserService.exception.BadRequestException;
import bachelor.UserService.model.User;
import bachelor.UserService.repository.UserRepository;
import bachelor.UserService.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto login(CredentialsDto credentialsDto) {

        List<User> users = userRepository.findAll();
        User user = users.stream().filter(user1 -> user1.getUsername().equals(credentialsDto.getUsername()) && user1.getPassword().equals(credentialsDto.getPassword())).findFirst().orElseThrow(() -> new BadRequestException("Username or password not match"));

        return UserDto.builder().username(credentialsDto.getUsername()).id(user.getId().toHexString()).build();
    }
}
