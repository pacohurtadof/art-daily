# Reglas de ProGuard/R8 específicas del proyecto. Las reglas por defecto de Android
# (proguard-android-optimize.txt) ya cubren la mayoría de los casos; las librerías que
# usamos (Room, Hilt, Retrofit, kotlinx.serialization, Coil, Glance, WorkManager) traen
# sus propias reglas empaquetadas en sus .aar, así que no hace falta duplicarlas aquí.
#
# Agregar reglas puntuales solo si R8 rompe algo específico del proyecto en un build de
# release (típicamente: reflexión sobre clases nuestras que R8 no puede rastrear).
