package bachelor.UserService.service.impl;

import bachelor.UserService.dto.CredentialsDto;
import bachelor.UserService.dto.UserDto;
import bachelor.UserService.exception.BadRequestException;
import bachelor.UserService.model.User;
import bachelor.UserService.repository.UserRepository;
import bachelor.UserService.service.AwsKeyManagementService;
import bachelor.UserService.service.UserService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AwsKeyManagementService awsKeyManagementService;
    private final ModelMapper mapper;

    @Override
    public UserDto login(CredentialsDto credentialsDto) {

        List<User> users = userRepository.findAll();
        User user = users.stream().filter(user1 -> user1.getUsername().equals(credentialsDto.getUsername()) && user1.getPassword().equals(credentialsDto.getPassword())).findFirst().orElseThrow(() -> new BadRequestException("Username or password not match"));

        return UserDto.builder().username(credentialsDto.getUsername()).id(user.getId().toHexString()).build();
    }

    @Override
    public void createAdmin() {
        userRepository.save(User.builder().key(awsKeyManagementService.GenerateDataKey().getCiphertext()).password("admin").username("admin").build());
    }

    @Override
    public UserDto getUserById(String id) {
        if (ObjectId.isValid(id)) {
            ObjectId userId = new ObjectId(id);

            List<User> users = userRepository.findAll();
            User user = users.stream().filter(user1 -> user1.getId().equals(userId)).
            findFirst().orElseThrow(() -> new BadRequestException("User doesn't exist"));

            return mapper.map(user, UserDto.class);
        } else {
            throw new BadRequestException("User id is invalid");
        }
    }
}
