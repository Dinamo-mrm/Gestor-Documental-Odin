package com.odin.odin.config;
import com.odin.odin.service.OdinUserDetails;
import com.odin.odin.service.SecurityAuditService;
import jakarta.servlet.ServletException; import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication; import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
public class SecurityAuditSuccessHandler implements AuthenticationSuccessHandler {
 private final SecurityAuditService audit; public SecurityAuditSuccessHandler(SecurityAuditService audit){this.audit=audit;}
 public void onAuthenticationSuccess(HttpServletRequest req,HttpServletResponse res,Authentication auth)throws IOException,ServletException{
  if(auth.getPrincipal() instanceof OdinUserDetails u) audit.loginExitoso(u,req); res.sendRedirect(req.getContextPath()+"/view/dashboard");
 }
}
