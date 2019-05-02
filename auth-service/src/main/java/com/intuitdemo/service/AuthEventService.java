package com.intuitdemo.service;

import com.intuitdemo.dao.AuthEventDao;
import com.intuitdemo.dao.UserDao;
import com.intuitdemo.model.AuthEventEntity;
import com.intuitdemo.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AuthEventService {
	private final Logger logger = LoggerFactory.getLogger(AuthEventService.class);
	
	@Autowired
	private AuthEventDao authEventDao;

	public void onSuccess(AuthenticationSuccessEvent authorizedEvent) {
		logger.info("User Oauth2 login success" + authorizedEvent.getAuthentication().getPrincipal());
		logEvent(authorizedEvent.getAuthentication().getPrincipal(), AuthEventEntity.ResultType.SUCCESS);
	}

	public void onFailure(AbstractAuthenticationFailureEvent oAuth2AuthenticationFailureEvent) {
		logger.info("User Oauth2 login Failed " + oAuth2AuthenticationFailureEvent.getAuthentication().getPrincipal());
		logEvent(oAuth2AuthenticationFailureEvent.getAuthentication().getPrincipal(), AuthEventEntity.ResultType.FAILURE);
	}

	private void logEvent(Object principal, AuthEventEntity.ResultType result) {
		try {
			AuthEventEntity authEvent = new AuthEventEntity();
			authEvent.setEventTime(new Date());
			authEvent.setResult(result);
			authEvent.setUsername(getUsername(principal));
			authEventDao.save(authEvent);
		}
		catch (Exception e) {
			// don't let exceptions bubble out to stop authentication
			logger.error("Unexpected error logging failure", e);
		}
	}

	private String getUsername(Object principal) {
		if (principal instanceof String)
			return (String) principal;
		else if (principal instanceof User)
			return ((User)principal).getUsername();
		throw new UnsupportedOperationException("Unknown principal type");
	}
}

