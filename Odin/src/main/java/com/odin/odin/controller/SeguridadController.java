package com.odin.odin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/seguridad")
public class SeguridadController {
 @GetMapping("/politicas")
 public ResponseEntity<Map<String,Object>> politicas(){
  return ResponseEntity.ok(Map.of("sesionMaximaConcurrente",1,"sessionFixationProtection",true,"passwordHash","BCrypt","csrf","Spring Security default policy","apiAuthentication","required"));
 }
}
