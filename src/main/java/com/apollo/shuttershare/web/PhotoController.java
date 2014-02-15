package com.apollo.shuttershare.web;

import lombok.extern.slf4j.Slf4j;
import com.apollo.shuttershare.common.SqlInjectionMapper;
import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
@Slf4j
public class PhotoController {
    @Autowired
    PhotoService photoService;

	@Autowired
	SqlInjectionMapper sqlInjectionMapper;

    @ResponseBody
	@RequestMapping(value = "/groups/{groupId}/photos", method = RequestMethod.GET)
	public String list(Model model,
                       UserVO user,
                       @PathVariable Long groupId) {
        return null;
	}

    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public String upload(Model model) {
        return "gallery";
    }
}
