package com.apollo.shuttershare.web;

import com.apollo.shuttershare.common.SqlInjectionMapper;
import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.photoentry.PhotoEntryService;
import com.apollo.shuttershare.core.photoentry.PhotoEntryVO;
import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(value = "/")
@Slf4j
public class HomeController {
    @Autowired
    PhotoService photoService;

    @Autowired
    UserService userService;

    @Autowired
    PhotoEntryService photoEntryService;

    @Autowired
    PhotoController photoController;

	private static final Long userId = 1l;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
        UserVO user = userService.getUser(userId);
		List<PhotoElements.JsonPhoto> photos = photoService.getLatestJsonPhotosListForUser(user, 20, Long.MAX_VALUE, -1l);

        model.addAttribute("photos", photos);
        model.addAttribute("api_key", user.getApiKey());
		return "home";
	}

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String form(Model model) {
        UserVO user = userService.getUser(userId);
        model.addAttribute("api_key", user.getApiKey());
        return "form";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(Model model,
                         @RequestParam Long[] groupIds,
                         @RequestParam MultipartFile image,
                         @RequestParam(required = false) Double latitude,
                         @RequestParam(required = false) Double longitude) {
        UserVO user = userService.getUser(userId);
        photoController.uploadPhoto(user, groupIds, image, latitude, longitude);
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginFailed(Model model) {
        model.addAttribute("error", "true");
        return "login";
    }

}
