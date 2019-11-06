# coreAndroid
[![](https://jitpack.io/v/VRGsoftUA/coreAndroid.svg)](https://jitpack.io/#VRGsoftUA/coreAndroid)

core modules for android applications

*Все модули предполагают использование Kodein в качестве di.

## retrofit

модуль для упрощения работы с Retrofit. 

##### Использование

1. Настроить конфигурацию модуля в Application классе приложения:
```gradle
RetrofitConfig.apply {
            baseUrl = baseUrlValue  // укажите базовый url
            auth = interceptor      // укажите интерцептор для создания хедера авторизации (необходимо наследовать от Auth класса)
            enableLogging()         // если необходимо логгирование, включите его посредством вызова этого метода
        }
```

2. Подключите модуль в di:
```gradle
object AppModule {
    fun module(application: App) = Kodein.Module("AppModule") {
        //...
        import(RetrofitModule.get())
        //...
    }
}

```

После этого можно инджектить Retrofit класс где это необходимо.

##### Подключение

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
   implementation "com.github.VRGsoftUA.coreAndroid:retrofit:$coreAndroid_version"
}
```
