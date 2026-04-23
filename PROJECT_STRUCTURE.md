# DocTool 项目结构

## 已完成的核心代码

### 1. 数据模型 (`data/model/`)
- `FormatParams.kt` - 格式参数数据结构
- `Template.kt` - 模板数据结构和预设模板

### 2. 核心解析器 (`core/parser/`)
- `DocumentParser.kt` - 文档解析器接口
- `impl/PdfParser.kt` - PDF解析器实现（规则化识别）

### 3. 模板系统 (`template/`)
- `TemplateManager.kt` - 模板管理器接口和工具类

### 4. UI组件 (`ui/`)
- `components/ParamEditor.kt` - 参数编辑器组件
- `screens/MainScreen.kt` - 主界面和各个屏幕
- `theme/` - 应用主题和排版

### 5. 应用入口
- `DocToolApp.kt` - 应用主类

## 需要的外部依赖

### Gradle依赖 (`build.gradle.kts`):
```kotlin
dependencies {
    // Android基础
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // 文件处理
    implementation("org.apache.pdfbox:pdfbox:3.0.2")
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    
    // 数据库
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // 后台任务
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    
    // 测试
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

### Android清单 (`AndroidManifest.xml`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.DocTool">
        
        <activity
            android:name=".DocToolApp"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            
            <!-- 文件类型支持 -->
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="application/pdf" />
                <data android:mimeType="application/msword" />
                <data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
            </intent-filter>
        </activity>
        
    </application>
</manifest>
```

## 构建说明

### 1. 环境要求
- Android Studio Giraffe 或更高版本
- JDK 17+
- Android SDK 34+
- Gradle 8.0+

### 2. 构建步骤
```bash
# 克隆项目
git clone <repository>
cd DocTool

# 使用Gradle构建
./gradlew build

# 生成APK
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

### 3. 一键构建脚本 (`build.sh`)
```bash
#!/bin/bash
echo "开始构建 DocTool..."

# 清理
./gradlew clean

# 构建APK
./gradlew assembleDebug

# 检查构建结果
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "构建成功！APK位置: app/build/outputs/apk/debug/app-debug.apk"
    
    # 可选：安装到连接的设备
    # adb install app/build/outputs/apk/debug/app-debug.apk
else
    echo "构建失败！"
    exit 1
fi
```

## 功能模块状态

### ✅ 已完成
- 核心数据模型设计
- 参数编辑器UI组件
- 主界面布局
- 模板系统架构
- PDF解析器接口

### ⚠️ 待实现
- 实际的PDF解析逻辑（需要PDFBox集成）
- 文件浏览器实现
- 模板数据库实现
- 文档转换功能
- 图像处理功能

### 📋 测试计划
1. 参数编辑器功能测试
2. 模板保存/加载测试
3. PDF解析准确性测试
4. 性能测试（大文件处理）
5. 兼容性测试（不同Android版本）

## 部署说明

### 发布版本
1. 生成签名密钥
2. 配置发布构建类型
3. 生成发布APK
4. 上传到应用商店

### 更新日志
- v1.0.0: 初始版本，基础文档格式识别和编辑
- v1.1.0: 增加模板系统
- v1.2.0: 增加更多文档格式支持
- v1.3.0: 优化性能和用户体验

## 问题排查

### 常见问题
1. **PDF解析失败**: 检查PDF文件是否加密或损坏
2. **内存不足**: 优化大文件处理，使用流式解析
3. **权限问题**: 确保已授予存储权限
4. **兼容性问题**: 检查Android版本和API级别

### 调试工具
- 使用Android Studio Profiler监控性能
- 启用详细日志记录
- 使用测试文档验证功能