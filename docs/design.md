# Detailed Design

## Android Module

### Packages
- `ui` – Compose screens (`MainScreen`, `ResultScreen`).
- `camera` – `TrayDetector`, `CameraManager`.
- `network` – `ApiService`, `NetworkModule`.
- `model` – data classes (`Dish`, `Result`).
- `viewmodel` – `MainViewModel`.

### Sequence
1. `MainScreen` observes `MainViewModel.uiState`.
2. When state = `TrayReady`, `CameraManager.capture()`.
3. Captured `File` emitted → `MainViewModel.uploadImage()`.
4. Retrofit call executes.
5. Response parsed into `Result`; state updated to `ShowResult`.
6. `ResultScreen` composable displays list.

### Tray Detection
- Simple heuristic: monitor luminosity & edges; frame diff.
- Future: ML Kit Object Detection.

### Error Handling
- Network timeout → Snackbar.
- No tray detected after 30s → hints overlay.

---

## Backend
### API Contract
```
POST /api/v1/recognize
Body: multipart/form-data (image)
Response 200: {
  "dishes": [
    { "name": "Mapo Tofu", "calories": 215, "price": 4.5 }
  ]
}
```
### Service Layers
- Router → `recognize(image)`
- `VisionService.predict(image)` returns labels.
- `DishService.enrich(labels)` queries DB.

### Performance
- Avg inference < 120 ms on RTX A4000.
- Throughput: 30 rps per GPU.

### Scaling
- Horizontal Pod Autoscaler based on GPU utilization.