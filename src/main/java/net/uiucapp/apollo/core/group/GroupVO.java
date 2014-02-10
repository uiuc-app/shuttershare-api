package net.uiucapp.apollo.core.group;

import lombok.Data;

@Data
public class GroupVO {

	private long id;

	private long ownerId;

	private String name;

	private long createdAt;
	
}
