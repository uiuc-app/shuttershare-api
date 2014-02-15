package com.apollo.shuttershare.core.group;

import com.apollo.shuttershare.common.NotFoundException;
import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.photo.PhotoVO;
import com.apollo.shuttershare.core.photoentry.PhotoEntryService;
import com.apollo.shuttershare.core.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

    public void authenticateUserToGroups(Long[] groupIds, UserVO user) {
        log.debug("Authenticating user {} to groups {}", user, Arrays.toString(groupIds));
        if (groupIds == null || groupIds.length == 0) {
            throw new ShutterShareException("There are no groups to authenticate");
        } else {
            for (Long groupId : groupIds) {
                List<GroupMemberVO> groupMembers = groupMemberMapper.getListWithGroupIdAndUserId(groupId, user.getId());
                if (groupMembers.isEmpty() || groupMembers.size() > 1) {
                    log.debug("User {} is not authorized to access group with id {}", user, groupId);
                    throw new UnauthorizedException("User is not authorized to access one of the groups");
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
}
