package ru.avalieva.otus.recipe.recomendation.system.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.avalieva.otus.recipe.recomendation.system.domain.Diet;
import ru.avalieva.otus.recipe.recomendation.system.domain.User;
import ru.avalieva.otus.recipe.recomendation.system.model.ERationStrategy;


@ChangeLog
public class MongoChangeLog {
    @ChangeSet(order = "000", id = "dropDB", author = "avalieva", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "init_diets", author = "avalieva", runAlways = true)
    public void initDiets(MongoTemplate template){
        Diet dietRandom = new Diet((new ObjectId()).toHexString(), ERationStrategy.RANDOM.getValue(), ERationStrategy.RANDOM.name(),
                "Не диета. Рецепты выбираются случайным образом");
        template.save(dietRandom);

        Diet dietVegan = new Diet((new ObjectId()).toHexString(),  ERationStrategy.VEGAN.getValue(), ERationStrategy.VEGAN.name(),
                "Веганская диета не включает мясо и птицу, рыбу и морепродукты, яйца, молочные продукты, а также блюда, которые могут включать компоненты животного происхождения: желатин, казеин, молочную кислоту.\n" +
                        "Продукты растительного происхождения потребляются без всяких ограничений. Веганы едят бобовые, сыр тофу, орехи, семена, овощи и фрукты, пьют кокосовое и миндальное молоко.");
        template.save(dietVegan);

        Diet dietSoup = new Diet((new ObjectId()).toHexString(),  ERationStrategy.DIET_SOUP.getValue(), ERationStrategy.DIET_SOUP.name(),
                "Диета на супах длится 7 дней, похудеть можно на 5 кг. Супы можно готовить из всего, кроме: мяса, морепродуктов, картофеля, бобовых." +
                        " Также запрещено добавлять специи и сливочное масло, соли можно самую малость. Хлеб, мучные изделия кушать нельзя.");
        template.save(dietSoup);

    }

    @ChangeSet(order = "001", id = "init_usrs", author = "avalieva", runAlways = true)
    public void initUsers(MongoTemplate template){
        User admin = new User(null,"admin", "$2a$10$46EiMLeacFJq9dj.O5oDzu/CTCtmplo/5.QKQE5spiA9IIerfs72i"); // password
        template.save(admin);

        User alfiya = new User(null,"'alfiya'", "$2a$10$hYrTym98OrAfW1N91.7NN.NasRvNH6U1Ly2GmcQVMEPNMiY9107la"); // mypassword
        template.save(alfiya);

    }

}
