package com.brewmes.demo.Persistence;

import com.brewmes.demo.model.Machine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MachineRepository extends CrudRepository<Machine, UUID> {
}
