package com.app.space.system.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "permission_group")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PermissionGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "group_name")
	private String groupName;
	/*
	 * @OneToMany(mappedBy = "permissionGroup", fetch = FetchType.LAZY, cascade =
	 * CascadeType.ALL) private Set<Item> item;
	 * 
	 */

	/*
	 * public Set<Item> getItem() { return item; }
	 * 
	 * public void setItem(Set<Item> item) { this.item = item; }
	 */
	public Integer getId() {
		return id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}