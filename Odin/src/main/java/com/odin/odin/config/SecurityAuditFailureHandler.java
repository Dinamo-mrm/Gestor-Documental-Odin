package com.odin.odin.config;
import com.odin.odin.service.SecurityAuditService;
import jakarta.servlet.ServletException; import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException; import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import java.io.IOException;
public class SecurityAuditFailureHandler implements AuthenticationFailureHandler {
 private final SecurityAuditService audit; public SecurityAuditFailureHandler(SecurityAuditService audit){this.audit=audit;}
 public void onAuthenticationFailure(HttpServletRequest req,HttpServletResponse res,AuthenticationException ex)throws IOException,ServletException{
  audit.loginFallido(req.getParameter("username"),req,ex.getMessage()); res.sendRedirect(req.getContextPath()+"/login?error=true");
 }
}
