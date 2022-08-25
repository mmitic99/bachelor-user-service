package bachelor.UserService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

@Configuration
public class KmsClientConfig {

    @Bean
    public KmsClient KmsClient() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create("AKIAWGYYFVFVQMT6L6GV", "xS4x6yOL9G+/eoZEDvl6LDDpitE12yXMGA+iJFbn");
        return KmsClient.builder().credentialsProvider(StaticCredentialsProvider.create(awsCredentials)).region(Region.US_EAST_1).build();
    }
}
