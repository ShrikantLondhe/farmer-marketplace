package com.farmermarket;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FarmerMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmerMarketplaceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }
}
