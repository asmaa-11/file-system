
package com.app.space.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.space.system.entity.Item;
import com.app.space.system.entity.ItemDetails;

public interface ItemDetailsRepository extends JpaRepository<ItemDetails, Integer> {

}
