package com.healthylinkx;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.SecurityException;

@Configuration
public class DBDataSource {

    @Bean
	@Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() throws SQLException {
		//supporting docker containers. get the address of the mysql container
		//this works most of the time but it has issues
		//I need to move the constants to properties
		String SQLContainerID = "MySQLDB";
		try{
			InetAddress Address = InetAddress.getByName(SQLContainerID); 
			SQLContainerID = Address.getHostAddress();
		} catch (UnknownHostException e){
			SQLContainerID = "127.0.0.1";
		} catch (SecurityException e){
			SQLContainerID = "127.0.0.1";
		}	
		//SQLContainerID="172.17.0.2";
		
        return DataSourceBuilder
                .create()
                .url("jdbc:mysql://" + SQLContainerID + ":3306" + "/healthylinkx")
                .build();
    }
}

