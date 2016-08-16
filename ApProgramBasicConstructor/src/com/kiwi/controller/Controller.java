package com.kiwi.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.kiwi.service.Service;

public class Controller {

	@Autowired
	Service service;

	public String testProcess() {
		return service.testProcess();
	}

}
