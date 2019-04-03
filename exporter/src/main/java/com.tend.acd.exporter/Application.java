package com.tend.acd.exporter;

import com.tend.acd.exporter.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    RecordService recordService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
        Util.logger.trace("Job Completed");
    }

    @Override
    public void run(String... args) {
        try {
            String version = getClass().getPackage().getImplementationVersion();
            Util.logger.error("Running with app version {}", version);
            recordService.export();
        } catch (Exception e) {
            Util.logger.error("Job failed ", e);
        }
//        Scanner scanner = new Scanner(System.in);
//        Util.logger.trace("Please enter database host address, default is 127.0.0.1");
//        String dbConnectionString = scanner.nextLine();
//        if(dbConnectionString.equals(""))
//        {
//            dbConnectionString = "127.0.0.1";
//        }
//        Util.logger.trace("Please enter database host port, default is 5432");
//        Integer dbConnectionPort = scanner.nextInt();
//        if(dbConnectionPort.equals(0))
//        {
//            dbConnectionPort = 5432;
//        }
//        Util.logger.trace("Connecting to DB {}:{}",dbConnectionString,dbConnectionPort);
    }
}
