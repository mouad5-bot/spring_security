package com.youcode.taskmanager.core.utils.Config;

import com.youcode.taskmanager.common.security.dto.request.SignUpRequest;
import com.youcode.taskmanager.core.database.model.entity.User;
import com.youcode.taskmanager.shared.Enum.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class Mapper {

    private  final PasswordEncoder passwordEncoder;

    @Configuration
    public class ModelMapperConfig {
        @Bean
        public ModelMapper modelMapper() {

            ModelMapper modelMapper = new ModelMapper();

            userRequestToUser(modelMapper);

            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper;
        }
    }


    private void userRequestToUser(ModelMapper modelMapper) {
        Converter<String, String> passwordEncoderConverter = context -> {
            String sourcePassword = context.getSource();
            return passwordEncoder.encode(sourcePassword);
        };

        modelMapper.createTypeMap(SignUpRequest.class, User.class)
                .addMappings(mapper -> mapper.using(passwordEncoderConverter)
                        .map(SignUpRequest::getPassword, User::setPassword))
                .setPostConverter(context -> {
                    User user = context.getDestination();
                    user.setRole(Role.USER);
                    return user;
                });
    }
}
