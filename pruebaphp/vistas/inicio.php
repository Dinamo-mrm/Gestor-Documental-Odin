<?php
    $controlador = new controladorUsuario();
    $resultado = $controlador->Listar();

    var_dump($resultado);
?>
<H2>LISTA DE USUARIOS</H2>

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
        </tr>
    </thead>
    <tbody>
        <?php
            while($fila=mysqli_fetch_assoc($resultado)){
                //var_dump($fila);
                echo "<tr>";
                    echo "<td>".$fila['id_usuario']."</td>";
                    echo "<td>".$fila['tipo_identificacion']."</td>";
                    echo "<td>".$fila['num_identificacion']."</td>";
                    echo "<td>".$fila['nombre']."</td>";
                    echo "<td>".$fila['apellidos']."</td>";
                    echo "<td>".$fila['direccion']."</td>";                    
                    echo "<td>".$fila['correo']."</td>";
                    echo "<td>".$fila['telefono']."</td>"; 
                    echo "<td>".$fila['clave']."</td>";                     
                    echo "<td>".$fila['estado']."</td>";                    
                    echo "<td>";
                        echo "<a href= '?cargar=consultar&id=".$fila['id_usuario']."'> consultar</a>";
                        echo "<a href= '?cargar=editar&id=".$fila['id_usuario']."'> editar</a>";
                        echo "<a href= '?cargar=eliminar&id=".$fila['id_usuario']."'> eliminar</a>";
                    echo "</td>";    
                echo "</tr>";
        }
        ?>
        <tr>
            <td>1</td>
            <td>755</td>
            <td>Carlos</td>
            <td>Barahona</td>
            <td>carlx</td>
            <td>13246548</td>
            <td>
                <a href= "#"> "consultar"</a>
                <a href= "#"> "editar"</a>
                <a href= "#"> "eliminar"</a>
        </tr>
    </tbody>
</table>