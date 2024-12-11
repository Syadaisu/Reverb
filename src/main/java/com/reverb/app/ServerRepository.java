package com.reverb.app;

import org.springframework.data.repository.CrudRepository;

public interface ServerRepository extends CrudRepository<Server, Integer> {
    Server findByServerName(String name);
}
