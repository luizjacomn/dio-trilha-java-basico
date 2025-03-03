package com.luizjacomn.designpatterns.service;

import com.luizjacomn.designpatterns.model.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ViaCepCachedProxy implements IViaCepService {

    @Value("${print-cache}")
    private boolean printCache;

    private final ViaCepService viaCepService;

    private final Map<String, ViaCepResponse> cache = new HashMap<>();

    @Override
    public ViaCepResponse findByCep(String cep) {
        if (cache.containsKey(cep)) {
            return cache.get(cep);
        }

        var viaCepResponse = viaCepService.findByCep(cep);
        cache.put(cep, viaCepResponse);

        this.printCacheIfPropertyTrue();

        return viaCepResponse;
    }

    private void printCacheIfPropertyTrue() {
        if (printCache) {
            log.info("Cache: {}\n", cache);
        }
    }

}
