<H2>ELIMINIAR USUARIO</H2>

<?php
$controlador = new controladorUsuario();

if (isset($_GET["id"])) {    
    $reg=$controlador->ver($_GET["id"]);
    }

    if (isset($_POST["Eliminar"])){
        $controlador->eliminar($_POST["id"]);
        header('location:index.php');
    }
?>

<form action="" method="POST">

    <input type="hidden" name="id_usuario" 
           value="<?php echo $reg['id_usuario']; ?>">
           
   <table border= 1>
        <thead>
            <tr>
                <th>Id</th>
                <th>tipo de documento</th>
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
        <tbody>
            <tr>
                <td><?php echo $reg["tipo de documento"]?></td>
                <td><?php echo $reg["Numero Cedula"]?></td>
                <td><?php echo $reg["Nombre"]?></td>
                <td><?php echo $reg["Apellidos"]?></td>
                <td><?php echo $reg["Direccion"]?></td>
                <td><?php echo $reg["Correo"]?></td>
                <td><?php echo $reg["Telefono"]?></td>
                <td><?php echo $reg["Estado"]?></td>

                <td>
                    <input type="submit" name="eliminar" value="eliminar">
                </td>
            </tr>    
        </tbody>
    </table>
</form>
