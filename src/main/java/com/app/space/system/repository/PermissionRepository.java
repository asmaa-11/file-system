
package com.app.space.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.space.system.entity.Item;
import com.app.space.system.entity.Permission;
import com.app.space.system.entity.PermissionGroup;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

	public Permission findByUserEmail(String userEmail);

}
