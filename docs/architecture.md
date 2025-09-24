# System Architecture

```
+-------------+          HTTPS           +-------------------+
|  Android    |  ─────── POST /recognize ─▶ Dish Recognition |
|  App        |                           | Backend Service   |
+-------------+                           +-------------------+
       ▲                                             ▲
       |                                             |
       | Live preview &                             | Writes / Reads
       | tray detector                               |
       |                                             v
+-------------+                             +-------------------+
|  CameraX    |                             |  Database         |
+-------------+                             +-------------------+
```

## Components
1. **Android App (Edge Client)**
   - CameraX for live preview/tray detection.
   - Retrofit + OkHttp for networking.
   - ViewModel + LiveData for UI state.
   - Jetpack Compose UI.

2. **Dish Recognition Backend**
   - Python FastAPI server.
   - Model: CNN trained on canteen dishes.
   - Returns JSON with dish list, calories, price.

3. **Database**
   - PostgreSQL storing dish metadata (name, price, nutrition).

## Data Flow
1. Tray detected → capture image.
2. App uploads image to `/recognize`.
3. Backend processes image → predicts dishes → queries DB.
4. Backend responds JSON.
5. App displays results.

## Deployment
- Backend deployed on Kubernetes with GPU node.
- Load balancer exposes API over HTTPS.

## Security
- Auth via JWT (optional).
- HTTPS TLS 1.3.