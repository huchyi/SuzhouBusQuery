# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# 通过线路时时查询
#（1）线路号
#"http://content.2500city.com/Json?method=SearchBusLine&appVersion=3.6&deviceID=866523018841548&lineName=108"
#（2）线路方向
#http://content.2500city.com/Json?method=GetBusLineDetail&appVersion=3.6&deviceID=866523018841548&Guid=0596b2fe-84f3-46a1-a410-59243bd71080

#站点查询
#（1）模糊名
#http://content.2500city.com/Json?method=SearchBusStation&appVersion=3.6&deviceID=866523018841548&standName=%E8%8B%8F%E5%A4%A7%E5%8C%97
#（1）详细名
#http://content.2500city.com/Json?method=GetBusStationDetail&appVersion=3.6&deviceID=866523018841548&NoteGuid=AUE

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
