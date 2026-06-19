package com.divyanshu.youtube.hospitalManagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/hospitalDB",
    "spring.datasource.username=postgres",
    "spring.datasource.password=divyanshu4321", // <-- IMPORTANT: SET YOUR PASSWORD HERE
    "spring.datasource.driver-class-name=org.postgresql.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect"
})
class HospitalManagementApplicationTests {

	@Test
	void contextLoads() {
	}

}