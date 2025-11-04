<?php
header("Content-Type: application/json; charset=UTF-8");
include 'conexion.php';

$res = $conn->query("SELECT * FROM contactos ORDER BY id DESC");
$data = [];
while ($r = $res->fetch_assoc()) {
    $r['video_url'] = $r['video_url'] ? "http://192.168.0.9/PM2ExamenAPI/" . $r['video_url'] : null;
    $data[] = $r;
}
echo json_encode($data);
$conn->close();
?>
