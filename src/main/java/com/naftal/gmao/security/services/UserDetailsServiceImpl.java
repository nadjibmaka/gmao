package com.naftal.gmao.security.services;

import com.naftal.gmao.model.Utilisateur;
import com.naftal.gmao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {



		Utilisateur utilisateur = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("Utilisateur Not Found with -> username or email : " + username));

		if (! utilisateur.isActivated()) {new UsernameNotFoundException("Utilisateur with the username "+username+" is desactivated");}
		return UserPrinciple.build(utilisateur);
	}
}