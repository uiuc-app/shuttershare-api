package com.apollo.shuttershare.core.group;

import lombok.Data;

@Data
public class GroupMemberVO {
	private Long id;
	private Long groupId;
	private Long userId;
    private Long joinAt;
}
