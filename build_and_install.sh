#!/bin/bash

# DocTool 一键构建和安装脚本
# 需要: Android SDK, Gradle, ADB

set -e

echo "========================================"
echo "DocTool - 文档格式识别与编辑工具"
echo "一键构建和安装脚本"
echo "========================================"

# 检查环境
check_environment() {
    echo "检查构建环境..."
    
    # 检查Java
    if ! command -v java &> /dev/null; then
        echo "❌ 未找到Java，请安装JDK 17+"
        exit 1
    fi
    
    # 检查Android SDK
    if [ -z "$ANDROID_HOME" ]; then
        echo "⚠️  未设置ANDROID_HOME环境变量"
        echo "请设置: export ANDROID_HOME=/path/to/android/sdk"
        exit 1
    fi
    
    # 检查Gradle
    if ! command -v gradle &> /dev/null; then
        echo "❌ 未找到Gradle，请安装Gradle 8.0+"
        exit 1
    fi
    
    echo "✅ 环境检查通过"
}

# 准备项目结构
prepare_project() {
    echo "准备项目结构..."
    
    # 创建必要的目录
    mkdir -p app/src/main/res/{drawable,layout,values}
    mkdir -p app/src/main/assets
    
    # 创建默认资源文件
    if [ ! -f "app/src/main/res/values/strings.xml" ]; then
        cat > app/src/main/res/values/strings.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">DocTool</string>
    <string name="action_settings">设置</string>
    <string name="file_operation">文件操作</string>
    <string name="format_edit">格式编辑</string>
    <string name="template_library">模板库</string>
</resources>
EOF
    fi
    
    # 创建默认图标（占位符）
    if [ ! -f "app/src/main/res/drawable/ic_launcher.png" ]; then
        echo "⚠️  缺少应用图标，请添加 ic_launcher.png"
    fi
    
    echo "✅ 项目结构准备完成"
}

# 创建Gradle构建文件
create_gradle_files() {
    echo "创建Gradle构建文件..."
    
    # 项目级 build.gradle
    cat > build.gradle.kts << 'EOF'
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}
EOF
    
    # 模块级 build.gradle
    cat > app/build.gradle.kts << 'EOF'
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.doctool"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.doctool"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

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
    implementation("androidx.compose.material:material-icons-extended")
    
    // 文件处理（注释掉，需要手动下载）
    // implementation("org.apache.pdfbox:pdfbox:3.0.2")
    // implementation("org.apache.poi:poi:5.2.5")
    // implementation("org.apache.poi:poi-ooxml:5.2.5")
    
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
EOF
    
    # Gradle包装器属性
    cat > gradle/wrapper/gradle-wrapper.properties << 'EOF'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF
    
    echo "✅ Gradle文件创建完成"
}

# 创建Android清单
create_manifest() {
    echo "创建Android清单文件..."
    
    cat > app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!-- 存储权限 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    
    <!-- 网络权限（可选，用于未来更新） -->
    <uses-permission android:name="android.permission.INTERNET" />
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.DocTool"
        android:supportsRtl="true">
        
        <activity
            android:name=".DocToolApp"
            android:exported="true"
            android:windowSoftInputMode="adjustResize"
            android:screenOrientation="portrait">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            
            <!-- 支持的文件类型 -->
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="application/pdf" />
            </intent-filter>
            
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="application/msword" />
                <data android:mimeType="application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
            </intent-filter>
            
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:mimeType="image/*" />
            </intent-filter>
        </activity>
        
    </application>
</manifest>
EOF
    
    echo "✅ Android清单创建完成"
}

