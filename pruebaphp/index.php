<?php
    include_once("controladores/enrutador.php");
    include_once("controladores/controlador.php");
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>php index</title>
</head>
<body>
    <h1>ejercicio creacion de CRUD con php, MVC y POO</h1>

    <nav>
        <ul>
            <li><a href="index.php"> Inicio </a></li>
            <li><a href="?cargar=crear"> registrar </a></li>
        </ul>
    </nav>

    <?php

        $enrutador = new Enrutador();
        //echo "<h1>hola desde php</h1>";
        if (isset($_GET['cargar'])){
            $cargar=$_GET['cargar'];
        }else{
            $cargar='';
        }
        if($enrutador->validarVista($cargar)){
            $enrutador->cargarVista($cargar);
        };
    ?>


</body>
</html>