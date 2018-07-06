# Vago

[![Vago](https://jitpack.io/v/ivanisidrowu/vago.svg)](https://jitpack.io/#ivanisidrowu/vago)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Vago-green.svg?style=flat)](https://android-arsenal.com/details/1/7015)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Get bored with writing tedious and dull model tests?

Vago helps you write tests based on POJOs and supports test generation for POJOs transformations.

* Avoid writing model testing boilerplates
* Writing tests of POJOs faster
* Test object transformation with simple implementation and annotations

[Click for examples!](https://github.com/ivanisidrowu/vago/tree/master/app/src/test/java/tw/invictus/vago)

## Test General POJOs or Beans

### Auto test without customization
```kotlin
Vago.testClass(AudioBean::class)
```
### Auto test with customization
```kotlin
Vago.testClass(AudioBean::class, object: Vago.VagoCustomization() {
            override fun getDefaultValueByClass(clazz: Class<*>): Any? {
                if (clazz.isString()) {
                    return "1"
                }
                return super.getDefaultValueByClass(clazz)
            }
        })
```
### Get Instance directly
```kotlin
// without customization
val bean = Vago.createInstance(AudioBean::class)
// with customization
val bean = Vago.createInstance(AudioBean::class, yourCustomization)
```
## Vago Annotations
Vago has annotations to help you test object transformation. 

### @VagoMethod
It marks the method which can be tested for object transformation.

### @VagoMapping
It establishes attribute name and type mapping for testing transformation. It's useful to map those attributes which have different names and types. Then Vago will generate testing code for you. You can use generated code to test your methods. The exmple below shows you how to use Vago. In the example, ```AudioRespVo``` has attribute ```childId```. It also has ```toAudio()``` function to do the object transformation. However, to test the transformaiton can be really boring and troublesome. As you can see the ```childId``` attribute is transformed to ```chId``` in AudioBean. Also, the type tranforms from String to Long. ```@VagoMapping``` can help them mapping names and type conversion.

### Example
For instance, you have a value object like this one.

```kotlin
class AudioRespVo {
    var id: Long? = null
        get() = field ?: 0L
    var type: Short? = null
        get() = field ?: 0
    var title: String? = null
        get() = field ?: ""
    var mediaUrl: String? = null
        get() = field ?: ""
    var coverUrl: String? = null
        get() = field ?: ""

    @VagoMapping(name = "chId", type = Long::class)
    var childId: String? = null
        get() = field ?: ""
}

@VagoMethod
fun AudioRespVo.toAudio(): AudioBean {
    return AudioBean(id!!, type!!, title!!, mediaUrl!!, coverUrl!!, childId!!.toLong())
}
```

```kotlin
data class AudioBean(var id: Long, var type: Short, var title: String,
                     var mediaUrl: String, var coverUrl: String, var chId: Long)
```

After add annotation to the methods, we need to **"REBUILD"** project to generate code for testing tranformation.
Then use the generated class to test it in unit tests. In this case, the generated class named ```VagoAudioRespVoKt```.

```kotlin
@Test
fun testVoToBean() {
    // VagoAudioRespVoKt is the generated class to help testing.
    VagoAudioRespVoKt.testToAudio(object: Vago.VagoCustomization() {
        override fun getDefaultValueByClass(clazz: Class<*>): Any? {
            if (clazz.isString()) {
                return "1"
            }
            return super.getDefaultValueByClass(clazz)
        }
    })
}
```

## Customization
This is a class that defines your own customization for testing classes. ```Customization``` can be put as parameter into ```Vago.testClass()``` or any Vago generated classes.

```kotlin
/*
 * Get Default Value by Class.
 * For example, if the class is String, return a default string "test string".
 */
override fun getDefaultValueByClass(clazz: Class<*>): Any? = null

/*
 * Get Default Value of Specific Attribute
 * For example, if the class has an attribute called "id", return a Long with 0L value.
 */
override fun getDefaultValueForSpecificAttribute(attr: String): Any? = null

/*
 * Skip Attributes that you do not want to test
 */
override fun isSkipAttribute(attr: String) = false

/*
 * Get Custom Convert type between different classes
 */
override fun getConvertedType(originalType: Class<*>, wantedType: Class<*>): Any? = null
```
### Example

```kotlin
VagoAudioRespVoKt.testToAudio(object: Vago.VagoCustomization() {
            override fun getDefaultValueByClass(clazz: Class<*>): Any? {
                if (clazz.isString()) {
                    return "1"
                }
                return super.getDefaultValueByClass(clazz)
            }
        })
```

## Download
Add this repo to the root ```build.gradle``` file.
```gradle
allprojects {
  repositories {
  ...
  maven { url 'https://jitpack.io' }
  }
}
```
Then add this dependency to app's ```build.gradle``` file.
```gradle
dependencies {
  compile 'com.github.ivanisidrowu:vago:v1.0.3'
}
```

## Contribution
Contributions are always welcome. If you have any ideas or suggestions, you can contact me or create a github issue.
