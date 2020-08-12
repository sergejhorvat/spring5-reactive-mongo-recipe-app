package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/27/17.
 */
public interface IngredientService {

    // Changed to reactive type
    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    //IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    // Changed to reactive type
    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);
    //IngredientCommand saveIngredientCommand(IngredientCommand command);

    // Changed to reactive type
    Mono<Void> deleteById(String recipeId, String idToDelete);
    //Void deleteById(String recipeId, String idToDelete);
}
