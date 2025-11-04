<?php
header("Content-Type: application/json; charset=UTF-8");
include 'conexion.php';

$id = intval($_POST['id'] ?? 0);
$nombre = $_POST['nombre'] ?? '';
$lat = $_POST['lat'] ?? null;
$lng = $_POST['lng'] ?? null;

if ($id > 0) {
    $sql = "UPDATE contactos SET 
            nombre=?, 
            lat=?, 
            lng=?
            WHERE id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sdsi", $nombre, $lat, $lng, $id);
    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Registro actualizado"]);
    } else {
        echo json_encode(["success" => false, "message" => $stmt->error]);
    }
    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "ID inválido"]);
}
$conn->close();
?>
