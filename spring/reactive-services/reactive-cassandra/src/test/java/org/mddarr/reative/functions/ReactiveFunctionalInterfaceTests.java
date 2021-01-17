package org.mddarr.reative.functions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


class ReactiveFunctionalInterfaceTests {

	@Test
	void contextLoads() {
		Flux<String> stringFlux =Flux.just("A","B","C");
		stringFlux.log().subscribe(data ->System.out.println(data));
	}


	@Test
	void testMono(){
		Mono<String> firstMono = Mono.just("Spring");
		firstMono.log().subscribe(data ->System.out.println(data));

	}


}
