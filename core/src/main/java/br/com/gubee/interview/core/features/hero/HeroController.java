package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {
    @Autowired
    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateHeroRequest> create(
            @Validated @RequestBody CreateHeroRequest createHeroRequest) {
        heroService.create(createHeroRequest);
        return new ResponseEntity<>(createHeroRequest, HttpStatus.OK);
    }

    @GetMapping(path="/all")
    public ResponseEntity<List<CreateHeroRequest>> getAllHeroes(){
        List<CreateHeroRequest> hero = heroService.listAllUsers();
        return new ResponseEntity<>(hero, HttpStatus.OK);
    }

    @GetMapping(path="/hero/{id}")
    public ResponseEntity<CreateHeroRequest> getHeroById(@PathVariable UUID id){
        CreateHeroRequest hero = heroService.getHeroById(id);
        //return new ResponseEntity<>(hero, HttpStatus.OK);
        return ResponseEntity.ok(hero);
    }

    @DeleteMapping(path = "/delete/hero/{id}")
    public ResponseEntity<CreateHeroRequest> deleteHeroById(@PathVariable UUID id){
        heroService.deleteHeroById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path="/compara/{id1}/{id2}")
    public ResponseEntity<CreateHeroRequest> getHeroById(@PathVariable(name = "id1") UUID idH1, @PathVariable(name = "id2") UUID idH2){
        //a logica de comparar é simples, o heroi de referencia é sempre o que esta no
        //primeiro parametro, então quando retornar algum valor negativo, significa
        //que o heroi 2 é melhor nesse valor em relação ao heroi 1
        //caso retorne 0 significa que é igual
        CreateHeroRequest statsComparados = heroService.compareHeroesById(idH1, idH2);
        return new ResponseEntity<>(statsComparados, HttpStatus.OK);
    }
}
