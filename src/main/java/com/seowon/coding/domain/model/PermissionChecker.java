package com.seowon.coding.domain.model;


import lombok.Builder;

import java.util.List;

class PermissionChecker {

    /**
     * TODO #7: 코드를 최적화하세요
     * 테스트 코드`PermissionCheckerTest`를 활용하시면 작업 결과를 검증 할 수 있습니다.
     */
    public static boolean hasPermission(
            String userId,
            String targetResource,
            String targetAction,
            List<User> users,
            List<UserGroup> groups,
            List<Policy> policies
    ) {
        User user = findUser(users, userId);
        List<UserGroup> userGroups = findUserGroups(groups, user);

        List<Policy> matchPolices = policies.stream().
                filter(policy -> policy.hasMatchStatement(targetAction, targetResource))
                .toList();

        for(UserGroup userGroup : userGroups){
            List<String> policyIds = userGroup.policyIds;
            for(String policyId : policyIds){
                return matchPolices.stream().map(policy -> policy.id)
                        .anyMatch(id->id.equals(policyId));
            }

        }

        return false;
    }

    private static List<UserGroup> findUserGroups(List<UserGroup> groups, User user) {
        return groups.stream()
                .filter(group -> user.groupIds.contains(group.id))
                .toList();
    }

    private static User findUser(List<User> users, String userId) {
        return users.stream()
                .filter(user -> user.id.equals(userId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
    }

}

class User {
    String id;
    List<String> groupIds;

    public User(String id, List<String> groupIds) {
        this.id = id;
        this.groupIds = groupIds;
    }
}

class UserGroup {
    String id;
    List<String> policyIds;

    public UserGroup(String id, List<String> policyIds) {
        this.id = id;
        this.policyIds = policyIds;
    }
}

class Policy {
    String id;
    List<Statement> statements;

    public Policy(String id, List<Statement> statements) {
        this.id = id;
        this.statements = statements;
    }

    public boolean hasMatchStatement(String targetAction, String targetResource){
        return statements.stream()
                .anyMatch(statement -> statement.isMatchStatement(targetAction,targetResource));
    }
}

class Statement {
    List<String> actions;
    List<String> resources;

    @Builder
    public Statement(List<String> actions, List<String> resources) {
        this.actions = actions;
        this.resources = resources;
    }

    public boolean isMatchStatement(String targetAction, String targetResource){
        return actions.contains(targetAction) &&
                resources.contains(targetResource);
    }
}