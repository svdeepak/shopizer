#!/bin/bash

TOKEN=$(curl -s "http://localhost:8080/api/v1/private/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@shopizer.com","password":"password"}' | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['token'])")

echo "✓ Authenticated"
echo ""
echo "Creating products with proper structure..."

# Product 1
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "WH001",
    "price": 149.99,
    "quantity": 50,
    "productSpecifications": {"weight": 0.5},
    "type": "GENERAL",
    "available": true,
    "visible": true,
    "productShipeable": true,
    "description": {
      "language": "en",
      "name": "Wireless Headphones",
      "description": "Premium wireless headphones with noise cancellation",
      "friendlyUrl": "wireless-headphones"
    },
    "availability": {
      "owner": "DEFAULT",
      "region": "*",
      "available": true
    }
  }' > /tmp/p1.json

P1_ID=$(python3 -c "import json; print(json.load(open('/tmp/p1.json'))['id'])" 2>/dev/null)
if [ ! -z "$P1_ID" ]; then
  echo "✓ Created: Wireless Headphones (ID: $P1_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/product/$P1_ID/group/FEATURED_ITEM" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
else
  echo "✗ Failed: Wireless Headphones"
  cat /tmp/p1.json
fi

# Product 2
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SM001",
    "price": 899.99,
    "quantity": 30,
    "productSpecifications": {"weight": 0.2},
    "type": "GENERAL",
    "available": true,
    "visible": true,
    "productShipeable": true,
    "description": {
      "language": "en",
      "name": "Smartphone Pro",
      "description": "Latest flagship smartphone with advanced features",
      "friendlyUrl": "smartphone-pro"
    },
    "availability": {
      "owner": "DEFAULT",
      "region": "*",
      "available": true
    }
  }' > /tmp/p2.json

P2_ID=$(python3 -c "import json; print(json.load(open('/tmp/p2.json'))['id'])" 2>/dev/null)
if [ ! -z "$P2_ID" ]; then
  echo "✓ Created: Smartphone Pro (ID: $P2_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/product/$P2_ID/group/FEATURED_ITEM" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
else
  echo "✗ Failed: Smartphone Pro"
  cat /tmp/p2.json
fi

# Product 3
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "LAP001",
    "price": 1299.99,
    "quantity": 20,
    "productSpecifications": {"weight": 2.0},
    "type": "GENERAL",
    "available": true,
    "visible": true,
    "productShipeable": true,
    "description": {
      "language": "en",
      "name": "Gaming Laptop",
      "description": "High-performance gaming laptop with RTX graphics",
      "friendlyUrl": "gaming-laptop"
    },
    "availability": {
      "owner": "DEFAULT",
      "region": "*",
      "available": true
    }
  }' > /tmp/p3.json

P3_ID=$(python3 -c "import json; print(json.load(open('/tmp/p3.json'))['id'])" 2>/dev/null)
if [ ! -z "$P3_ID" ]; then
  echo "✓ Created: Gaming Laptop (ID: $P3_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/product/$P3_ID/group/FEATURED_ITEM" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
else
  echo "✗ Failed: Gaming Laptop"
  cat /tmp/p3.json
fi

# Product 4
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "TAB001",
    "price": 599.99,
    "quantity": 40,
    "productSpecifications": {"weight": 0.5},
    "type": "GENERAL",
    "available": true,
    "visible": true,
    "productShipeable": true,
    "description": {
      "language": "en",
      "name": "Tablet Pro",
      "description": "Professional tablet with stylus support",
      "friendlyUrl": "tablet-pro"
    },
    "availability": {
      "owner": "DEFAULT",
      "region": "*",
      "available": true
    }
  }' > /tmp/p4.json

P4_ID=$(python3 -c "import json; print(json.load(open('/tmp/p4.json'))['id'])" 2>/dev/null)
if [ ! -z "$P4_ID" ]; then
  echo "✓ Created: Tablet Pro (ID: $P4_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/product/$P4_ID/group/FEATURED_ITEM" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
else
  echo "✗ Failed: Tablet Pro"
  cat /tmp/p4.json
fi

echo ""
echo "✅ Done! Check React app at http://localhost:3000"
