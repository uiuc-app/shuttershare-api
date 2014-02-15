package com.apollo.shuttershare.core.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVO {
	private Long id;
    private String name;
    private Long jointAt;
	private String apiKey;
    private String deviceType;
    private String udid;
}
