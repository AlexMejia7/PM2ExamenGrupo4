<?php
header("Content-Type: application/json; charset=UTF-8");

$host = "localhost";
$user = "root";
$pass = "";
$db = "pm2examen";

// Crear conexión
$conn = new mysqli($host, $user, $pass, $db);

// Verificar conexión
if ($conn->connect_error) {
    die(json_encode([
        "success" => false,
        "message" => "Error al conectar con la base de datos: " . $conn->connect_error
    ]));
}

// Establecer charset
$conn->set_charset("utf8");
?>
