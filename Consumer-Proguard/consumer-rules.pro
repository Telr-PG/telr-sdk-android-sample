# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class *


-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-keep public class * extends androidx.versionedparcelable.VersionedParcelable

-keep public class * extends android.app.Application

#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider

#-libraryjars  libs/commons-io-2.2.jar

#-keep public class * extends android.view.View {
#      public <init>(android.content.Context);
#      public <init>(android.content.Context, android.util.AttributeSet);
#      public <init>(android.content.Context, android.util.AttributeSet, int);
#      public void set*(...);
#}

#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}

#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}

#-keepclassmembers class * extends android.content.Context {
#    public void *(android.view.View);
#    public void *(android.view.MenuItem);
#}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

#-keepclassmembers class **.R$* {
#    public static <fields>;
#}

#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}

#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}

#-keep class mypackage.MyCallbackClass {
#    void myCallbackMethod(java.lang.String);
#}

#-keepclasseswithmembernames,includedescriptorclasses class * {
#    native <methods>;
#}

# This is a configuration file for ProGuard.

# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-keepparameternames
#-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve all annotations.

-keepattributes *Annotation*,Signature
-keep class org.springframework.** { *; }
#-keep public class com.telr.** { *;}
-keep public class com.telr.mobile.sdk.activity.** { *;}
-keep public class com.telr.mobile.sdk.entity.** { *;}
-keep class com.telr.mobile.sdk.entity** {*;}
-keep class com.telr.mobile.sdk.activity** {*;}
-keepclassmembers class com.telr.mobile.sdk.entity** {*;}
-keepclassmembers class com.telr.mobile.sdk.activity** {*;}
-keep class org.simpleframework.** { *;}
-keep class android.** { *;}
-keep interface com.telr.mobile.sdk.service.InitiatePaymentListener {*;}
-keep interface com.telr.mobile.sdk.service.StatusListener {*;}
-keep class com.telr.mobile.sdk.entity.** { *; }

#Proguard rules for simpleXML
-keep interface org.simpleframework.xml.core.Label {
   public *;
}
-keep class * implements org.simpleframework.xml.core.Label {
   public *;
}
-keep interface org.simpleframework.xml.core.Parameter {
   public *;
}
-keep class * implements org.simpleframework.xml.core.Parameter {
   public *;
}
-keep interface org.simpleframework.xml.core.Extractor {
   public *;
}
-keep class * implements org.simpleframework.xml.core.Extractor {
   public *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class org.springframework.http.ResponseEntity {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep class * implements android.os.Parcelable {
     public static final android.os.Parcelable$Creator *;
   }
 -keepclassmembers public class org.springframework {
     public *;
 }
 -keep interface org.simpleframework.xml.core.Label {
    public *;
 }
 -keep class * implements org.simpleframework.xml.core.Label {
    public *;
 }
 -keep interface org.simpleframework.xml.core.Parameter {
    public *;
 }
 -keep class * implements org.simpleframework.xml.core.Parameter {
    public *;
 }
 -keep interface org.simpleframework.xml.core.Extractor {
    public *;
 }
 -keep class * implements org.simpleframework.xml.core.Extractor {
    public *;
 }
 -keepattributes SourceFile, LineNumberTable
# Preserve all .class method names.

#-keepclassmembernames class * {
#    java.lang.Class class$(java.lang.String);
#    java.lang.Class class$(java.lang.String, boolean);
#}

# Preserve all native method names and the names of their classes.

#-keepclasseswithmembernames class * {
#    native <methods>;
#}

# Your library may contain more items that need to be preserved;
# typically classes that are dynamically created using Class.forName:

# -keep public class mypackage.MyClass
# -keep public interface mypackage.MyInterface
# -keep public class * implements mypackage.MyInterface
