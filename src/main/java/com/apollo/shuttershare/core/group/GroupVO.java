package com.apollo.shuttershare.core.group;

import lombok.Data;

@Data
public class GroupVO {
	private Long id;
	private String name;
	private Long createdAt;
    private String passPhrase;
}
