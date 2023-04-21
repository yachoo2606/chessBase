package pl.tiwpr.chessbase.config;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;


@NoArgsConstructor
@Configuration
@Builder
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){

        final ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE);

        return modelMapper;
    }

}
