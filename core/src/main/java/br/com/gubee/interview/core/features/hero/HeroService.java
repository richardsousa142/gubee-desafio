package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private PowerStatsService powerStatsService;

    @Autowired
    public HeroService(HeroRepository heroRepository, PowerStatsService powerStatsService){
        this.heroRepository = heroRepository;
        this.powerStatsService = powerStatsService;
    }

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        UUID id = powerStatsService.create(new PowerStats(createHeroRequest));
        return heroRepository.create(new Hero(createHeroRequest, id));
    }

    public List<CreateHeroRequest> listAllUsers(){
        return heroRepository.findAllHeroes();
    }

    public CreateHeroRequest getHeroById(UUID id){
        return heroRepository.getHeroById(id);
    }

    public void deleteHeroById(UUID id){
        heroRepository.deleteHeroById(id);
    }

    public CreateHeroRequest compareHeroesById(UUID idH1, UUID idH2){
        return heroRepository.compareHeroesById(idH1, idH2);
    }
}
