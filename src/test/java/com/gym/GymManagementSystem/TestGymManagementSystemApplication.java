package com.gym.GymManagementSystem;

import org.springframework.boot.SpringApplication;


public class TestGymManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(GymManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
