package com.apollo.shuttershare.web;

import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.group.GroupService;
import com.apollo.shuttershare.core.group.GroupVO;
import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
@Transactional
@Slf4j
public class GroupController {
    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public GroupElements.JsonGroups getGroupsList(
            UserVO user) {
        log.debug("Getting the list of joined groups for user {}", user);

        List<GroupElements.JsonGroup> groups = groupService.getJsonGroupsWithUser(user);
        return new GroupElements.JsonGroups(groups);
    }

    @ResponseBody
    @RequestMapping(value = "/users/{userId}/groups", method = RequestMethod.POST)
    public GroupElements.JsonGroup joinGroup(
                              UserVO user,
                              @PathVariable Long userId,
                              @RequestParam String pass_phrase) {
        log.debug("User {} joining the group with passPhrase {}", user, pass_phrase);

        if (!user.getId().equals(userId)) {
            throw new UnauthorizedException("Your credential does not match our records.");
        }
        GroupVO group = groupService.joinUserToGroupWithPassPhrase(user, pass_phrase);
        List<UserVO> groupMembers = userService.getUsersWithGroup(group);

        return new GroupElements.JsonGroup(group, groupMembers);
    }

    @ResponseBody
    @RequestMapping(value = "/users/{userId}/groups/{groupId}", method = RequestMethod.DELETE)
    public void leaveGroup(
            UserVO user,
            @PathVariable Long userId,
            @PathVariable Long groupId) {
        log.debug("User {} leaving the group with id {}", user, groupId);

        if (!user.getId().equals(userId)) {
            throw new UnauthorizedException("Your credential does not match our records.");
        }

        groupService.authenticateUserToGroups(new Long[]{groupId}, user);
        groupService.leaveGroupWithUser(groupId, user);
    }

    @ResponseBody
    @RequestMapping(value = "/groups", method = RequestMethod.POST)
    public GroupElements.JsonGroup createGroup(
            UserVO user,
            @RequestParam String name) {
        log.debug("User {} creating a new group with name {}", user, name);

        GroupVO group = groupService.createGroupWithCreatorAndName(user, name);
        List<UserVO> groupMembers = new ArrayList<>();
        groupMembers.add(user);

        return new GroupElements.JsonGroup(group, groupMembers);
    }
}
