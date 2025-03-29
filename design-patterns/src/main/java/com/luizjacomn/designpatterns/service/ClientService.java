package com.luizjacomn.designpatterns.service;

import com.luizjacomn.designpatterns.exception.ClientNotFoundException;
import com.luizjacomn.designpatterns.model.entity.Client;
import com.luizjacomn.designpatterns.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final IViaCepService iViaCepService;

    public ClientService(ClientRepository clientRepository,
                         @Qualifier("viaCepCachedProxy") IViaCepService iViaCepService) {
        this.clientRepository = clientRepository;
        this.iViaCepService = iViaCepService;
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() ->
            new ClientNotFoundException(id)
        );
    }

    @Transactional
    public Client save(String name, String code) {
        var viaCepResponse = iViaCepService.findByCep(code);

        var address = viaCepResponse.toAddressResponse().toEntity();
        var client = new Client();
        client.setName(name);
        client.setAddress(address);

        clientRepository.save(client);

        return clientRepository.save(client);
    }

    @Transactional
    public Client update(Long id, String name, String code) {
        var client = this.findById(id);

        var viaCepResponse = iViaCepService.findByCep(code);
        var address = viaCepResponse.toAddressResponse().toEntity();

        client.setName(name);
        client.setAddress(address);

        return clientRepository.save(client);
    }

    @Transactional
    public void delete(Long id) {
        this.findById(id);

        clientRepository.deleteById(id);
    }

    @Transactional
    public Client changeState(Long id, Consumer<Client> action) {
        var client = this.findById(id);

        action.accept(client);

        return client;
    }

}
