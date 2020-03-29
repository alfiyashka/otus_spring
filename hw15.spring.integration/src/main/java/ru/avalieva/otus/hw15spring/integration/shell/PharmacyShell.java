package ru.avalieva.otus.hw15spring.integration.shell;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.avalieva.otus.hw15spring.integration.domain.Medicine;
import ru.avalieva.otus.hw15spring.integration.domain.MedicineItem;
import ru.avalieva.otus.hw15spring.integration.domain.Recipe;
import ru.avalieva.otus.hw15spring.integration.integration.PharmacyIntegration;
import ru.avalieva.otus.hw15spring.integration.service.IOService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class PharmacyShell {

    private final PharmacyIntegration pharmacyIntegration;
    private final IOService ioService;
    private LoginType loginType = LoginType.USER;

    public PharmacyShell(PharmacyIntegration pharmacyIntegration, IOService ioService) {
        this.pharmacyIntegration = pharmacyIntegration;
        this.ioService = ioService;
    }

    @ShellMethod(key = "as_root", value = "as root user")
    public void asroot() {
        loginType = LoginType.ROOT;
    }

    @ShellMethod(key = "buy", value = "buy medicine")
    public void byMedicineByRecipe(@ShellOption({"doctor", "d"}) String doctor,
                                   @ShellOption({"date", "dt"}) String date,
                                   @ShellOption({"medicines", "m"}) String medicines) {
        List<String> medicinesList = Arrays.asList(medicines.split(","));
        List<MedicineItem> medicineItems = new ArrayList<>();
        medicinesList.forEach(it -> medicineItems.add(new MedicineItem(it)));

        Recipe recipe = new Recipe(medicineItems, date, doctor);
        if (recipe.isValid()) {
            List<Medicine> medicinesToBuy = pharmacyIntegration.getMedicine(recipe)
                    .stream()
                    .filter(Medicine::isNotEmpty)
                    .collect(Collectors.toList());
            if (medicinesToBuy.isEmpty()) {
                ioService.outputData("There are no required medicines");
            }
            else {
                ioService.outputData(String.format("There are medicines: %s", medicinesToBuy.stream()
                        .map(Medicine::getName)
                        .collect(Collectors.joining(","))));
            }
        }
        else {
            ioService.outputData("Recipe is not valid");
        }
    }

    @ShellMethodAvailability("rootAvailability")
    @ShellMethod(key = "add", value = "add medicine")
    public void addMedicine(@ShellOption({"medicine", "m"}) String medicineName,
                            @ShellOption({"production date", "d"}) String productionDate) {
        Medicine medicine = new Medicine(0, medicineName, productionDate);
        var result = pharmacyIntegration.addMedicine(medicine);
        if (result) {
            ioService.outputData(String.format("Medicine %s is added", medicine.getName()));
        }
        else {
            ioService.outputData(String.format("Cannot add medicine %s", medicine.getName()));
        }
    }

    @ShellMethod(key = "getAll", value = "get all medicine")
    public void getAllMedicine() {
        Message message = MessageBuilder.withPayload("").build();

        var medicines = pharmacyIntegration.getAllMedicine(message);
        if (!medicines.isEmpty()) {
            ioService.outputData(String.format("Medicines %s", medicines.stream()
                    .map(Medicine::getName)
                    .collect(Collectors.joining(","))));
        }
        else {
            ioService.outputData(String.format("Cannot get medicines from database"));
        }
    }


    public Availability rootAvailability() {
        return loginType == LoginType.ROOT
                ? Availability.available()
                : Availability.unavailable("you have not permissions");
    }


}
