<?php

include_once("modelos/usuario.php");

class controladorUsuario {
    private $persona;    

    public function __construct() {
        // Se mantiene la instancia de la clase Persona
        $this->persona = new Persona();
    }

    public function listar() {
        $res = $this->persona->listar();
        return $res;
    }

    public function registrar($cedula, $nombre, $apellidos, $direccion, $correo, $telefono, $password) {
    // Los nombres de los atributos aquí deben coincidir con los "private $" del modelo
    $this->persona->set("cedula", $cedula);
    $this->persona->set("nombre", $nombre);
    $this->persona->set("apellidos", $apellidos);
    $this->persona->set("direccion", $direccion);
    $this->persona->set("correo", $correo);
    $this->persona->set("telefono", $telefono);
    $this->persona->set("password", $password);

    return $this->persona->registrar();
    }

    public function ver ($usuario_id){
        $this->persona->set("id", $usuario_id);
        return $this->persona->ver();        
    }

       
    public function eliminar($id) {
        $this->persona->set("id", $usuario_id); 
        $this->persona->eliminar();
    }
} // fin de la clase controladorUsuario