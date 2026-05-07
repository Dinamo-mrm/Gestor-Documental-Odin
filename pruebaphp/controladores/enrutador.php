<?php

class Enrutador{

public function cargarVista($vista){
    echo "la vista a cargado es $vista";

    switch($vista){
        case "crear":include_once("vistas/crear.php"); break;
        case "eliminar":include_once("vistas/eliminar.php"); break;
        case "editar":include_once("vistas/editar.php"); break;
        case "consultar":include_once("vistas/consultar.php"); break;
        default:include_once("vistas/error404.php"); break;
    }
} // fin del metodo de cargarVista

    public function validarVista($variable){
        if(empty($variable)){
            include_once("vistas/inicio.php");
        }
        else{
            return true;
            //include_once("vista/".$variable".php");
        }
    } // fin del metodo validarVista
} //fin de la clase enrutador