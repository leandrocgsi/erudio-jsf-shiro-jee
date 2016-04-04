package br.com.tecsinapse.servlet;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

//bug ao enviar multipart form
//http://ocpsoft.org/support/topic/command-not-fired-with-multipart-form-jsf-2-2/#post-26184
@WebServlet(name = io.undertow.servlet.handlers.ServletPathMatches.DEFAULT_SERVLET_NAME, urlPatterns = "/*")
@MultipartConfig
public class MultipartDefaultServlet extends io.undertow.servlet.handlers.DefaultServlet {
    private static final long serialVersionUID = 1L;
}