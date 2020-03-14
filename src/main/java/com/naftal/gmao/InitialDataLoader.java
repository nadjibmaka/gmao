package com.naftal.gmao;

import com.naftal.gmao.model.Administrateur;
import com.naftal.gmao.model.Role;
import com.naftal.gmao.model.RoleName;
import com.naftal.gmao.repository.AdminRepository;
import com.naftal.gmao.repository.RoleRepository;
import com.naftal.gmao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {


        if (userRepository.existsByUsername("admin")) {
            return;
        }
        Role role_admin = roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        Role role_intervenant = roleRepository.save(new Role(RoleName.ROLE_INTERVENANT));
        Role role_magasinier = roleRepository.save(new Role(RoleName.ROLE_MAGASINIER));
        Role role_cadre = roleRepository.save(new Role(RoleName.ROLE_CADRE));
        Role role_chefStation = roleRepository.save(new Role(RoleName.ROLE_CHEFSTATION));
        Role role_commercial = roleRepository.save(new Role(RoleName.ROLE_COMMERCIAL));

        Administrateur utilisateur = new  Administrateur();
        utilisateur.setMatricule("es2201151532003407");
        utilisateur.setPrenom("admin");
        utilisateur.setName("Admin");
        utilisateur.setUsername("admin");
        utilisateur.setNumTelephone("0659898496");
        utilisateur.setPassword(passwordEncoder.encode("1234567"));
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            java.util.Date date = simpleDateFormat.parse("12-01-2018");
            utilisateur.setDateNaissance( date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        utilisateur.setEmail("test@test.com");


        Set<Role> list = new HashSet<>();
        list.add(role_admin);
        utilisateur.setRoles(list);
        adminRepository.save(utilisateur);
    }


}