package com.test.app.sleuth;

import brave.baggage.BaggageField;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SleuthService {

    @Autowired
    private List<BaggageField> baggageFields;

    public void updateBaggageFieldValue(String name, String value) {
        baggageFields.stream()
            .filter(baggageField -> baggageField.name().equals(name))
            .forEach(baggageField -> baggageField.updateValue(value));
    }

    public String getBaggageFieldValue(String name) {
        return baggageFields.stream().filter(baggageField -> baggageField.name().equals(name)).findFirst().get().getValue();
    }
}
