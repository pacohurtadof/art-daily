# Reglas de ProGuard/R8 específicas del proyecto. Las reglas por defecto de Android
# (proguard-android-optimize.txt) ya cubren la mayoría de los casos; las librerías que
# usamos (Room, Hilt, Retrofit, kotlinx.serialization, Coil, Glance, WorkManager) traen
# sus propias reglas empaquetadas en sus .aar, así que no hace falta duplicarlas aquí.
#
# Agregar reglas puntuales solo si R8 rompe algo específico del proyecto en un build de
# release (típicamente: reflexión sobre clases nuestras que R8 no puede rastrear).

# ML Kit (traducción + detección de idioma, 2026-08-21): descubre sus componentes en
# tiempo de ejecución instanciando reflexivamente clases *Registrar cuyo nombre está
# declarado como string en el manifest (com.google.mlkit.common.internal
# .MlKitInitProvider) — las reglas que traen sus propios .aar (com.google.mlkit:common/
# translate/language-id) NO cubren esto, se verificó leyendo su proguard.txt empaquetado.
# Sin esta regla, R8 saca el constructor sin argumentos de esas clases y crashea en
# tiempo real con NoSuchMethodException al tocar "Traducir" — encontrado probando el
# build de release de verdad en el emulador (no se ve compilando, solo ejecutando).
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**
