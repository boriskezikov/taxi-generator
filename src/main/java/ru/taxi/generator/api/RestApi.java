package ru.taxi.generator.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taxi.generator.generator.GeneratorAccessorService;
import ru.taxi.generator.generator.GeneratorParametersEntity;
import ru.taxi.generator.generator.GeneratorParams;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gen")
public class RestApi {

    private final GeneratorAccessorService accessorService;

    @PutMapping("/update")
    public GeneratorParametersEntity updateParams(@RequestBody GeneratorParams generatorParams) {
        return accessorService.updateGeneratorParams(generatorParams);
    }

    @GetMapping("/load")
    public GeneratorParametersEntity loadParams() {
        return accessorService.loadParameters();
    }

    @GetMapping("/generate")
    public void generate() {
        accessorService.generate();
    }
}
