# 系统架构说明

## 总览

- Android 客户端（本项目）：
  - 使用 Jetpack Compose 构建 UI
  - CameraX 进行相机预览、图像采集
  - 通过 Retrofit 上传图片至后端 `/recognize`
  - 获取识别结果 `/result/{id}` 并展示
  - DataStore 持久化服务器地址

- 后端（自备）：
  - 接收 `multipart/form-data` 图片，返回 `requestId`
  - 异步识别并以 `requestId` 为键存储结果
  - 提供 `GET /result/{id}` 查询识别结果

## 模块划分

- `ui`：Compose 界面（`CameraScreen`、`ResultsScreen`、`SettingsScreen`）
- `data`：网络层（`BackendApi`）
- `util`：通用工具（`AppPreferences`）
- 根入口：`MainActivity`、主题：`ui/theme`

## 数据流

1. `CameraScreen` 检测到托盘后允许拍照
2. 拍照回调中将图片字节通过 `BackendApi.uploadImage` 上传
3. 后端返回 `requestId`，跳转 `ResultsScreen`
4. `ResultsScreen` 通过 `BackendApi.fetchResult(requestId)` 拉取结果并展示

## 权限与安全

- 仅申请相机与网络权限
- 上传采用 `OkHttp`，可在 `BackendApi` 中配置 HTTPS 与认证头

## 可扩展性

- 托盘检测：当前为占位逻辑，可替换为 MLKit/自研算法
- 结果展示：可扩展为列表、营养成分、价格合计等
- 错误处理：可加入重试、超时、状态轮询等策略