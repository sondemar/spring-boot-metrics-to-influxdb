package sample.metrics.influx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"sample.metrics.influx",
        "org.springframework.boot.actuate.autoconfigure.metrics.export.influx"})
public class InfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfluxApplication.class, args);
    }

}
