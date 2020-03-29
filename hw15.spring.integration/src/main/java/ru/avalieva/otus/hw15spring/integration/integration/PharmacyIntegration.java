package ru.avalieva.otus.hw15spring.integration.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import ru.avalieva.otus.hw15spring.integration.domain.Medicine;
import ru.avalieva.otus.hw15spring.integration.domain.Recipe;

import java.util.List;

@MessagingGateway
public interface PharmacyIntegration {

    @Gateway(requestChannel = "recipeChannel", replyChannel = "medicineChannel")
    List<Medicine> getMedicine(Recipe recipe);

    @Gateway(requestChannel = "addMedicineChannel", replyChannel = "addMedicineResultChannel")
    boolean addMedicine(Medicine medicine);

    @Gateway(requestChannel = "addAllChannel", replyChannel = "addAllResultChannel")
    List<Medicine> getAllMedicine(Message o);
}
