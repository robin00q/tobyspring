package springbook.user.dao;

import springbook.user.domain.User;

import java.util.List;

public class TestUserService extends UserServiceImpl {
    private String id = "user4";

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }

    public List<User> getAll() {
        for (User user : super.getAll()) {
            super.update(user);
        }
        return null;
    }
}
