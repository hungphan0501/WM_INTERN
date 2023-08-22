package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TestMarkerController {
	@Autowired
	UserRepository userRepository;

	@RequestMapping("/")
	public String index(@RequestParam(name = "name", required = false) String name, Model model) {
		if (name == null) {
			name = "Anonymous";
		}
		String welcomeMessage = "Hi " + name + "! Welcome to SpringBoot Freemaker";
		model.addAttribute("message", welcomeMessage);
		return "index";
	}

	@GetMapping("/users")
	public String getUsers(Model model) {
		List<User> users = userRepository.findAll();

		model.addAttribute("users", users);
		return "users-template";
	}
}
