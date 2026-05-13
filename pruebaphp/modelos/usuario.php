<?php
include_once("connection.php");

class Persona {
    private $id;
    private $cedula;
    private $nombre;
    private $apellidos;
    private $direccion;
    private $correo;
    private $telefono;
    private $password;
    private $con;

    public function __construct(){
        $this->con = new Conexion();
    }

    public function set($atributo, $valor){
        $this->$atributo = $valor;
    }

    public function registrar(){
        // Buscamos si la cédula ya existe usando el nombre de columna correcto
        $sql = "SELECT * FROM usuarios WHERE num_identificacion = '$this->cedula'";
        $respuesta = $this->con->consultaRetorno($sql);
        $filas = mysqli_num_rows($respuesta);

        if ($filas == 0){
            // SQL con los nombres de columnas que vimos en tu tabla de inicio
            $sql2 = "INSERT INTO usuarios (num_identificacion, nombre, apellidos, direccion, correo, telefono, clave, estado) 
                    VALUES ('$this->cedula', '$this->nombre', '$this->apellidos', '$this->direccion', '$this->correo', '$this->telefono', '$this->password', 'activo')";
            
            $this->con->consultaSimple($sql2);
            return true;
        } else {
            return false;
        }
    }

    public function listar (){
        $sql = "SELECT * FROM usuarios";
        return $this->con->consultaRetorno($sql);
    }

    public function ver(){
        $sql="SELECT * FROM usuarios WHERE id_usuario = $this->id";
        $resultado=$this->con->consultaRetorno($sql);
        $registro=mysqli_fetch_assoc($resultado);
        
        //$this->cedula=$registro["$cedula"];
        //$this->cedula=$registro["$nombre"];
        //$this->cedula=$registro["$apellidos"];
        //$this->cedula=$registro["$direccion"];
        //$this->cedula=$registro["$correo"];
        //$this->cedula=$registro["$telefono"];
        //$this->cedula=$registro["$password"];

        return $registro;
    }

    public function eliminar() {
        $sql = "DELETE FROM usuarios WHERE id_usuario = $this->id";       
        $this->con->consultaSimple($sql);
    }

    public function editar(){
        $sql = "UPDATE usuarios SET 
                num_identificacion = '{$this->cedula}',
                nombre = '{$this->nombre}',
                apellidos = '{$this->apellidos}',
                direccion = '{$this->direccion}',
                correo = '{$this->correo}',
                telefono = '{$this->telefono}',
                clave = '{$this->password}'
                WHERE id_usuario = {$this->id}";

        $this->con->consultaSimple($sql);
    }

    

    
} // fin de la clase persona

$miPersona=new Persona();