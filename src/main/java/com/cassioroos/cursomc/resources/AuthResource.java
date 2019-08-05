package com.cassioroos.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cassioroos.cursomc.DTO.EmailDTO;
import com.cassioroos.cursomc.security.JWTUtil;
import com.cassioroos.cursomc.security.UserSS;
import com.cassioroos.cursomc.services.AuthService;
import com.cassioroos.cursomc.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthService authService;

	@PostMapping(value = "refresh-token")
	public ResponseEntity<Void> refrestToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "forgot")
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO obj) {
		authService.sendNewPassword(obj.getEmail());
		return ResponseEntity.noContent().build();
	}
}
