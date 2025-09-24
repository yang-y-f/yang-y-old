# 后端 API 约定

以下为客户端期望的后端接口格式，可按需调整：

## POST /recognize

- 请求：`multipart/form-data`
  - 字段：`image` 二进制 JPEG 文件
- 响应：`200 OK`
```json
{ "requestId": "abc123" }
```

## GET /result/{id}

- 请求：路径参数 `id` 为 `requestId`
- 响应：`200 OK`
```json
{ "result": "红烧肉, 青菜, 米饭" }
```

- 失败/未完成：
```json
{ "result": "Processing or failed" }
```

## 备注

- 建议在识别完成前返回“Processing”状态，客户端可定时刷新或轮询
- 可以扩展返回结构为明细数组：菜名、单价、营养信息等