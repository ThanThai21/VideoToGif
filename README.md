# VideoToGif

Android/Java module provide gif making feature.

# All this library in your project
1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add the dependency
```
dependencies {
	        compile 'com.github.ThanThai21:VideoToGif:1.0.1'
	}
```

# Usage

1. Declare an instance of VideoToGifConverter
```
VideoToGifConverter converter = new VideoToGifConverter(context, uri);
```
You can use orther constructor by passing different parameter. 
```
VideoToGifConverter(MediaDataSource mediaDataSource);
VideoToGifConverter(FileDescriptor fileDescriptor);
```

2. Generate GIF
```
byte[] gif = converter.generateGIF(delay);
```
parameter is the delay time between each frame in milisecond. This function returns byte array. You can use it to save gif in storage.
The simplest way to display gif in imageview is using Glide:
```
Glide.with(context).asGif().load(gif).into(gifView);
```
@gif You can pass File object or byte[] as parameter inside load function.
@gifView ImageView where you want to display GIF. 
