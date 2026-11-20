package com.gymmanagement.GymManagementSystem;

import org.springframework.boot.SpringApplication;

import com.gym.GymManagementSystem.GymManagementSystemApplication;

public class TestGymManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(GymManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
