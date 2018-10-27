package com.ankamma.user.application.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ankamma.user.application.feign.UserClient;
import com.ankamma.user.application.model.RoleList;
import com.ankamma.user.application.model.UserExit;
import com.ankamma.user.application.model.UserList;
import com.ankamma.user.application.model.UserRequest;
import com.ankamma.user.application.model.UserResponse;
@Component
public class UserClientFallBack implements UserClient {

	@Override
	public UserResponse createUser(UserRequest UserRequest) {
		UserResponse response=new UserResponse();
		response.setId(UserRequest.getId());
		return response;
	}

	@Override
	public UserExit checkUserExit(String username) {
		UserExit exit=new UserExit();
		exit.setAvailable(false);
		return exit;
	}

	@Override
	public UserExit checkEmailExit(String email) {
		UserExit exit=new UserExit();
		exit.setAvailable(false);
		return exit;
	}

	@Override
	public UserList getUserNames(String username) {
		UserList list=new UserList();
		list.setName(username);
		list.setEmail("ankamma.java@gmail.com");
		list.setId(12l);
		list.setPassword("ankammma");
		list.setRoleList(setRoleList());
		return list;
	}
	private List<RoleList> setRoleList() {
		List<RoleList> list = new ArrayList<>();
		RoleList roleList = new RoleList();
		roleList.setId(13l);
		roleList.setName("admin");
		list.add(roleList);
		return list;
	}

	@Override
	public List<UserList> getUserList() {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

}
