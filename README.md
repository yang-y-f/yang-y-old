# Cafeteria Recognizer (Android)

一款使用 CameraX 拍照并上传后端进行菜品识别的安卓应用，包含设置服务器地址、识别结果展示等功能。可在 Windows 的 Android Studio 直接构建、运行并烧录到手机。

## 运行步骤（Windows + Android Studio）

1. 使用 Android Studio 打开本项目根目录
2. 连接 Android 手机并开启开发者模式与 USB 调试
3. 在应用内的 设置 页面配置后端地址（如 `http://192.168.1.100:8000/`）
4. 在主界面等待“已检测到餐盘”提示后点击拍照，自动上传并跳转到结果页

默认后端地址：`http://10.0.2.2:8000/`（Android 模拟器访问宿主机）

## 功能
- CameraX 预览与拍照
- 简易托盘检测（占位逻辑，可替换为更精确算法）
- 通过 Retrofit 上传图片到后端 `/recognize`
- 拉取识别结果 `/result/{id}` 并展示
- DataStore 保存服务器地址

## 目标 API 与依赖
- minSdk 24，targetSdk 34
- Jetpack Compose + Material3
- CameraX, Retrofit, OkHttp, Moshi, DataStore

## 文档
- 见 `docs/architecture.md`、`docs/design.md`、`docs/api.md`
