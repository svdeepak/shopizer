#!/bin/bash

TOKEN=$(curl -s "http://localhost:8080/api/v1/private/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@shopizer.com","password":"password"}' | \
  python3 -c "import sys, json; print(json.load(sys.stdin)['token'])")

echo "✓ Authenticated"
echo ""

# Product 1
echo "Creating Product 1: Wireless Headphones..."
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "WH001",
    "visible": true,
    "available": true,
    "productShipeable": true,
    "type": "GENERAL",
    "inventory": {
      "sku": "WH001",
      "quantity": 50,
      "price": {
        "defaultPrice": true,
        "price": 149.99
      }
    },
    "descriptions": [{
      "language": "en",
      "name": "Wireless Headphones",
      "description": "Premium wireless headphones with noise cancellation",
      "friendlyUrl": "wireless-headphones"
    }]
  }' > /tmp/p1.json

P1=$(cat /tmp/p1.json)
if echo "$P1" | grep -q '"id"'; then
  P1_ID=$(echo "$P1" | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])")
  echo "  ✓ Created (ID: $P1_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/products/$P1_ID/group/FEATURED_ITEM?store=DEFAULT&lang=en" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
  echo "  ✓ Added to FEATURED_ITEM"
else
  echo "  ✗ Failed:"
  echo "$P1" | python3 -m json.tool
fi

# Product 2
echo "Creating Product 2: Smartphone Pro..."
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SM001",
    "visible": true,
    "available": true,
    "productShipeable": true,
    "type": "GENERAL",
    "inventory": {
      "sku": "SM001",
      "quantity": 30,
      "price": {
        "defaultPrice": true,
        "price": 899.99
      }
    },
    "descriptions": [{
      "language": "en",
      "name": "Smartphone Pro",
      "description": "Latest flagship smartphone with advanced features",
      "friendlyUrl": "smartphone-pro"
    }]
  }' > /tmp/p2.json

P2=$(cat /tmp/p2.json)
if echo "$P2" | grep -q '"id"'; then
  P2_ID=$(echo "$P2" | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])")
  echo "  ✓ Created (ID: $P2_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/products/$P2_ID/group/FEATURED_ITEM?store=DEFAULT&lang=en" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
  echo "  ✓ Added to FEATURED_ITEM"
else
  echo "  ✗ Failed:"
  echo "$P2" | python3 -m json.tool
fi

# Product 3
echo "Creating Product 3: Gaming Laptop..."
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "LAP001",
    "visible": true,
    "available": true,
    "productShipeable": true,
    "type": "GENERAL",
    "inventory": {
      "sku": "LAP001",
      "quantity": 20,
      "price": {
        "defaultPrice": true,
        "price": 1299.99
      }
    },
    "descriptions": [{
      "language": "en",
      "name": "Gaming Laptop",
      "description": "High-performance gaming laptop with RTX graphics",
      "friendlyUrl": "gaming-laptop"
    }]
  }' > /tmp/p3.json

P3=$(cat /tmp/p3.json)
if echo "$P3" | grep -q '"id"'; then
  P3_ID=$(echo "$P3" | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])")
  echo "  ✓ Created (ID: $P3_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/products/$P3_ID/group/FEATURED_ITEM?store=DEFAULT&lang=en" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
  echo "  ✓ Added to FEATURED_ITEM"
else
  echo "  ✗ Failed:"
  echo "$P3" | python3 -m json.tool
fi

# Product 4
echo "Creating Product 4: Tablet Pro..."
curl -s -X POST "http://localhost:8080/api/v1/private/product" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "TAB001",
    "visible": true,
    "available": true,
    "productShipeable": true,
    "type": "GENERAL",
    "inventory": {
      "sku": "TAB001",
      "quantity": 40,
      "price": {
        "defaultPrice": true,
        "price": 599.99
      }
    },
    "descriptions": [{
      "language": "en",
      "name": "Tablet Pro",
      "description": "Professional tablet with stylus support",
      "friendlyUrl": "tablet-pro"
    }]
  }' > /tmp/p4.json

P4=$(cat /tmp/p4.json)
if echo "$P4" | grep -q '"id"'; then
  P4_ID=$(echo "$P4" | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])")
  echo "  ✓ Created (ID: $P4_ID)"
  curl -s -X POST "http://localhost:8080/api/v1/private/products/$P4_ID/group/FEATURED_ITEM?store=DEFAULT&lang=en" \
    -H "Authorization: Bearer $TOKEN" > /dev/null
  echo "  ✓ Added to FEATURED_ITEM"
else
  echo "  ✗ Failed:"
  echo "$P4" | python3 -m json.tool
fi

echo ""
echo "✅ Done! Verifying..."
curl -s "http://localhost:8080/api/v1/products/group/FEATURED_ITEM?store=DEFAULT&lang=en" | \
  python3 -c "import sys, json; data=json.load(sys.stdin); print(f'Featured products: {len(data[\"products\"])}'); [print(f'  - {p[\"description\"][\"name\"]} (\${p[\"price\"]})') for p in data['products']]"

echo ""
echo "Check React app: http://localhost:3000"
