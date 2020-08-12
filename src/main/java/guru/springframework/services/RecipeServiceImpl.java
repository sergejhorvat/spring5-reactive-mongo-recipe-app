package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/13/17.
 */
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("I'm in the service");

        // Change to reactive repository
        //Set<Recipe> recipeSet = new HashSet<>();
        //recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {

        // Change to reactive type
        return recipeReactiveRepository.findById(id);


//        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
//
//        if (!recipeOptional.isPresent()) {
//            throw new NotFoundException("Recipe Not Found. For ID value: " + id );
//        }
//
//        return recipeOptional.get();
    }

    @Override
    //@Transactional // removed in reactive
    public Mono<RecipeCommand> findCommandById(String id) {

        // Change to reactive type
        return recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
                    recipeCommand.getIngredients().forEach(rc -> {
                        rc.setRecipeId(recipeCommand.getId());
                    });
                    return recipeCommand;
                });


//        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(findById(id));
//
//        //enhance command object with id value
//        if(recipeCommand.getIngredients() != null && recipeCommand.getIngredients().size() > 0){
//            recipeCommand.getIngredients().forEach(rc -> {
//                rc.setRecipeId(recipeCommand.getId());
//            });
//        }
//
//        return recipeCommand;
    }

    @Override
    @Transactional
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {

        // Convert to reactive type
        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
                .map(recipeToRecipeCommand::convert);



//        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
//
//        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
//        log.debug("Saved RecipeId:" + savedRecipe.getId());
//        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {

        // Convert to reactive type
        recipeReactiveRepository.deleteById(idToDelete).subscribe();
        return Mono.empty();


        //recipeRepository.deleteById(idToDelete);
    }
}
