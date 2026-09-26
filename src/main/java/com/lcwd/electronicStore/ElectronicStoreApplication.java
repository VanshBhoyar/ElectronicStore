package com.lcwd.electronicStore;

import com.lcwd.electronicStore.entities.Role;
import com.lcwd.electronicStore.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Value("${admin.role.id}")
	private String adminRoleId;

	@Value("${normal.role.id}")
	private String normalRoleId;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("abcd"));

		try{

			Role roleAdmin = Role.builder().roleId(adminRoleId).roleName("ADMIN").build();
			Role roleNormal = Role.builder().roleId(normalRoleId).roleName("NORMAL").build();
			roleRepository.save(roleAdmin);
			roleRepository.save(roleNormal);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
