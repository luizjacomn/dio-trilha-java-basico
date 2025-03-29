package com.luizjacomn.designpatterns.controller;

import com.luizjacomn.designpatterns.model.dto.ClientRequest;
import com.luizjacomn.designpatterns.model.dto.ClientResponse;
import com.luizjacomn.designpatterns.model.entity.Client;
import com.luizjacomn.designpatterns.model.entity.level.ClientLevelState;
import com.luizjacomn.designpatterns.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientResponse> getAll() {
        return clientService.findAll().stream().map(ClientResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ClientResponse getById(@PathVariable Long id) {
        return ClientResponse.from(clientService.findById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClientResponse save(@Valid @RequestBody ClientRequest clientRequest) {
        return ClientResponse.from(clientService.save(clientRequest.name(), clientRequest.code()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest clientRequest) {
        return ClientResponse.from(clientService.update(id, clientRequest.name(), clientRequest.code()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }

    @PatchMapping("/{id}/{nextLevel}")
    public ClientResponse changeState(@PathVariable Long id, @PathVariable ClientLevelState.Level nextLevel) {
        Consumer<Client> action = switch (nextLevel) {
            case ASSOCIATE -> Client::makeAssociate;
            case PRO -> Client::makePro;
            case PLUS -> Client::makePlus;
            case PRIME -> Client::makePrime;
        };

        return ClientResponse.from(clientService.changeState(id, action));
    }

}
