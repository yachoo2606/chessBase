package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Title;
import pl.tiwpr.chessbase.repositories.TitleRepository;

import java.util.List;

@Service
@Slf4j
public class TitlesService {
    public final ModelMapper modelMapper;
    private final TitleRepository titleRepository;


    public TitlesService(ModelMapper modelMapper, TitleRepository titleRepository) {
        this.modelMapper = modelMapper;
        this.titleRepository = titleRepository;
    }


    public Page<Title> getAllTitles(Pageable pageable) {
        return titleRepository.findAll(pageable);
    }

    public Title getTitle(String codeName) {
        return titleRepository.findOneByCodeName(codeName).orElseThrow(()-> new MissingDataException("Title not found"));
    }

    public List<Title> getTitlesByGender(Gender gender) {
        return titleRepository.findAllByGender(gender);
    }

}
