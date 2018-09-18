package tw.invictus.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class VagoParcel(val parentName: String = "")