# 构建项目
build_project() {
    echo "开始构建项目..."
    
    # 下载Gradle包装器（如果需要）
    if [ ! -f "gradlew" ]; then
        echo "下载Gradle包装器..."
        gradle wrapper
    fi
    
    # 赋予执行权限
    chmod +x gradlew
    
    # 清理并构建
    echo "执行Gradle构建..."
    ./gradlew clean assembleDebug
    
    # 检查构建结果
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "✅ 构建成功！"
        echo "📦 APK文件: $APK_PATH"
        echo "📊 文件大小: $APK_SIZE"
    else
        echo "❌ 构建失败！"
        exit 1
    fi
}

# 安装到设备
install_to_device() {
    echo "检查连接的Android设备..."
    
    # 检查ADB
    if ! command -v adb &> /dev/null; then
        echo "⚠️  未找到ADB，跳过安装步骤"
        return
    fi
    
    # 检查设备连接
    DEVICE_COUNT=$(adb devices | grep -v "List of devices" | grep -c "device$")
    
    if [ "$DEVICE_COUNT" -eq 0 ]; then
        echo "⚠️  未检测到连接的Android设备"
        echo "请通过USB连接设备并启用USB调试"
        return
    fi
    
    echo "检测到 $DEVICE_COUNT 台设备"
    
    # 安装APK
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    echo "正在安装APK..."
    
    if adb install -r "$APK_PATH"; then
        echo "✅ 安装成功！"
        echo "📱 应用已安装到设备"
        
        # 可选：自动启动应用
        read -p "是否要启动应用？(y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo "启动DocTool应用..."
            adb shell am start -n com.doctool/.DocToolApp
        fi
    else
        echo "❌ 安装失败！"
    fi
}

# 生成简易APK（备用方案）
create_simple_apk() {
    echo "生成简易APK（开发测试版）..."
    
    # 创建临时目录
    TEMP_DIR="temp_apk"
    mkdir -p "$TEMP_DIR"
    
    # 复制必要的文件
    cp -r app/src/main/java "$TEMP_DIR/"
    cp app/src/main/AndroidManifest.xml "$TEMP_DIR/"
    cp -r app/src/main/res "$TEMP_DIR/" 2>/dev/null || true
    
    # 创建说明文件
    cat > "$TEMP_DIR/README.txt" << 'EOF'
DocTool 开发测试版

这是一个简化版的DocTool应用，包含：
1. 完整的UI界面
2. 参数编辑器组件
3. 模板系统架构
4. 文档解析器接口

需要手动集成的功能：
1. PDF解析库 (PDFBox)
2. Office文档支持 (Apache POI)
3. 数据库实现

构建说明：
1. 使用Android Studio打开项目
2. 添加必要的依赖库
3. 构建并运行

项目结构已完整，只需添加实际的文件处理库。
EOF
    
    # 打包
    ZIP_FILE="DocTool_DevPreview_$(date +%Y%m%d_%H%M%S).zip"
    zip -r "$ZIP_FILE" "$TEMP_DIR"
    
    echo "✅ 简易包创建完成: $ZIP_FILE"
    echo "📦 包含完整的源代码和项目结构"
    
    # 清理
    rm -rf "$TEMP_DIR"
}

# 主流程
main() {
    echo "选择操作模式:"
    echo "1. 完整构建（需要Android开发环境）"
    echo "2. 生成源代码包（无需构建环境）"
    echo "3. 仅检查项目结构"
    read -p "请输入选择 (1-3): " -n 1 -r
    echo
    
    case $REPLY in
        1)
            check_environment
            prepare_project
            create_gradle_files
            create_manifest
            build_project
            install_to_device
            ;;
        2)
            prepare_project
            create_manifest
            create_simple_apk
            ;;
        3)
            echo "当前项目结构:"
            find . -type f -name "*.kt" -o -name "*.xml" | sort
            echo ""
            echo "总文件数: $(find . -type f -name "*.kt" -o -name "*.xml" | wc -l)"
            ;;
        *)
            echo "无效选择"
            exit 1
            ;;
    esac
    
    echo ""
    echo "========================================"
    echo "DocTool 项目处理完成！"
    echo "========================================"
}

# 执行主函数
main "$@"