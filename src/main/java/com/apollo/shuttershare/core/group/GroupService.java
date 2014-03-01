package com.apollo.shuttershare.core.group;

import com.apollo.shuttershare.common.NotFoundException;
import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.photo.PhotoVO;
import com.apollo.shuttershare.core.photoentry.PhotoEntryService;
import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import com.apollo.shuttershare.web.GroupElements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Date: 2/15/14
 */
@Service
@Transactional
@Slf4j
public class GroupService {
    @Autowired
    GroupMapper groupMapper;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Autowired
    PhotoEntryService photoEntryService;

    @Autowired
    UserService userService;

    public GroupVO getGroup(Long id) {
        log.debug("Retrieving GroupVO with id {} from Database", id);
        GroupVO group = groupMapper.get(id);
        if (group == null) {
            throw new NotFoundException("GroupVO does not exist in DB");
        } else {
            return group;
        }
    }

    public List<GroupMemberVO> getGroupMembers(GroupVO group) {
        log.debug("Retrieving list of group members for group {}", group);
        return groupMemberMapper.getListWithGroup(group);
    }

    public GroupMemberVO getGroupMemberWithGroupAndUser(GroupVO group, UserVO user) {
        log.debug("Getting the GroupMemberVO for group {} and user {}", group, user);
        List<GroupMemberVO> groupMembers = groupMemberMapper.getListWithGroupIdAndUserId(group.getId(), user.getId());
        if (groupMembers.isEmpty()) {
            throw new NotFoundException("You are not joined to the group");
        } else if(groupMembers.size() > 1) {
            throw new ShutterShareException("More than one records are found on the group with same user");
        } else {
            return groupMembers.get(0);
        }
    }

    public void authenticateUserToGroups(Long[] groupIds, UserVO user) {
        log.debug("Authenticating user {} to groups {}", user, Arrays.toString(groupIds));
        if (groupIds == null || groupIds.length == 0) {
            throw new ShutterShareException("There are no groups to authenticate");
        } else {
            for (Long groupId : groupIds) {
                List<GroupMemberVO> groupMembers = groupMemberMapper.getListWithGroupIdAndUserId(groupId, user.getId());
                if (groupMembers.isEmpty() || groupMembers.size() > 1) {
                    log.debug("User {} is not authorized to access group with id {}", user, groupId);
                    throw new UnauthorizedException("User is not authorized to access one or more of the groups");
                }
            }
        }
        return;
    }

    public void addPhotoToGroup(Long groupId, PhotoVO photo, UserVO photoOwner) {
        log.debug("Adding photo {} to the group with id {}", photo, photoOwner);
        GroupVO group = getGroup(groupId);
        List<GroupMemberVO> groupMembers = getGroupMembers(group);
        for (GroupMemberVO groupMember : groupMembers) {
            boolean acknowledged = (groupMember.getUserId().equals(photoOwner.getId()));
            photoEntryService.addPhotoEntry(photo, group, groupMember.getUserId(), acknowledged);
        }
    }

    public void saveGroup(GroupVO group) {
        log.debug("Saving the group {} to DB", group);
        groupMapper.save(group);
    }

    public GroupMemberVO joinGroup(GroupVO group, UserVO user) {
        log.debug("Joining user {} to the group {}", user, group);
        List<GroupMemberVO> groupMembers = getGroupMembers(group);
        for (GroupMemberVO groupMember : groupMembers) {
            if (groupMember.getUserId().equals(user.getId())) {
                throw new ShutterShareException("The user is already joined to the group");
            }
        }

        GroupMemberVO groupMember = new GroupMemberVO();
        groupMember.setGroupId(group.getId());
        groupMember.setUserId(user.getId());
        groupMember.setJoinAt(System.currentTimeMillis());

        groupMemberMapper.save(groupMember);
        return groupMember;
    }

    public List<GroupElements.JsonGroup> getJsonGroupsWithUser(UserVO user) {
        log.debug("Getting joined groups list for the user {}", user);

        List<GroupElements.JsonGroup> result = new ArrayList<>();
        List<GroupVO> joinedGroups = groupMapper.getListWithUserId(user.getId());

        for (GroupVO group : joinedGroups) {
            List<UserVO> users = userService.getUsersWithGroup(group);
            result.add(new GroupElements.JsonGroup(group, users));
        }

        return result;
    }

    public GroupVO getGroupWithPassPhrase(String passPhrase) {
        log.debug("Getting a group with passPhrase {}", passPhrase);
        List<GroupVO> groups = groupMapper.getListWithPassPhrase(passPhrase);
        if (groups.isEmpty()) {
            throw new UnauthorizedException("Your Passphrase does not match our records.");
        } else if (groups.size() > 1) {
            throw new ShutterShareException("There are more than one with same passPhrase");
        } else {
            return groups.get(0);
        }
    }

    public GroupVO joinUserToGroupWithPassPhrase(UserVO user, String passPhrase) {
        log.debug("Joining User {} to a group with passPhrase {}", user, passPhrase);
        GroupVO group = getGroupWithPassPhrase(passPhrase);
        joinGroup(group, user);
        return group;
    }

    public void leaveGroupWithUser(Long groupId, UserVO user) {
        log.debug("Quitting the user {} from the group with id {}", user, groupId);
        GroupMemberVO groupMember = getGroupMemberWithGroupAndUser(getGroup(groupId), user);
        groupMemberMapper.delete(groupMember);
    }

    public GroupVO createGroupWithCreatorAndName(UserVO creator, String groupName) {
        log.debug("Creating a new group by user {}, with a group name {}", creator, groupName);
        String passPhrase;
        int tryCount = 5;
        do {
            passPhrase = UUID.randomUUID().toString();
            List<GroupVO> existingGroups = groupMapper.getListWithPassPhrase(passPhrase);
            if (!existingGroups.isEmpty()) {
                tryCount--;
                log.warn("UUID conflict with group {}", existingGroups.get(0));
                if (tryCount == 0) {
                    throw new ShutterShareException("Unable to create a valid passphrase.");
                }
            } else {
                break;
            }
        } while (tryCount > 0);

        GroupVO group = new GroupVO();
        group.setName(groupName);
        group.setCreatedAt(System.currentTimeMillis());
        group.setPassPhrase(passPhrase);

        saveGroup(group);

        joinGroup(group, creator);
        return group;
    }
}
