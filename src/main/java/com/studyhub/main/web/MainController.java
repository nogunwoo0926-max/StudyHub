package com.studyhub.main.web;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    private final DataSource dataSource;

    public MainController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/main.do")
    public String main() {
        return "main/main";
    }

    @ResponseBody
    @GetMapping(value = "/dbTest.do", produces = "text/plain; charset=UTF-8")
    public String dbTest() {

        try (Connection conn = dataSource.getConnection()) {

            return "DB 연결 성공 : "
                    + conn.getMetaData().getDatabaseProductName();

        } catch (Exception e) {

            e.printStackTrace();
            return "DB 연결 실패 : " + e.getMessage();
        }
    }
}