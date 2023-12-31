package com.medicare;

import com.medicare.models.Cart;
import com.medicare.models.Role;
import com.medicare.models.User;
import com.medicare.models.UserRole;
import com.medicare.repositories.CartRepository;
import com.medicare.repositories.UserRepository;
import com.medicare.services.CartService;
import com.medicare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MedicareBackendApplication implements CommandLineRunner {
	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartService cartService;
	public static void main(String[] args) {
		SpringApplication.run(MedicareBackendApplication.class, args);
	}

	@Override
	public void run(String... args)  {

		User user = new User();
		user.setUsername("admin");
		user.setUser_email("cmallick88@gmail.com");
		user.setPassword(passwordEncoder.encode("admin123"));
		user.setEnabled(true);
		user.setFirst_name("Chandan");
		user.setLast_name("Mallick");
		user.setUser_phone("7008045937");




		Role adminRole = new Role();
		adminRole.setRole_name("ADMIN");


		Set<UserRole> userRoles = new HashSet<>();

		UserRole userRole = new UserRole();
		userRole.setRole(adminRole);
		userRole.setUser(user);

		userRoles.add(userRole);

		user.setUserRoles(userRoles);

		User local = userRepository.findByUsername(user.getUsername());
		if(local != null){
			System.err.println("User Already Exists");
			try {
				throw new Exception("User Already Exist");
			} catch (Exception e) {
				System.err.println("User Already Exist");
			}
		}
		else {

			try {


			User user1=userService.createUser(user, userRoles);

			Cart cart = new Cart();
			cart.setUser(user1);
			cartService.createCart(cart);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}
}
