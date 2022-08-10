package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tokenbid.models.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
