plugins {
    id("com.android.application")
    id("com.google.protobuf")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gr"
    compileSdk = 34
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
    defaultConfig {
        applicationId = "com.example.gr"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        named("main") {
            java.srcDirs("build/generated/source/proto/main/java")
        }
    }
}

dependencies {
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    //protobuf
    implementation("com.google.protobuf:protobuf-javalite:3.21.7")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation ("androidx.preference:preference:1.1.0")
    // for device support class
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.0")
    implementation ("net.e175.klaus:solarpositioning:0.0.9")
    implementation ("org.cyanogenmod:platform.sdk:6.0")
    //Jsoup for scraping html
    implementation("org.jsoup:jsoup:1.15.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //GSON
    implementation ("com.google.code.gson:gson:2.8.9")
    //XML
    implementation ("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.7")
    // Maps
    val mapsforge_version = "0.17.0"
    implementation ("org.mapsforge:mapsforge-core:$mapsforge_version")
    implementation ("org.mapsforge:mapsforge-map:$mapsforge_version")
    implementation ("org.mapsforge:mapsforge-map-reader:$mapsforge_version")
    implementation ("org.mapsforge:mapsforge-themes:$mapsforge_version")
    implementation ("org.mapsforge:mapsforge-map-android:$mapsforge_version")
    implementation ("com.caverock:androidsvg:1.4")
    //array utils
    implementation ("com.github.pfichtner:durationformatter:0.1.1")

    implementation ("org.apache.commons:commons-lang3:3.7")
    //logger
    implementation ("org.slf4j:slf4j-api:2.0.7")
    implementation ("com.github.tony19:logback-android:3.0.0")
    //greenrobot
    implementation ("org.greenrobot:greendao:2.2.1")
    //Charts
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Room database
    // Android Room Database
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    //event bus
    implementation ("org.greenrobot:eventbus:3.3.1")
    // Circle Imageview
    implementation ("de.hdodenhof:circleimageview:3.0.0")
    //MaterialDialog
    implementation("com.afollestad.material-dialogs:core:0.9.6.0")
    // Glide load image
    implementation ("com.github.bumptech.glide:glide:3.7.0")

}
