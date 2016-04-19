package com.woniu.demo.admin.user.service;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.woniu.base.db.Pagination;
import com.woniu.base.db.Query;
import com.woniu.base.lang.Strings;
import com.woniu.base.service.EntityService;
import com.woniu.base.util.Utils;
import com.woniu.base.web.QueryForm;
import com.woniu.demo.admin.user.entity.User;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends EntityService<User, Long> {
	private Logger logger = LoggerFactory.getLogger(UserService.class);

	private HashFunction sha1 = Hashing.sha1();

	public UserService() {
		super(User.class);
	}

	public Pagination<User> search(QueryForm qf) {
		Query q = db.from("user");
		qf.setupLikeConditions(q, "login_name", "name", "email");
		return q.orderBy(qf.getOrderBy()).paginate(User.class, qf.getPage());
	}

	public User findByLoginName(String loginName){
		if(Strings.isBlank(loginName)){
			return null;
		}
		return db.from(User.class).where("login_name", loginName).first(User.class);
	}

	public User findByToken(String token){
		if(Strings.isBlank(token)){
			return null;
		}
		return db.from(User.class).where("token", token).first(User.class);
	}

	public User load(Long id) {
		return find(id);
	}

    private String encodePassword(String password, String salt) {
        return sha1.hashString(password + "#" + salt, Charsets.UTF_8).toString();
    }

    private String createSalt(String name) {
        return sha1.hashString(name + "#" + System.currentTimeMillis() + "#" + RandomStringUtils.random(20), Charsets.UTF_8).toString();
    }

	@Transactional
	public User create(User user, Long currentUserId) {
		String salt = createSalt(user.getLoginName());
		String loginPassword = encodePassword(user.getLoginPassword(), salt);
		user.setSalt(salt);
		user.setLoginPassword(loginPassword);

		user.setToken(Utils.generateToken(user.getLoginName()));

		user.setCreatorId(currentUserId);
		user.setCreateDate(DateTime.now());
		db.insert(user);
		logger.info("created a new user, loginName=" + user.getLoginName() + ", id="
				+ user.getId());
		return user;
	}

	@Transactional
	public User changePassword(Long id, String plainPassword, Long currentUserId) {
		User user = db.find(User.class, id);
		String loginPassword = encodePassword(plainPassword,
                user.getSalt());
		user.setLoginPassword(loginPassword);

		user.setUpdateDate(DateTime.now());
		user.setModifierId(currentUserId);
		db.update(user);
		return user;
	}

	public User login(String loginName, String password) {
		User user = db.from("user").where("login_name", loginName).first(User.class);
		if (user == null) {
			return null;
		}
		String loginPassword = encodePassword(password,
                user.getSalt());
		if (loginPassword.equals(user.getLoginPassword())) {
			return user;
		}
		return null;
	}

	public boolean isValidPassword(User user, String password) {
		String loginPassword = encodePassword(password,
				user.getSalt());
		return loginPassword.equals(user.getLoginPassword());
	}
}
