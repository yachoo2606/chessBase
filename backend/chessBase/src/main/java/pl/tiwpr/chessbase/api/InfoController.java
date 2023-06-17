package pl.tiwpr.chessbase.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Title;
import pl.tiwpr.chessbase.services.TitlesService;

import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/info")
public class InfoController {
    private final TitlesService titlesService;

    public InfoController(TitlesService titlesService) {
        this.titlesService = titlesService;
    }

    @GetMapping("/titles")
    public Page<Title> getAllTitles(@RequestParam(defaultValue ="1") int page,
                                          @RequestParam(defaultValue = "#{titleRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return this.titlesService.getAllTitles(pageable);
    }

    @GetMapping("/titles/{codeName}")
    public ResponseEntity<Title> getSpecificTitle(@PathVariable String codeName){
        return ResponseEntity.ok().body(titlesService.getTitle(codeName));
    }

    @GetMapping("/titles/genders/{gender}")
    public ResponseEntity<List<Title>> getTitlesByGender(@PathVariable Gender gender){
        return ResponseEntity.ok().body(titlesService.getTitlesByGender(gender));
    }

}
