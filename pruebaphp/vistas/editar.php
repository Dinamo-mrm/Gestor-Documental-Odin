<h2>MODIFICAR USUARIO</h2>

<?php
$controlador = new controladorUsuario();

if (isset($_GET["id"])) {
    $registro = $controlador->ver($_GET["id"]); 
}

if (isset($_POST["editar"])) {
    $controlador->procesarEdicion($_POST);
    header('location:index.php'); // Redirigir al finalizar
}
?>

<form action="" method="POST">

    <input type="hidden" name="id_usuario" value="<?php echo $registro['id_usuario']; ?>">

    <table border="1">
        <thead>
            <tr>                
                <th>Tipo de Documento</th>
                <th>Numero Cedula</th>
                <th>Nombre</th>
                <th>Apellidos</th>
                <th>Direccion</th>
                <th>Correo</th>
                <th>Telefono</th>
                <th>Password</th>
                <th>Estado</th>
                <th>Accion</th>        
            </tr>
        </thead>

        <tr>            
            <td>
                <input type="text" name="tipo_identificacion"
                value="<?php echo $registro['tipo_identificacion']; ?>">
            </td>               
            <td>
                <input type="text" name="num_identificacion"
                value="<?php echo $registro['num_identificacion']; ?>"disabled>
            </td>                  
            <td>
                <input type="text" name="nombre"
                value="<?php echo $registro['nombre']; ?>">
            </td>                   
            <td>
                <input type="text" name="apellidos"
                value="<?php echo $registro['apellidos']; ?>">
            </td>                  
            <td>
                <input type="text" name="direccion"
                value="<?php echo $registro['direccion']; ?>">
            </td>                   
            <td>
                <input type="email" name="correo"
                value="<?php echo $registro['correo']; ?>">
            </td>                  
            <td>
                <input type="text" name="telefono"
                value="<?php echo $registro['telefono']; ?>">
            </td>   
            <td>
                <input type="password" name="clave"
                value="<?php echo $registro['clave']; ?>">
            </td>                   
            <td>
                <input type="text" name="estado"
                value="<?php echo $registro['estado']; ?>">
            </td>        
            <td colspan="2">
                <input type="submit" name="editar" value="Guardar Cambios">
            </td>
        </tr>

    </table>

</form>