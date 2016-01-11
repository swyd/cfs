package com.csf.api.rest.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.csf.api.rest.transfer.model.ErrorDetailTransfer;

@ControllerAdvice
public class RestExceptionMapper extends ResponseEntityExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ErrorDetailTransfer unauthorizedError(HttpServletRequest request, Exception exception) {
		ErrorDetailTransfer error = new ErrorDetailTransfer();
		logger.error("Exception occured: {}", exception);
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(exception.getLocalizedMessage());
		return error;
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ErrorDetailTransfer badUsernamPassword(HttpServletRequest request, Exception exception) {
		ErrorDetailTransfer error = new ErrorDetailTransfer();
		logger.error("Exception occured: {}", exception);
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(exception.getLocalizedMessage());
		return error;
	}
	
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ErrorDetailTransfer badCredentials(HttpServletRequest request, Exception exception) {
		ErrorDetailTransfer error = new ErrorDetailTransfer();
		logger.error("Exception occured: {}", exception);
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage("Korisnicko ime ili sifra su pogresni");
		return error;
	}
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ErrorDetailTransfer alreadyExistsError(HttpServletRequest request, Exception exception) {
		ErrorDetailTransfer error = new ErrorDetailTransfer();
		logger.error("Exception occured: {}", exception);
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("Korisnicko ime vec postoji, izaberite drugo.");
		return error;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorDetailTransfer myError(HttpServletRequest request, Exception exception) {
		ErrorDetailTransfer error = new ErrorDetailTransfer();
		logger.error("Exception occured: {}", exception);
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exception.getLocalizedMessage());
		return error;
	}
}