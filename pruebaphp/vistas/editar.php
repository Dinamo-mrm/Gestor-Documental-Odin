<h2>MODIFICAR USUARIO</h2>

<?php
    $controlador = new controladorUsuario();

    
    if (isset($_POST["id"])) {        
        $controlador->editar($_POST);
        
        echo "Datos actualizados con éxito";
    } 
   
    
?>