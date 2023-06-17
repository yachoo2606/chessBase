package pl.tiwpr.chessbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Title;

import java.util.List;
import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title,Long>{

    Optional<Title> findOneById(Long id);
    Optional<Title> findOneByCodeName(String codeName);
    Optional<Title> findOneByName(String name);
    List<Title> findAllByGender(Gender gender);
}
