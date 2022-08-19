package com.tokenbid.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    /**
     * Redirects to the index.html file.
     * 
     * @param httpResponse - response to the client
     * @throws IOException - if there is an error
     */
    @GetMapping("/")
    public void index(HttpServletResponse httpResponse) throws IOException {
        httpResponse.sendRedirect("/index.html");
    }
}
