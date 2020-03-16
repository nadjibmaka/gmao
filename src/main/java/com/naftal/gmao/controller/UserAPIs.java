package com.naftal.gmao.controller;

import com.naftal.gmao.message.request.EditForm;
import com.naftal.gmao.message.request.SignUpForm;
import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.*;
import com.naftal.gmao.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserAPIs {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ChefStationRepository chefStationRepository;

    @Autowired
    CadreRepository cadreRepository;

    @Autowired
    MagasinierRepository magasinierRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    IntervenantRepository intervenantRepository;




    @GetMapping("/api/test/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Utilisateur> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/Intervenants")
    public List<Intervenant> getIntervenants() {
        return intervenantRepository.findAll();
    }



    @GetMapping("/api/test/user/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public Utilisateur getUtilisateurbyMatriculeUser(@PathVariable String matricule)

    {
//        System.out.println("get user called");
        return userRepository.findByMatricule(matricule);
    }

    @GetMapping("/User/{username}")
    public Utilisateur getUserbyUsername(@PathVariable String username){
        return userRepository.findByUsername(username).get();
    }


    @GetMapping("/api/test/users/desactivate/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>  desactivateUser(@PathVariable String matricule) {
        Utilisateur utilisateur = userRepository.findByMatricule(matricule);
        utilisateur.setActivated(false);
        userRepository.save(utilisateur);

        return new ResponseEntity<>(new ResponseMessage("Utilisateur desactivated successfully!"), HttpStatus.OK);
    }

    @GetMapping("/api/test/users/activate/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>  activateUser(@PathVariable String matricule) {
        Utilisateur utilisateur = userRepository.findByMatricule(matricule);
        utilisateur.setActivated(true);
        userRepository.save(utilisateur);

        return new ResponseEntity<>(new ResponseMessage("Utilisateur activated successfully!"), HttpStatus.OK);
    }


    @PutMapping("/users/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody EditForm editForm , @PathVariable String matricule) {


        System.out.println(editForm);
        Utilisateur user = userRepository.findByMatricule(matricule);

        for (Role r : user.getRoles()){

            if (r.getName() == RoleName.ROLE_CHEFSTATION){
                System.out.println("entered inside role station");
                System.out.println("new station code is "+editForm.getStation());
                ChefStation chefStation = chefStationRepository.findByMatricule(matricule);
                Station station = stationRepository.findByCodeStation(editForm.getStation());
                chefStation.setStation(station);
                chefStationRepository.save(chefStation);
            } else
            if (r.getName() == RoleName.ROLE_INTERVENANT){
                System.out.println("entered inside role intervenanat");

                Intervenant intervenant = intervenantRepository.findByMatricule(matricule);
                System.out.println(editForm.getSecteur());
                if (editForm.getSecteur().equals("Chlef")) intervenant.setSecteur(Secteur.Chlef);
                else if (editForm.getSecteur().equals("Ghelizane")) intervenant.setSecteur(Secteur.Ghelizane);
                else if (editForm.getSecteur().equals("Tiaret")) intervenant.setSecteur(Secteur.Tiaret);

                if (editForm.getSpecialite().equals("elec")) intervenant.setSpecialite(Specialite.elec);
                else if (editForm.getSpecialite().equals("mec")) intervenant.setSpecialite(Specialite.mec);


                intervenantRepository.save(intervenant);

            }

        }

        user.setEmail(editForm.getEmail());
        user.setName(editForm.getName());
        user.setDateNaissance(editForm.getDateNaissance());
        user.setNumTelephone(editForm.getNumTelephone());
        user.setPrenom(editForm.getPrenom());
        user.setUsername(editForm.getUsername());
        userRepository.save(user);



        return new ResponseEntity<>(new ResponseMessage("Utilisateur registered successfully!"), HttpStatus.OK);
    }




    @GetMapping("/api/test/users/SecteurIntervenant/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public String getSecteurOfIntervenant( @PathVariable String matricule) {
        return intervenantRepository.findByMatricule(matricule).getSecteur().name();
    }

    @GetMapping("/api/test/users/SpecialiteIntervenant/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public String getSpecialiteOfIntervenant( @PathVariable String matricule) {
        return intervenantRepository.findByMatricule(matricule).getSpecialite().name();
    }



    @PostMapping("/user/{username}/changerMdp")
    public ResponseEntity<?> updateUser(@Valid @RequestBody String password,@PathVariable String username){
        Utilisateur utilisateur = userRepository.findByUsername(username).get();
        utilisateur.setPassword(encoder.encode(password));
        userRepository.save(utilisateur);

        return new ResponseEntity<>(new ResponseMessage("Mot de passe changé avec succes"), HttpStatus.OK);
    }






}