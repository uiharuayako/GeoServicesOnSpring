# 基于开源框架高性能空间信息共享服务Web API设计实现

[中文](README.md) | [English](README_en.md)  

 地理服务的SpringBoot3.0实现  

 本仓库包含基于 Spring Boot 3.0 的空间信息共享服务系统的源代码。该系统旨在满足空间信息共享领域的需求，提供高效且稳定的数据查询、上传和共享功能。

## 特性

- 使用 Spring Boot 作为基础框架的服务端架构
- 结合文件系统和 PostGIS 数据库存储空间数据
- 使用 Redis 缓存技术优化 API 性能
- 利用 GeoTools 库进行矢量数据格式转换
- 容器化部署，简化部署过程并提高可移植性
- 访问和缓存 SHP 格式的矢量数据

## 安装与设置

1. 克隆仓库：  
```
git clone https://github.com/uiharuayako/GeoServicesOnSpring.git
cd GeoServicesOnSpring
```
2. 安装依赖：
```
./gradlew build
```
3. 使用 Docker 启动服务：
```
docker-compose up -d
```
4. 访问 API 文档，地址：`http://localhost:8080/swagger-ui.html`

## 使用方法

请参阅 API 文档了解如何与系统进行交互。

## 贡献

欢迎提供贡献！如有改进、错误修复或新功能，请随时提交问题或发起拉取请求。

## 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。