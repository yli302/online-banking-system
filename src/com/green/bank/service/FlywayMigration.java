package com.green.bank.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.flywaydb.core.Flyway;

@WebListener
public class FlywayMigration implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Flyway flyway = new Flyway();

	}
}
