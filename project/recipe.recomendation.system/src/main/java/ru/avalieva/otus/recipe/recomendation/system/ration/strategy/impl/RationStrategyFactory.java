package ru.avalieva.otus.recipe.recomendation.system.ration.strategy.impl;

import ru.avalieva.otus.recipe.recomendation.system.feing.CookbookFeignController;
import ru.avalieva.otus.recipe.recomendation.system.model.ERationStrategy;
import ru.avalieva.otus.recipe.recomendation.system.ration.strategy.RationStrategy;


public class RationStrategyFactory {

     public static RationStrategy getStrategy(ERationStrategy rationStrategy, CookbookFeignController feignController) {
         switch (rationStrategy) {
             case VEGAN:
                 return new VeganStrategy(feignController);
             case RANDOM:
                 return new RandomStrategy(feignController);
             case DIET_SOUP:
                 return new SoupDietStrategy(feignController);
                 default:
                     return null;
         }
     }
}
