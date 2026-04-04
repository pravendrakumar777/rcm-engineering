package com.niiran.software.solutions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.niiran.software.solutions.repository")
@EntityScan(basePackages = "com.niiran.software.solutions.domain")
@OpenAPIDefinition(
        info = @Info(
                title = "RCM Engineering API",
                version = "v1",
                description = "REST API documentation"
        )
)
public class RcmEngineeringApplication {
	public static void main(String[] args) {
		SpringApplication.run(RcmEngineeringApplication.class, args);
        // swagger URL : http://localhost:8080/swagger-ui.html
	}

    @Autowired
    private Environment env;

    @PostConstruct
    public void checkProfile() {
        System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
    }

    static class Example {
        public static void main(String[] args) {

            List<String> names = Arrays.asList("java","spring","hibernate");

            List<String> result = names.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            System.out.println("============ To upper case ============");
            System.out.println(result);
            System.out.println();
            System.out.println();

            System.out.println("============ find highest number ============");
            List<Integer> numbers = Arrays.asList(10,20,30,15);
            int max = numbers.stream()
                    .max(Integer::compare)
                    .get();
            System.out.println("Max Number : " + max);
            System.out.println();
            System.out.println();


            System.out.println("============ second highest number ============");
            List<Integer> number = Arrays.asList(10,40,20,30,50);
            int secondHighest = number.stream()
                    .sorted(Comparator.reverseOrder())
                    .skip(1)
                    .findFirst()
                    .get();
            System.out.println("Second Highest : " + secondHighest);
            System.out.println();
            System.out.println();


            System.out.println("============ count occurrence ============");
            String str = "java";
            Map<Character, Long> map = str.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
            System.out.println("occurrence : " + map);
            System.out.println();
            System.out.println();


            System.out.println("============ find even numbers ============");
            List<Integer> num = Arrays.asList(1,2,3,4,5,6,7,8);

            List<Integer> even = num.stream()
                    .filter(n -> n % 2 == 0)
                    .collect(Collectors.toList());
            System.out.println("Even Numbers : " + even);
            System.out.println();
            System.out.println();


            System.out.println("============ remove duplicate numbers ============");
            List<Integer> list = Arrays.asList(1,2,3,2,4,5,3);

            List<Integer> unique = list.stream()
                    .distinct()
                    .collect(Collectors.toList());
            System.out.println(unique);
            System.out.println();
            System.out.println();


            System.out.println("============ Non-Repeated Character ============");
            String st = "java stream";

            Character res = st.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .get();
            System.out.println(res);
            System.out.println();
            System.out.println();


            System.out.println("============ Ascending Order ============");
            List<Integer> nms = Arrays.asList(5,3,8,1);

            List<Integer> nm = nms.stream()
                    .sorted().collect(Collectors.toList());
            System.out.println(nm);
            System.out.println();
            System.out.println();



            System.out.println("============ Convert List to Map ============");
            List<String> chList = Arrays.asList("A","B","C");

            Map<String,Integer> mp = chList.stream()
                    .collect(Collectors.toMap(s -> s, s -> s.length()));
            System.out.println(mp);
            System.out.println();
            System.out.println();


            System.out.println("============ sum of All Numbers ============");
            List<Integer> numb = Arrays.asList(1,2,3,4,5);

            int sum = numb.stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            System.out.println(sum);
            System.out.println();
            System.out.println();



            // 1. Find Second highest
            int arr[] = {10,20,30,50,40};
            //int numb  = Arrays.stream(arr).sorted(Comparator.reverseOrder()).skip(1).findfirst(1).get();

            //2. Frequency of each Character
            String string = "programming";
            //Map<Character,Long> result = str.char().mapToObj(c -> (char) c).collect(IdentityFunction,Collectors.counting());

            //3. Find duplicate characters
            String stri = "programming";
            //List<Character> output =str.char().mapToObj(c -> (char) c).collect(identityFunction,Collectors.counting()).entryset().stream().filter(c -> c.getValue > 1).map(Map::getkeySet).collect(Collectors.toList());
        }
    }
}
