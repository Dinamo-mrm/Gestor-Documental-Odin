<?php

class Conexion{

    // atributos
    private $host;
    private $user;
    private $pass;
    private $dataBase;
    private $conexion;

    // metodos
    public function __construct(){
        $this->host = "localhost";
        $this->user = "root";
        $this->pass = "root";
        $this->dataBase = "odin";

        $this->con= mysqli_connect($this->host, $this->user, $this->pass, $this->dataBase);
        if (mysqli_error($this->con)){
            echo "falla la conexion a $this->dataBase";
        }else {
            echo "Conexion exitosa a $this->dataBase";
        }
    }

        public function consultaSimple($sql){
            mysqli_query($this->con,$sql);
        }// fin de la consulta simple

        public function consultaRetorno($sql){
            $retorno=mysqli_query($this->con,$sql);
            return $retorno;
        }// fin de la consulta simple

    // fin del metodo constructor
    

} // fin de la clase conexion 

//$miConexion = new Conexion();