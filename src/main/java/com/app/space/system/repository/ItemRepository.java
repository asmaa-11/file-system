
package com.app.space.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.space.system.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
