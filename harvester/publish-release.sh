#!/usr/bin/env bash
# Publica harvester/output/artworks.db + el delta.json más reciente como un release
# nuevo en GitHub — la app sincroniza automáticamente contra "latest" (ver
# ArtworkSyncService en :app). Correr DESPUÉS de una cosecha nueva
# (`./gradlew :harvester:run --args="bulk 2000"`), desde la raíz del repo o desde acá.
#
# Requiere `gh` autenticado (`gh auth status` para comprobar) y permiso de push al repo.
set -euo pipefail

cd "$(dirname "$0")/output"

if [ ! -f artworks.db ]; then
    echo "No hay artworks.db en harvester/output/ — corré el harvester primero." >&2
    exit 1
fi

# El delta más reciente por fecha en el nombre (artworks-delta-YYYYMMDD.json).
latest_delta=$(ls -1 artworks-delta-*.json 2>/dev/null | sort | tail -1)
if [ -z "$latest_delta" ]; then
    echo "No hay ningún artworks-delta-*.json en harvester/output/." >&2
    exit 1
fi

# Nombre de asset ESTABLE ("delta.json", sin fecha) — la app busca ese nombre exacto
# en el último release, no le importa cómo se llame el archivo local.
cp "$latest_delta" delta.json

tag="data-$(date +%Y%m%d)"
echo "Publicando release $tag (delta: $latest_delta, $(wc -l < "$latest_delta" 2>/dev/null || echo '?') líneas)..."

gh release create "$tag" \
    artworks.db \
    delta.json \
    --repo pacohurtadof/art-daily \
    --title "Datos $(date +%Y-%m-%d)" \
    --notes "Cosecha del $(date +%Y-%m-%d). Ver docs/bitacora.md del repo para el detalle."

echo "Listo: https://github.com/pacohurtadof/art-daily/releases/tag/$tag"
echo "Recordatorio: copiar artworks.db también a app/src/main/assets/ para que el primer arranque (createFromAsset) tenga los datos nuevos — el sync solo cubre instalaciones YA existentes."
