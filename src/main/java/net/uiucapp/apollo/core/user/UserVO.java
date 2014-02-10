package net.uiucapp.apollo.core.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVO {
	private Long id;
	private String name;
	private String email;

	private String imageUrl;
}
