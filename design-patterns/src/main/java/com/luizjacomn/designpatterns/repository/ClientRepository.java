package com.luizjacomn.designpatterns.repository;

import com.luizjacomn.designpatterns.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  @Query("SELECT c FROM Client c JOIN FETCH c.address a WHERE c.id = :id")
  @Override
  Optional<Client> findById(Long id);

  @Query("SELECT c FROM Client c JOIN FETCH c.address a")
  @Override
  List<Client> findAll();

}
