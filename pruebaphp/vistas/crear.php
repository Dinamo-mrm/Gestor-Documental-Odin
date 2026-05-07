<h2>MODULO DE REGISTRO DE USUARIO</h2>

<form method="POST">
    <label for="tipo_documento">Tipo de Documento</label>
    <select name="tipo_documento" required>        
        <option value="CC">Cédula de Ciudadanía</option>
        <option value="TI">Tarjeta de Identidad</option>
        <option value="CE">Cédula de Extranjería</option>
    </select>
    
    <input type="number" name="cedula" placeholder="Cédula" required>
    <input type="text" name="nombre" required placeholder="Primer Nombre">
    <input type="text" name="apellidos" required placeholder="Primer Apellido">
    <input type="text" name="direccion" required placeholder="Dirección">
    <input type="email" name="correo" required placeholder="Email">
    <input type="text" name="telefono" placeholder="Teléfono (Solo números)">
    <input type="password" name="password" required placeholder="Contraseña">
    
    <label for="estado">Estado</label>
    <select name="estado" required>            
        <option value="activo">Activo</option>
        <option value="inactivo">Inactivo</option>            
    </select>
    
    <input type="submit" name="Guardar" value="registrar">
</form>

<?php
    $controlador = new controladorUsuario();
    if (isset($_POST["Guardar"])) {        
        $resultado = $controlador->registrar(
            $_POST["cedula"],
            $_POST["nombre"],
            $_POST["apellidos"],
            $_POST["direccion"],
            $_POST["correo"],
            $_POST["telefono"],
            $_POST["password"]
        );

        if ($resultado) {
            echo "<p style='color:green;'>Usuario registrado con éxito.</p>";
        } else {
            echo "<p style='color:red;'>Error: La cédula ya existe o hubo un problema con la base de datos.</p>";
        }
    }
?>