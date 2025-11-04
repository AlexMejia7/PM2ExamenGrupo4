<?php
header("Content-Type: application/json; charset=UTF-8");
include 'conexion.php';

$nombre = $_POST['nombre'] ?? '';
$lat = $_POST['lat'] ?? null;
$lng = $_POST['lng'] ?? null;

$uploadDir = "uploads/";
if (!is_dir($uploadDir)) mkdir($uploadDir, 0777, true);

$video_url = null;
if (isset($_FILES['video']) && $_FILES['video']['error'] == 0) {
    $vidName = time() . "_vid_" . basename($_FILES['video']['name']);
    $vidPath = $uploadDir . $vidName;
    move_uploaded_file($_FILES['video']['tmp_name'], $vidPath);
    $video_url = $vidPath;
}

$sql = "INSERT INTO contactos (nombre, lat, lng, video_url)
        VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sdds", $nombre, $lat, $lng, $video_url);

if ($stmt->execute()) {
    echo json_encode(["success" => true, "message" => "Registro insertado correctamente", "id" => $stmt->insert_id]);
} else {
    echo json_encode(["success" => false, "message" => $stmt->error]);
}
$stmt->close();
$conn->close();
?>
