package com.csf.api.rest.resources;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csf.api.rest.exception.RestException;
import com.csf.api.rest.transfer.TransferConverterUtil;
import com.csf.api.rest.transfer.model.StringTransfer;
import com.csf.api.rest.transfer.model.UserTransfer;
import com.csf.persistence.entity.User;
import com.csf.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

	@Autowired
	private UserService userService;

	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public UserTransfer getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			throw new RestException("Not Authorized");
		}
		UserDetails userDetails = (UserDetails) principal;

		return TransferConverterUtil.convertUserToTransfer((User) userDetails);
	}

	/**
	 * Retrieves a list of all users admins and normal users
	 * 
	 * @return A list of all users with their details
	 */
	@RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public List<UserTransfer> getAllUsers() {
		List<User> users = userService.findAll();

		return TransferConverterUtil.convertAllUsersToTransfer(users);
	}

	/**
	 * Creates a new user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public UserTransfer createUser(@RequestBody User userTransfer) {
		if (userTransfer.getIsAdmin() == null) {
			userTransfer.setIsAdmin(false);
		}
		if (userTransfer.getIsActive() == null) {
			userTransfer.setIsActive(false);
		}
		if (userTransfer.getIsAdvanced() == null) {
			userTransfer.setIsAdvanced(false);
		}

		User savedUser = userService.save(userTransfer, true);

		return TransferConverterUtil.convertUserToTransfer(savedUser);

	}

	@RequestMapping(path = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public UserTransfer changeUserRole(@PathVariable("id") Integer id) {
		User user = userService.find(id);
		if (user == null) {
			throw new RestException("User with given ID not found");
		}
		user.setIsAdmin(!user.getIsAdmin());
		User savedUser = userService.save(user, false);
		return TransferConverterUtil.convertUserToTransfer(savedUser);
	}

	@RequestMapping(path = "/changePassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public UserTransfer changeUserPassword(@RequestParam(name = "password", required = false) String password) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//TODO add password validation
		User savedUser = userService.changePassword(user, password);
		return TransferConverterUtil.convertUserToTransfer(savedUser);
	}

	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public UserTransfer updateUser(@RequestBody UserTransfer userTransfer) {
		if (userTransfer.getId() == null) {
			throw new RestException("ID required for update");
		}
		User user = userService.find(userTransfer.getId());
		if (user == null) {
			throw new RestException("User with given ID not found");
		}
		User savedUser = userService.update(userTransfer, user);
		return TransferConverterUtil.convertUserToTransfer(savedUser);
	}

	/**
	 * Delete user with provided ID.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public StringTransfer deleteUser(@PathVariable("id") Integer id) {
		userService.delete(id);

		return new StringTransfer("User deleted successfully");
	}

}
