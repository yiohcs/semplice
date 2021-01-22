package com.yschoi.demo.controller;

import com.yschoi.demo.model.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @Autowired
    TestDao dao;

    @GetMapping(value={""}, produces={"text/html"})
    public String index(HttpServletRequest req){
        System.out.println("root");
        return "index.html";
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest req){
        System.out.println("/home");
        return "index.html";
    }

    @RequestMapping("/test")
    public String test(HttpServletRequest req){
        System.out.println("/test");
        dao.testPrint();
        req.setAttribute("test","testPrint");
        return "/test/test.html";
    }
}
