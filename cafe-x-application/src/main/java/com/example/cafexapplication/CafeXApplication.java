package com.example.cafexapplication;

import com.example.cafexapplication.config.ServiceConfiguration;
import com.example.cafexapplication.model.MenuItem;
import com.example.cafexapplication.repository.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.example.cafexapplication.model.Category.BEVERAGE;
import static com.example.cafexapplication.model.Category.FOOD;
import static com.example.cafexapplication.model.Preparation.COLD;
import static com.example.cafexapplication.model.Preparation.HOT;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({ServiceConfiguration.class})
public class CafeXApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeXApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(MenuItemRepository repository) {
		return args -> {
			repository.saveAll(List.of(
					new MenuItem("Cola", ".50", COLD, BEVERAGE),
					new MenuItem("Coffee", "1.00", HOT, BEVERAGE),
					new MenuItem("Cheese Sandwich", "2.00", COLD, FOOD),
					new MenuItem("Steak Sandwich", "4.50", HOT, FOOD)
			));
		};
	}
}
