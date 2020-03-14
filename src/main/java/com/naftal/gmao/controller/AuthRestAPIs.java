package com.naftal.gmao.controller;

import com.naftal.gmao.message.request.LoginForm;
import com.naftal.gmao.message.request.SignUpForm;

import com.naftal.gmao.message.response.JwtResponse;
import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.*;
import com.naftal.gmao.security.jwt.JwtProvider;
import com.naftal.gmao.repository.*;
import com.naftal.gmao.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

	@Autowired
    AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	ChefStationRepository chefStationRepository;

	@Autowired
	CadreRepository cadreRepository;

	@Autowired
	MagasinierRepository magasinierRepository;

	@Autowired
	CommercialRepository commercialRepository;

	@Autowired
	IntervenantRepository intervenantRepository;

	@Autowired
	StationRepository stationRepository;

	@Autowired
    PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

		Utilisateur user = userRepository.findByUsername(loginRequest.getUsername()).get();

		if (!user.isActivated()){
			return new ResponseEntity<>(new ResponseMessage("Fail -> utilisateur deactivé!"),
					HttpStatus.BAD_REQUEST);
		}

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
	}




	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}


		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Utilisateur Role not find."));
				roles.add(adminRole);

				Administrateur administrateur = new Administrateur();
				administrateur.setMatricule(signUpRequest.getMatricule());
				administrateur.setEmail(signUpRequest.getEmail());
				administrateur.setName(signUpRequest.getName());
				administrateur.setDateNaissance(signUpRequest.getDateNaissance());
				administrateur.setNumTelephone(signUpRequest.getNumTelephone());
				administrateur.setPrenom(signUpRequest.getPrenom());
				administrateur.setUsername(signUpRequest.getUsername());
				administrateur.setPassword(encoder.encode(signUpRequest.getPassword()));
				administrateur.setRoles(roles);

				adminRepository.save(administrateur);

				break;

				case "intervenant":
					Role intervenantRole = roleRepository.findByName(RoleName.ROLE_INTERVENANT)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Utilisateur Role not find."));
					roles.add(intervenantRole);

					Intervenant intervenant=new Intervenant();
					intervenant.setMatricule(signUpRequest.getMatricule());
					intervenant.setEmail(signUpRequest.getEmail());
					intervenant.setName(signUpRequest.getName());
					intervenant.setDateNaissance(signUpRequest.getDateNaissance());
					intervenant.setNumTelephone(signUpRequest.getNumTelephone());
					intervenant.setPrenom(signUpRequest.getPrenom());
					intervenant.setUsername(signUpRequest.getUsername());
					intervenant.setPassword(encoder.encode(signUpRequest.getPassword()));
					intervenant.setDisponible(true);
					intervenant.setNbHeurs(0);
					intervenant.setRoles(roles);

					Secteur secteur;
					switch (signUpRequest.getSecteur()){

						case "ghelizane":
							secteur=Secteur.Ghelizane;
							intervenant.setSecteur(secteur);
							break;

						case "tiaret":
							secteur=Secteur.Tiaret;
							intervenant.setSecteur(secteur);
							break;

						case "chlef":
							secteur=Secteur.Chlef;
							intervenant.setSecteur(secteur);
							break;




					}
					Specialite specialite;
					switch (signUpRequest.getSpecialite()){

						case "elec":
							specialite=Specialite.elec;
							intervenant.setSpecialite(specialite);
							break;

						case "mec":
							specialite=Specialite.mec;
							intervenant.setSpecialite(specialite);
							break;


					}

					System.out.println("iiiiiiiiiiinnnnnnnnnnt"+intervenant);
					intervenantRepository.save(intervenant);
					break;

			case "magasinier":
				Role pmRole = roleRepository.findByName(RoleName.ROLE_MAGASINIER)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Utilisateur Role not find."));
				roles.add(pmRole);
				Magasinier magasinier = new Magasinier();

				magasinier.setMatricule(signUpRequest.getMatricule());
				magasinier.setEmail(signUpRequest.getEmail());
				magasinier.setName(signUpRequest.getName());
				magasinier.setDateNaissance(signUpRequest.getDateNaissance());
				magasinier.setNumTelephone(signUpRequest.getNumTelephone());
				magasinier.setPrenom(signUpRequest.getPrenom());
				magasinier.setUsername(signUpRequest.getUsername());
				magasinier.setPassword(encoder.encode(signUpRequest.getPassword()));
				magasinier.setRoles(roles);

				magasinierRepository.save(magasinier);

				break;
				case "commercial":
					Role comRole = roleRepository.findByName(RoleName.ROLE_COMMERCIAL)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Utilisateur Role not find."));
					roles.add(comRole);
Commercial commercial = new Commercial();

					commercial.setMatricule(signUpRequest.getMatricule());
					commercial.setEmail(signUpRequest.getEmail());
					commercial.setName(signUpRequest.getName());
					commercial.setDateNaissance(signUpRequest.getDateNaissance());
					commercial.setNumTelephone(signUpRequest.getNumTelephone());
					commercial.setPrenom(signUpRequest.getPrenom());
					commercial.setUsername(signUpRequest.getUsername());
					commercial.setPassword(encoder.encode(signUpRequest.getPassword()));
					commercial.setRoles(roles);

					commercialRepository.save(commercial);
					break;
				case "cadre":
					Role cdRole = roleRepository.findByName(RoleName.ROLE_CADRE)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Utilisateur Role not find."));
					roles.add(cdRole);
					Cadre cadre = new Cadre();

					cadre.setMatricule(signUpRequest.getMatricule());
					cadre.setEmail(signUpRequest.getEmail());
					cadre.setName(signUpRequest.getName());
					cadre.setDateNaissance(signUpRequest.getDateNaissance());
					cadre.setNumTelephone(signUpRequest.getNumTelephone());
					cadre.setPrenom(signUpRequest.getPrenom());
					cadre.setUsername(signUpRequest.getUsername());
					cadre.setPassword(encoder.encode(signUpRequest.getPassword()));
					cadre.setRoles(roles);

					cadreRepository.save(cadre);

					break;

			default:
				Role userRole = roleRepository.findByName(RoleName.ROLE_CHEFSTATION)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Utilisateur Role not find."));
				roles.add(userRole);

				Station station = stationRepository.findById(signUpRequest.getCodeStation()).get();

				ChefStation chefStation = new ChefStation();
				chefStation.setStation(station);
				chefStation.setMatricule(signUpRequest.getMatricule());
				chefStation.setEmail(signUpRequest.getEmail());
				chefStation.setName(signUpRequest.getName());
				chefStation.setDateNaissance(signUpRequest.getDateNaissance());
				chefStation.setNumTelephone(signUpRequest.getNumTelephone());
				chefStation.setPrenom(signUpRequest.getPrenom());
				chefStation.setUsername(signUpRequest.getUsername());
				chefStation.setPassword(encoder.encode(signUpRequest.getPassword()));
				chefStation.setRoles(roles);

				chefStationRepository.save(chefStation);

			}
		});


		return new ResponseEntity<>(new ResponseMessage("Utilisateur registered successfully!"), HttpStatus.OK);
	}
}