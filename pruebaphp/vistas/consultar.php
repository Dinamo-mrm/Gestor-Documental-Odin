<H2>CONSULTAR USUARIO</H2>

<?php
    $controlador = new controladorUsuario();

if (isset($_GET["id"])){
    $reg=$controlador->ver($_GET["id"]);

    echo "<p>".$reg["num_identificacion"]."</p>";
    echo "<p>".$reg["nombre"]."</p>";
    echo "<p>".$reg["apellidos"]."</p>";
    echo "<p>".$reg["direccion"]."</p>";
    echo "<p>".$reg["telefono"]."</p>";
    echo "<p>".$reg["clave"]."</p>";
    }
?>    
