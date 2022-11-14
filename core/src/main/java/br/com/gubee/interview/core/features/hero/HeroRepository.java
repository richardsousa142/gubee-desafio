package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.enums.Race;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
        " (name, race, power_stats_id)" +
        " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
            "race", hero.getRace().name(),
            "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
            CREATE_HERO_QUERY,
            params,
            UUID.class);
    }

    private static final String SELECT_ALL_HERO_QUERY =
            "SELECT * FROM interview_service.hero as h " +
                    "inner join" +
                    " interview_service.power_stats as ps" +
                    " ON ps.id = h.power_stats_id";

    List<CreateHeroRequest> findAllHeroes(){
        return namedParameterJdbcTemplate.query(
                SELECT_ALL_HERO_QUERY,
                new RowMapper<CreateHeroRequest>() {
                    @Override
                    public CreateHeroRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new CreateHeroRequest(
                                rs.getString("name"),
                                null,
                                rs.getInt("strength"),
                                rs.getInt("agility"),
                                rs.getInt("dexterity"),
                                rs.getInt("intelligence")
                        );
                    }
                }
        );
    }

    CreateHeroRequest getHeroById(UUID id){
        String sql = "SELECT * FROM interview_service.hero as h inner join interview_service.power_stats as ps ON ps.id = h.power_stats_id where h.id = :id";
        Map<String, Object> map = Map.of("id", id);

        CreateHeroRequest hero = namedParameterJdbcTemplate.queryForObject(sql, map, new RowMapper<CreateHeroRequest>() {
            @Override
            public CreateHeroRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CreateHeroRequest(
                        rs.getString("name"),
                        null,
                        rs.getInt("strength"),
                        rs.getInt("agility"),
                        rs.getInt("dexterity"),
                        rs.getInt("intelligence")
                );
            }
        });
        return hero;
    }

    void deleteHeroById(UUID id){
        String sqlHero  = "DELETE from interview_service.hero as h where h.id = :id";
        Map<String, Object> param = Map.of("id", id);
        namedParameterJdbcTemplate.queryForMap(sqlHero, param);
    }

    CreateHeroRequest compareHeroesById(UUID idH1, UUID idH2){
        String sql = "SELECT name, race, strength, agility, dexterity, intelligence FROM interview_service.hero as h " +
                "inner join interview_service.power_stats as ps ON ps.id = h.power_stats_id where h.id = :id";
        Map<String, Object> mapH1 = Map.of("id", idH1);
        Map<String, Object> mapH2 = Map.of("id", idH2);

        Map<String, Object> attrsHero1 = namedParameterJdbcTemplate.queryForMap(sql, mapH1);
        Object [] arrayStatsHero1 = attrsHero1.values().toArray();

        Map<String, Object> attrsHero2 = namedParameterJdbcTemplate.queryForMap(sql, mapH2);
        Object [] arrayStatsHero2 = attrsHero2.values().toArray();

        return new CreateHeroRequest(
                (String) arrayStatsHero1[0],
                Race.valueOf((String) arrayStatsHero1[1]),
                (int) arrayStatsHero1[2] - (int) arrayStatsHero2[2],
                (int) arrayStatsHero1[3] - (int) arrayStatsHero2[3],
                (int) arrayStatsHero1[4] - (int) arrayStatsHero2[4],
                (int) arrayStatsHero1[5] - (int) arrayStatsHero2[5]);
    }
}
