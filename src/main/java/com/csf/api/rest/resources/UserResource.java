package com.csf.api.rest.resources;

import java.util.List;
import java.util.Map;

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
	@PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_COACH')")
	public List<UserTransfer> getAllUsers() {
		List<User> users = userService.findAll();
		return TransferConverterUtil.convertAllUsersToTransfer(users);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(path = "/coaches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_COACH')")
	public Map<Integer, String> getCoaches() {
		List<User> coaches = userService.findAllCoaches();
		return TransferConverterUtil.convertCoachesToMap(coaches);
	}

	/**
	 * Creates a new user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_COACH')")
	public UserTransfer createUser(@RequestBody UserTransfer user) {
		User coach = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getCoachId() != null) {
			coach = userService.find(user.getCoachId());
		}
		if (coach == null) {
			throw new RestException("Ne postoji izabrani trener");
		}
		User savedUser = userService.createUser(TransferConverterUtil.convertUserTransferToUser(user, coach));

		return TransferConverterUtil.convertUserToTransfer(savedUser);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_COACH')")
	public UserTransfer changeUserRole(@PathVariable("id") Integer id, Integer userRole) {
		User user = userService.changeUserRole(id, userRole);
		return TransferConverterUtil.convertUserToTransfer(user);
	}

	@RequestMapping(path = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "isAuthenticated()")
	public UserTransfer changeUserPassword(@RequestParam("newPass") String newPassword,
			@RequestParam("oldPass") String oldPassword) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// TODO add password validation
		User savedUser = userService.changePassword(user, oldPassword, newPassword);
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
		User savedUser = userService.update(userTransfer);
		return TransferConverterUtil.convertUserToTransfer(savedUser);
	}

	/**
	 * Delete user with provided ID.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_COACH')")
	public StringTransfer deleteUser(@PathVariable("id") Integer id) {
		userService.delete(id);

		return new StringTransfer("User deleted successfully");
	}

}
