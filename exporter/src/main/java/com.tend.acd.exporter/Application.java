package com.tend.acd.exporter;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.exporter.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Value("${postgreSQL.connection}")
    String DBConnectionString;

    @Autowired
    RecordService recordService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Util.logger.trace("Job Completed");
    }

    @Bean("dbHelper")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public GenericDBHelper getDBHelper() throws ClassNotFoundException, SQLException, NamingException, IOException {
       Util.logger.warn("connect db {}", DBConnectionString);
       return new GenericDBHelper(DBConnectionString);
    }
    @Override
    public void run(String... args) {
        Util.logger.trace("Job Start");
        try {
            String version = getClass().getPackage().getImplementationVersion();
            Util.logger.error("Running with app version {}", version);
            if(args[0].equalsIgnoreCase("import"))
            {
                recordService.load();
            }
            if(args[0].equalsIgnoreCase("export"))
            {
                recordService.export();
            }
        } catch (Exception e) {
            Util.logger.error("Job failed", e);
        }
        finally {
            //getDBHelper().close();
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
