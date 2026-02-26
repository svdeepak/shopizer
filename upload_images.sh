#!/bin/bash

TOKEN=$(curl -s "http://localhost:8080/api/v1/private/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@shopizer.com","password":"password"}' | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['token'])")

IMAGE_PATH="/Users/deepaksankanal/Documents/GitHub/shopizer-admin /Kiro-icon.png"

echo "Uploading images to all products..."
echo ""

for pid in 1 2 3 4; do
  echo "Product $pid..."
  HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
    "http://localhost:8080/api/v1/private/product/$pid/image?store=DEFAULT&lang=en" \
    -H "Authorization: Bearer $TOKEN" \
    -F "file=@$IMAGE_PATH" \
    -F "order=0" \
    -F "defaultImage=true")
  
  if [ "$HTTP_CODE" = "201" ]; then
    echo "  ✓ Image uploaded (HTTP $HTTP_CODE)"
  else
    echo "  ✗ Failed (HTTP $HTTP_CODE)"
  fi
done

echo ""
echo "Verifying images..."
curl -s "http://localhost:8080/api/v1/products/group/FEATURED_ITEM?store=DEFAULT&lang=en" | \
  python3 -c "
import sys, json
data = json.load(sys.stdin)
for p in data['products']:
    name = p['description']['name']
    img_count = len(p.get('images', []))
    status = '✓' if img_count > 0 else '✗'
    print(f'{status} {name}: {img_count} image(s)')
    if img_count > 0:
        for img in p['images']:
            print(f'    {img.get(\"imageUrl\", \"N/A\")}')
"

echo ""
echo "Check React app: http://localhost:3000"
