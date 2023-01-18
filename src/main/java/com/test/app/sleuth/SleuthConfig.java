package com.test.app.sleuth;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagationConfig;
import brave.baggage.BaggagePropagationCustomizer;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

    public final static String TEST_BAGGAGE_FIELD_NAME = "test";

    @Bean
    BaggageField testBaggageField() {
        return BaggageField.create(TEST_BAGGAGE_FIELD_NAME);
    }

    @Bean
    public BaggagePropagationCustomizer baggagePropagationCustomizer(List<BaggageField> baggageFields) {
        return builder -> baggageFields.forEach(baggageField -> builder.add(BaggagePropagationConfig.SingleBaggageField.local(baggageField)));
    }
}
