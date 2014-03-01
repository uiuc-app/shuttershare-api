package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.group.GroupVO;
import com.apollo.shuttershare.core.photo.PhotoVO;
import com.apollo.shuttershare.core.user.UserVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Daniel
 * Date: 2/10/14
 * Time: 4:46 PM
 */
public class GroupElements {
    public static class JsonGroup {
        public Long id;
        public String name;
        public List<JsonGroupMember> members;

        public JsonGroup(GroupVO group, List<UserVO> members) {
            this.id = group.getId();
            this.name = group.getName();
            this.members = new ArrayList<>();
            for (UserVO user : members) {
                this.members.add(new JsonGroupMember(user));
            }
        }
    }

    public static class JsonGroups {
        public List<JsonGroup> groups = new ArrayList<>();

        public JsonGroups(List<JsonGroup> groups) {
            this.groups = groups;
        }
    }

    public static class JsonGroupMember {
        public Long id;
        public String name;

        public JsonGroupMember(UserVO user) {
            this.id = user.getId();
            this.name = user.getName();
        }
    }
}
