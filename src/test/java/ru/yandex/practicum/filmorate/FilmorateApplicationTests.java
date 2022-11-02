package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpClient;

@SpringBootTest
class FilmorateApplicationTests {
//	Проверьте, что валидация не пропускает пустые или неверно заполненные поля.
//		Посмотрите, как контроллер реагирует на пустой запрос.\


	@Test
	void contextLoads() {
		HttpClient httpClient = HttpClient.newBuilder().build();
	}
}
