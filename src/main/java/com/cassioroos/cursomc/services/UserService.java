package com.cassioroos.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.cassioroos.cursomc.domain.enums.Perfil;
import com.cassioroos.cursomc.security.UserSS;
import com.cassioroos.cursomc.services.exceptions.AuthorizationException;

public class UserService {
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

	public static UserSS authenticatedUser() {
		UserSS user = authenticated();

		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		return user;
	}

	public static UserSS authenticatedEmailUser(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !user.getUsername().equals(email)) {
			throw new AuthorizationException("Acesso negado!");
		}
		return user;
	}
}
