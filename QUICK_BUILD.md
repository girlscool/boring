# DocTool - 快速构建指南

## 🚀 一键构建方案

由于本地Android SDK安装耗时，提供以下快速构建方案：

### 方案一：使用在线构建服务（推荐）

#### 步骤：
1. **访问在线构建平台**：
   - [BuildAPK](https://buildapk.net)
   - [AppYet](https://www.appyet.com)
   - 或任何支持Android项目构建的在线服务

2. **上传项目文件**：
   - 将整个 `DocTool` 文件夹打包为ZIP
   - 上传到构建平台

3. **配置构建**：
   - 应用ID: `com.doctool`
   - 最低SDK: 24 (Android 7.0)
   - 目标SDK: 34 (Android 14)

4. **下载APK**：
   - 构建完成后下载APK文件
   - 直接安装到Android设备

### 方案二：使用GitHub Actions自动构建

#### 创建 `.github/workflows/build.yml`：
```yaml
name: Build Android APK

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Build with Gradle
      run: |
        cd DocTool
        chmod +x gradlew
        ./gradlew assembleDebug
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: DocTool-APK
        path: DocTool/app/build/outputs/apk/debug/app-debug.apk
```

### 方案三：使用简化构建脚本

#### 创建 `build_simple.sh`：
```bash
#!/bin/bash
echo "DocTool 简化构建脚本"

# 创建必要的目录
mkdir -p app/build/outputs/apk/debug

# 生成模拟APK（实际需要Android SDK）
echo "正在生成模拟APK..."

# 创建APK文件结构
mkdir -p temp_apk
cd temp_apk

# 创建Android清单
cat > AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest package="com.doctool">
    <application android:label="DocTool">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
EOF

# 打包为APK（模拟）
zip -r ../app-debug.apk .

echo "构建完成！"
echo "APK文件: app-debug.apk"
```

## 📱 功能验证

### 已实现的核心功能：
1. ✅ **参数编辑器** - 完整的UI界面
2. ✅ **模板系统** - 内存存储，支持保存/加载
3. ✅ **文档解析模拟** - 基于文件类型的智能识别
4. ✅ **主界面** - 三标签布局
5. ✅ **预设模板** - 5个常用模板

### 模拟功能：
- 文档格式识别（基于文件扩展名）
- 参数应用（模拟处理）
- 模板管理（内存存储）
- 文件操作（模拟转换/合并）

## 🔧 快速测试

### 无需安装的测试方法：
1. **使用Android模拟器**：
   ```bash
   # 启动Android模拟器
   emulator -avd Pixel_4_API_34
   
   # 安装APK
   adb install app-debug.apk
   ```

2. **使用在线模拟器**：
   - [BrowserStack](https://www.browserstack.com/app-live)
   - [LambdaTest](https://www.lambdatest.com/mobile-app-testing)
   - 上传APK进行在线测试

## 📦 项目结构说明

### 关键文件：
```
DocTool/
├── app/src/main/java/com/doctool/
│   ├── DocToolApp.kt              # 应用入口
│   ├── ui/screens/MainScreen.kt   # 主界面
│   ├── ui/components/ParamEditor.kt # 参数编辑器
│   ├── core/parser/impl/SimulatedParser.kt # 模拟解析器
│   └── template/impl/MemoryTemplateManager.kt # 内存模板管理
├── app/build.gradle.kts           # 构建配置
└── app/src/main/AndroidManifest.xml # 应用清单
```

### 依赖说明：
- 使用模拟实现，无需外部库
- 兼容Android 7.0+ (API 24)
- 使用Jetpack Compose UI

## ⚡ 生产环境升级

### 需要添加的实际依赖：
```kotlin
dependencies {
    // 实际文件处理
    implementation("org.apache.pdfbox:pdfbox:3.0.2")
    implementation("org.apache.poi:poi:5.2.5")
    
    // 实际数据库
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // 实际图像处理
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
```

## 🎯 部署流程

### 简易部署：
1. 使用在线服务构建APK
2. 下载APK文件
3. 通过USB/邮件发送到手机
4. 在手机上安装（需允许未知来源）

### 正式部署：
1. 生成签名密钥
2. 构建发布版本
3. 测试各Android版本
4. 上传到应用商店

## 📞 技术支持

### 构建问题：
- 检查Java版本（需要JDK 17+）
- 检查Android SDK路径
- 确保网络连接正常

### 功能问题：
- 模拟功能仅用于演示
- 实际文件处理需要添加PDFBox等库
- 数据库功能需要Room配置

## 🚨 注意事项

### 安全提示：
- 测试版本不要处理敏感文档
- 正式版本需要添加文件加密
- 注意存储权限管理

### 性能提示：
- 大文件处理需要优化内存
- 建议添加进度指示
- 考虑后台任务处理